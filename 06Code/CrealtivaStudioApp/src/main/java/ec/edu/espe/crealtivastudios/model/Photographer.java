package ec.edu.espe.crealtivastudios.model;

import java.util.*;
import java.util.stream.Collectors;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import utils.MongoConnection;

public class Photographer {

    private static final List<Photographer> photographers = new ArrayList<>();

    private int id;
    private String name;
    private String assignedEvent;
    private List<String> equipment;
    private boolean attending;
    private boolean assigned;

    // =====================================================
    // ✅ CONSTRUCTOR PRINCIPAL (USADO POR MONGO)
    // =====================================================
public Photographer(int id, String name, String assignedEvent,
                    List<String> equipment, boolean attending) {

    this.id = id;
    this.name = name;
    this.assignedEvent = assignedEvent;
    this.attending = attending;
    
    // --- 🔥 CORRECCIÓN CLAVE ---
    // El campo 'assigned' DEBE reflejar si hay un evento asignado,
    // o si se marcó como NO DISPONIBLE en FrmAvailable
    this.assigned = assignedEvent != null && !assignedEvent.isBlank(); 
    
    this.equipment = (equipment != null) ? equipment : new ArrayList<>();
}

    // =====================================================
    // ✅ CONSTRUCTOR COMPATIBLE CON CÓDIGO ANTIGUO (STRING)
    // =====================================================
    public Photographer(int id, String name, String assignedEvent,
                        String equipmentString, boolean attending) {

        this(
            id,
            name,
            assignedEvent,
            (equipmentString == null || equipmentString.isBlank())
                ? new ArrayList<>()
                : Arrays.stream(equipmentString.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList()),
            attending
        );
    }

    // =====================================================
    // ✅ LISTAR DESDE MONGO
    // =====================================================
    public static List<Photographer> findAll() {
        List<Photographer> list = new ArrayList<>();

        MongoConnection.connect();
        MongoCollection<Document> collection = MongoConnection.getPhotographerCollection();

        for (Document doc : collection.find()) {
            Photographer p = new Photographer(
                doc.getInteger("id"),
                doc.getString("name"),
                doc.getString("assignedEvent"),
                (List<String>) doc.getOrDefault("equipment", new ArrayList<>()),
                doc.getBoolean("attending", false)
            );
            list.add(p);
        }
        return list;
    }

    // =====================================================
    // ✅ FIND BY ID
    // =====================================================
    public static Photographer findById(int id) {
        MongoCollection<Document> collection = MongoConnection.getPhotographerCollection();
        Document doc = collection.find(new Document("id", id)).first();
        if (doc == null) return null;

        return new Photographer(
            doc.getInteger("id"),
            doc.getString("name"),
            doc.getString("assignedEvent"),
            (List<String>) doc.getOrDefault("equipment", new ArrayList<>()),
            doc.getBoolean("attending", false)
        );
    }

    // =====================================================
    // ✅ FIND BY NAME
    // =====================================================
    public static Photographer findByName(String name) {
        MongoCollection<Document> collection = MongoConnection.getPhotographerCollection();
        Document doc = collection.find(new Document("name", name)).first();
        if (doc == null) return null;

        return new Photographer(
            doc.getInteger("id"),
            doc.getString("name"),
            doc.getString("assignedEvent"),
            (List<String>) doc.getOrDefault("equipment", new ArrayList<>()),
            doc.getBoolean("attending", false)
        );
    }

    // =====================================================
    // ✅ ID AUTO
    // =====================================================
    public static int getNextId() {
        MongoCollection<Document> collection = MongoConnection.getPhotographerCollection();
        Document last = collection.find().sort(new Document("id", -1)).first();
        return (last == null) ? 1 : last.getInteger("id") + 1;
    }

    // =====================================================
    // ✅ GUARDAR / ACTUALIZAR
    // =====================================================
    public boolean save() {
        MongoCollection<Document> collection = MongoConnection.getPhotographerCollection();

        Document doc = new Document("id", id)
                .append("name", name)
                .append("assignedEvent", assignedEvent)
                .append("equipment", equipment)
                .append("attending", attending)
                .append("assigned", assigned);

        if (collection.find(new Document("id", id)).first() != null) {
            collection.updateOne(
                new Document("id", id),
                new Document("$set", doc)
            );
            photographers.removeIf(p -> p.id == id);
            photographers.add(this);
            return true;
        }

        collection.insertOne(doc);
        photographers.add(this);
        return true;
    }

    // =====================================================
    // ✅ ELIMINAR
    // =====================================================
    public static boolean deletePhotographer(int id) {
        MongoCollection<Document> collection = MongoConnection.getPhotographerCollection();

        long deleted = collection.deleteOne(new Document("id", id)).getDeletedCount();
        if (deleted > 0) {
            photographers.removeIf(p -> p.getId() == id);
            return true;
        }
        return false;
    }

    // =====================================================
    // ✅ DISPONIBILIDAD
    // =====================================================
    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
        if (!assigned) {
            this.assignedEvent = "";
        }
    }

    public String getStatus() {
        return assigned ? "No disponible" : "Disponible";
    }

    // =====================================================
    // ✅ GETTERS / SETTERS
    // =====================================================
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAssignedEvent() { return assignedEvent; }
    public List<String> getEquipment() { return equipment; }
    public boolean isAttending() { return attending; }

    public void setAssignedEvent(String assignedEvent) {
        this.assignedEvent = assignedEvent;
        this.assigned = assignedEvent != null && !assignedEvent.isBlank();
    }

    public void setEquipment(List<String> equipment) {
        this.equipment = equipment;
    }

    // =====================================================
    // ✅ TEXTO PARA UI
    // =====================================================
    public String toSimpleString() {
     // 🔥 CORRECCIÓN: Añadir más detalles si está asignado (ocupado)
    String eventDetail = isAssigned() ? " (" + getAssignedEvent() + ")" : ""; 
    return "ID: " + id + " | " + name + " [" + getStatus() + eventDetail + "]";    }

    public static List<Photographer> getAllPhotographers() {
        return new ArrayList<>(photographers);
    }
}
