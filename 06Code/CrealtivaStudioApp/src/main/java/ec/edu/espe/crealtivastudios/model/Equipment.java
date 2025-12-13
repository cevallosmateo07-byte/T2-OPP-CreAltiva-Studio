package ec.edu.espe.crealtivastudios.model;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import utils.MongoConnection;

public class Equipment {

    private int equipmentId;
    private String name;
    private String description;
    private String status;

    public static final String STATUS_AVAILABLE = "Disponible";
    public static final String STATUS_IN_USE = "En Uso";
    public static final String STATUS_MAINTENANCE = "Mantenimiento";

    // Constructor principal (nuevo equipo)
    public Equipment(String name, String description) {
        this.equipmentId = getNextId();
        this.name = name;
        this.description = description;
        this.status = STATUS_AVAILABLE;
    }

    // Constructor completo (para cargar desde MongoDB)
    public Equipment(int equipmentId, String name, String description, String status) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    /* ===================== ID AUTO ===================== */
    public static int getNextId() {
        MongoCollection<Document> col = MongoConnection.getEquipmentCollection();
        Document last = col.find().sort(new Document("equipmentId", -1)).first();
        return (last == null) ? 1 : last.getInteger("equipmentId") + 1;
    }

    /* ===================== SAVE ===================== */
    public boolean save() {
        MongoCollection<Document> col = MongoConnection.getEquipmentCollection();

        // Si ya existe, actualizar
        if (col.find(new Document("equipmentId", equipmentId)).first() != null) {
            col.updateOne(
                new Document("equipmentId", equipmentId),
                new Document("$set", new Document("name", name)
                        .append("description", description)
                        .append("status", status))
            );
            return true;
        }

        // Insertar nuevo
        Document doc = new Document("equipmentId", equipmentId)
                .append("name", name)
                .append("description", description)
                .append("status", status);
        col.insertOne(doc);
        return true;
    }

    /* ===================== DELETE ===================== */
    public boolean delete() {
        MongoCollection<Document> col = MongoConnection.getEquipmentCollection();
        return col.deleteOne(new Document("equipmentId", equipmentId))
                  .getDeletedCount() > 0;
    }

    /* ===================== FIND ===================== */
    public static Equipment findById(int id) {
        MongoCollection<Document> col = MongoConnection.getEquipmentCollection();
        Document doc = col.find(new Document("equipmentId", id)).first();

        if (doc == null) return null;

        return new Equipment(
                doc.getInteger("equipmentId"),
                doc.getString("name"),
                doc.getString("description"),
                doc.getString("status")
        );
    }

    /* ===================== STATUS ===================== */
    public boolean isAvailable() {
        return STATUS_AVAILABLE.equals(status);
    }

    public void markAsInUse() { updateStatus(STATUS_IN_USE); }
    public void markAsAvailable() { updateStatus(STATUS_AVAILABLE); }
    public void markAsMaintenance() { updateStatus(STATUS_MAINTENANCE); }

    private void updateStatus(String newStatus) {
        this.status = newStatus;
        save(); // Guardar automáticamente los cambios en MongoDB
    }

    /* ===================== GETTERS ===================== */
    public int getEquipmentId() { return equipmentId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }

    /* ===================== UTILITY ===================== */
    public String toSimpleString() {
        return "ID: " + equipmentId + " | " + name + " | Estado: " + status;
    }

}
