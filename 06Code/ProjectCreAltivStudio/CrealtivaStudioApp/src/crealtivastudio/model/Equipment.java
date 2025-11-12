package crealtivastudio.model;

import crealtivastudio.json.JsonOperations;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;

/**
 * 
 *
 * @@author Object Master, ESPE
 */
public class Equipment {

    // --- Atributos ---
    private int equipmentId;
    private String name;
    private String description;
    private String status;

    // --- Constantes para Estado ---
    // (Mensajes en español, como solicitaste)
    public static final String STATUS_AVAILABLE = "Disponible";
    public static final String STATUS_IN_USE = "En Uso";
    public static final String STATUS_MAINTENANCE = "Mantenimiento";

    // --- Constantes para JSON ---
    private static final String EQUIPMENT_FILE = "equipment";
    private static final Type EQUIPMENT_LIST_TYPE = new TypeToken<List<Equipment>>(){}.getType();

    // --- "Base de datos" en memoria ---
    private static List<Equipment> allEquipment = loadAllFromJson();
    private static int nextEquipmentId = calculateNextId();

    // --- Constructores ---

    /**
     * Constructor vacío, necesario para la deserialización de Gson.
     */
    public Equipment() {}

    /**
     * Constructor para crear una nueva pieza de equipamiento.
     * El ID se autogenera y el estado inicial es "Disponible".
     *
     * @param name El nombre del equipo (ej. "Canon R5").
     * @param description Una breve descripción (ej. "Cámara mirrorless full-frame").
     */
    public Equipment(String name, String description) {
        this.equipmentId = nextEquipmentId++;
        this.name = name;
        this.description = description;
        this.status = STATUS_AVAILABLE;
    }
    
    // --- Métodos de Instancia (CRUD) ---

    /**
     * Guarda o actualiza la instancia actual en la lista estática y 
     * persiste la lista completa en el archivo JSON.
     *
     * @return true si se guardó exitosamente.
     */
    public boolean save() {
        // Busca si ya existe para reemplazarlo (actualización)
        Equipment existingEquipment = findById(this.equipmentId);
        if (existingEquipment != null) {
            allEquipment.remove(existingEquipment);
        }
        
        // Si es nuevo, asegura que el ID sea único
        if (this.equipmentId == 0) {
             this.equipmentId = nextEquipmentId++;
        }
        
        allEquipment.add(this);
        return saveAllToJson();
    }

    /**
     * Elimina esta pieza de equipamiento de la lista y 
     * persiste los cambios en el archivo JSON.
     *
     * @return true si se eliminó exitosamente.
     */
    public boolean delete() {
        boolean removed = allEquipment.removeIf(item -> item.getEquipmentId() == this.equipmentId);
        if (removed) {
            saveAllToJson();
            updateNextId(); // Recalcula el próximo ID
        }
        return removed;
    }

    // --- Métodos de Estado ---

    /**
     * Marca el equipo como "En Uso" y guarda el cambio.
     */
    public void markAsInUse() {
        this.status = STATUS_IN_USE;
        this.save();
    }

    /**
     * Marca el equipo como "Disponible" y guarda el cambio.
     */
    public void markAsAvailable() {
        this.status = STATUS_AVAILABLE;
        this.save();
    }

    /**
     * Marca el equipo como "Mantenimiento" y guarda el cambio.
     */
    public void markAsMaintenance() {
        this.status = STATUS_MAINTENANCE;
        this.save();
    }

    // --- Métodos Estáticos (Gestión) ---

    /**
     * Recarga el inventario de equipamiento desde el archivo JSON.
     * Debe llamarse al iniciar la aplicación.
     */
    public static void reloadFromJson() {
        allEquipment = loadAllFromJson();
        updateNextId();
    }

    /**
     * Devuelve una copia de la lista de todo el equipamiento.
     * @return Una lista de {@code Equipment}.
     */
    public static List<Equipment> getAllEquipment() {
        return new ArrayList<>(allEquipment);
    }

    /**
     * Busca una pieza de equipamiento por su ID.
     * @param equipmentId El ID a buscar.
     * @return El objeto {@code Equipment} o {@code null} si no se encuentra.
     */
    public static Equipment findById(int equipmentId) {
        return allEquipment.stream()
                .filter(item -> item.getEquipmentId() == equipmentId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca todo el equipamiento que coincida con un estado específico.
     * @param status El estado a buscar (ej. {@code STATUS_AVAILABLE}).
     * @return Una lista de {@code Equipment} que coincida con el estado.
     */
    public static List<Equipment> findByStatus(String status) {
        return allEquipment.stream()
                .filter(item -> item.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    // --- Métodos de Persistencia (Privados) ---

    /**
     * Carga la lista de equipamiento desde el archivo JSON.
     * Utiliza la clase JsonOperations.
     */
    private static List<Equipment> loadAllFromJson() {
        List<Equipment> loadedEquipment = JsonOperations.loadListFromFile(EQUIPMENT_FILE, EQUIPMENT_LIST_TYPE);
        return (loadedEquipment != null) ? loadedEquipment : new ArrayList<>();
    }

    /**
     * Guarda la lista completa de equipamiento en el archivo JSON.
     * Utiliza la clase JsonOperations.
     */
    private static boolean saveAllToJson() {
        return JsonOperations.saveListToFile(allEquipment, EQUIPMENT_FILE);
    }

    /**
     * Calcula el próximo ID disponible basado en el ID más alto de la lista.
     */
    private static int calculateNextId() {
        if (allEquipment.isEmpty()) {
            return 1;
        }
        return allEquipment.stream()
                .mapToInt(Equipment::getEquipmentId)
                .max()
                .orElse(0) + 1;
    }

    /**
     * Actualiza el contador estático del próximo ID.
     */
    private static void updateNextId() {
        nextEquipmentId = calculateNextId();
    }

    // --- Getters y Setters ---
    // (Necesarios para Gson y para las actualizaciones)

    public int getEquipmentId() { return equipmentId; }
    public void setEquipmentId(int equipmentId) { this.equipmentId = equipmentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public boolean isAvailable() {
        return this.status.equals(STATUS_AVAILABLE);
    }

    // --- Métodos de Presentación ---

    /**
     * Devuelve una representación en String simple del objeto.
     */
    public String toSimpleString() {
        return "ID: " + equipmentId + " | " + name + " | Estado: " + status;
    }
    
    @Override
    public String toString() {
        String separator = "--------------------------------------";
        return separator + "\n" +
               "| ID Equipo:\t" + equipmentId + "\n" +
               "| Nombre:\t" + name + "\n" +
               "| Descripcion:\t" + description + "\n" +
               "| Estado:\t" + status + "\n" +
               separator;
    }
}