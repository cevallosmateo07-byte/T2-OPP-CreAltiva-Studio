package crealtivastudio.model;

import crealtivastudio.json.JsonOperations;
import com.google.gson.reflect.TypeToken; // Asegúrate de importar esto
import java.lang.reflect.Type; // Y esto
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 
 * * @author Object Master, ESPE
 */
public class Photographer {

    // --- Atributos ---
    private int id;
    private String name;
    private int eventId; // Cambio: de String eventName a int eventId
    private List<Integer> equipmentIds; // Cambio: de List<String> a List<Integer>
    private boolean attending;
    private String phone; // Añadido: un atributo que tenías en la vista

    // --- Constantes para JSON ---
    private static final String PHOTOGRAPHERS_FILE = "photographers";
    private static final Type PHOTOGRAPHER_LIST_TYPE = new TypeToken<List<Photographer>>(){}.getType();

    // --- "Base de datos" en memoria ---
    private static List<Photographer> allPhotographers = loadAllFromJson();
    private static int nextPhotographerId = calculateNextId();

    // --- Constructores ---

    /**
     * Constructor vacío, necesario para la deserialización de Gson.
     */
    public Photographer() {
        this.equipmentIds = new ArrayList<>();
    }

    /**
     * Constructor para crear un nuevo fotógrafo.
     */
    public Photographer(int id, String name, String phone, int eventId, List<Integer> equipmentIds, boolean attending) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.eventId = eventId;
        this.equipmentIds = (equipmentIds != null) ? equipmentIds : new ArrayList<>();
        this.attending = attending;
    }

    // --- MÉTODOS CRUD ---

    /**
     * Guarda o actualiza la instancia actual en la lista estática y 
     * persiste la lista completa en el archivo JSON.
     *
     * @return true si se guardó exitosamente.
     */
    public boolean save() {
        // Validación básica
        if (this.name == null || this.name.trim().isEmpty() || this.id == 0) {
            System.out.println("Error: El fotógrafo debe tener un ID y un nombre válidos.");
            return false;
        }

        // Busca si ya existe para reemplazarlo (actualización)
        Photographer existing = findById(this.id);
        if (existing != null) {
            allPhotographers.remove(existing);
        }
        
        allPhotographers.add(this);
        return saveAllToJson();
    }

    /**
     * Elimina un fotógrafo de la lista estática y persiste los cambios.
     * @param id ID del fotógrafo a eliminar.
     * @return true si se eliminó, false si no se encontró.
     */
    public static boolean deletePhotographer(int id) {
        Photographer photographer = findById(id);
        if (photographer == null) {
            return false;
        }

        // Antes de eliminar, libera el equipo que tenía asignado
        photographer.releaseAllEquipment();

        boolean removed = allPhotographers.removeIf(p -> p.getId() == id);
        if (removed) {
            saveAllToJson();
            updateNextId();
        }
        return removed;
    }

    // --- MÉTODOS ESTÁTICOS (Gestión) ---

    /**
     * Recarga el inventario de fotógrafos desde el archivo JSON.
     * Debe llamarse al iniciar la aplicación.
     */
    public static void reloadFromJson() {
        allPhotographers = loadAllFromJson();
        updateNextId();
    }

    /**
     * Devuelve una copia de la lista de todos los fotógrafos.
     * @return Una lista de {@code Photographer}.
     */
    public static List<Photographer> getAllPhotographers() {
        return new ArrayList<>(allPhotographers);
    }

    /**
     * Busca un fotógrafo por su ID.
     * @param id El ID a buscar.
     * @return El objeto Photographer o null si no se encuentra.
     */
    public static Photographer findById(int id) {
        return allPhotographers.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // --- MÉTODOS DE PERSISTENCIA (Privados) ---

    private static List<Photographer> loadAllFromJson() {
        List<Photographer> loadedPhotographers = JsonOperations.loadListFromFile(PHOTOGRAPHERS_FILE, PHOTOGRAPHER_LIST_TYPE);
        return (loadedPhotographers != null) ? loadedPhotographers : new ArrayList<>();
    }

    private static boolean saveAllToJson() {
        return JsonOperations.saveListToFile(allPhotographers, PHOTOGRAPHERS_FILE);
    }

    private static int calculateNextId() {
        if (allPhotographers.isEmpty()) {
            return 1;
        }
        return allPhotographers.stream()
                .mapToInt(Photographer::getId)
                .max()
                .orElse(0) + 1;
    }

    private static void updateNextId() {
        nextPhotographerId = calculateNextId();
    }

    // --- MÉTODOS DE ASIGNACIÓN DE EQUIPO ---

    /**
     * Libera todo el equipo asignado, marcándolo como "Disponible".
     */
    public void releaseAllEquipment() {
        if (this.equipmentIds == null) return;
        
        for (int equipmentId : this.equipmentIds) {
            Equipment item = Equipment.findById(equipmentId);
            if (item != null && !item.isAvailable()) {
                item.markAsAvailable(); // Esto ya incluye item.save()
            }
        }
        this.equipmentIds.clear();
        this.save(); // Guarda el fotógrafo con la lista de IDs vacía
    }

    // --- GETTERS Y SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isAttending() { return attending; }
    public void setAttending(boolean attending) { this.attending = attending; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public List<Integer> getEquipmentIds() { return equipmentIds; }
    public void setEquipmentIds(List<Integer> equipmentIds) { this.equipmentIds = equipmentIds; }

    // --- MÉTODOS DE INFORMACIÓN (para Vistas) ---

    /**
     * Busca el nombre del evento asignado usando el eventId.
     * @return El nombre del evento o "Evento no asignado".
     */
    public String getAssignedEventName() {
        if (this.eventId == 0) {
            return "Ninguno";
        }
        Event event = Customer.findEventById(this.eventId); // Usa el nuevo helper
        return (event != null) ? event.getEventName() : "Evento (ID: " + this.eventId + ") no encontrado";
    }

    /**
     * Obtiene la lista de objetos Equipment asignados.
     * @return Una lista de {@code Equipment}.
     */
    public List<Equipment> getAssignedEquipmentList() {
        if (this.equipmentIds == null) {
            return new ArrayList<>();
        }
        return this.equipmentIds.stream()
                .map(Equipment::findById) // Mapea cada ID a un objeto Equipment
                .filter(item -> item != null) // Filtra nulos si un ID es inválido
                .collect(Collectors.toList());
    }

    // --- MÉTODOS DE PRESENTACIÓN ---

    @Override
    public String toString() {
        String eventName = getAssignedEventName();
        List<Equipment> equipmentList = getAssignedEquipmentList();
        
        String equipmentString;
        if (equipmentList.isEmpty()) {
            equipmentString = "Ninguno registrado";
        } else {
            equipmentString = equipmentList.stream()
                                .map(Equipment::getName)
                                .collect(Collectors.joining(", "));
        }

        return "\n Fotógrafo ID: " + id +
               "\nNombre: " + name +
               "\nTelefono: " + (phone != null ? phone : "N/A") +
               "\nEvento asignado: " + eventName + " (ID: " + eventId + ")" +
               "\nEquipos: " + equipmentString +
               "\nAsistencia confirmada: " + (attending ? "✅ Sí" : "❌ No");
    }

    public String toSimpleString() {
        return "ID: " + id + " | " + name + " | Evento: " + getAssignedEventName() +
               " | Asistencia: " + (attending ? "Sí" : "No");
    }
}