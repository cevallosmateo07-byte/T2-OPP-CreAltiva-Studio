package crealtivastudio.model;

import crealtivastudio.json.JsonOperations;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;

/**
 * Class
 *
 * @author Object Masters, OOP, ESPE
 */
public class Equipment {

    private int equipmentId;
    private String name;
    private String description;
    private String status;

    public static final String STATUS_AVAILABLE = "Disponible";
    public static final String STATUS_IN_USE = "En Uso";
    public static final String STATUS_MAINTENANCE = "Mantenimiento";

    private static final String EQUIPMENT_FILE = "equipment";
    private static final Type EQUIPMENT_LIST_TYPE = new TypeToken<List<Equipment>>(){}.getType();

    private static List<Equipment> allEquipment = loadAllFromJson();
    private static int nextEquipmentId = calculateNextId();

    // --- Constructores ---
    public Equipment() {}

    public Equipment(String name, String description) {
        this.equipmentId = nextEquipmentId++;
        this.name = name;
        this.description = description;
        this.status = STATUS_AVAILABLE;
    }
    public boolean save() {
       
        Equipment existingEquipment = findById(this.equipmentId);
        if (existingEquipment != null) {
            allEquipment.remove(existingEquipment);
        }
        
        if (this.equipmentId == 0) {
             this.equipmentId = nextEquipmentId++;
        }
        
        allEquipment.add(this);
        return saveAllToJson();
    }

    public boolean delete() {
        boolean removed = allEquipment.removeIf(item -> item.getEquipmentId() == this.equipmentId);
        if (removed) {
            saveAllToJson();
            updateNextId(); 
        }
        return removed;
    }

    public void markAsInUse() {
        this.status = STATUS_IN_USE;
        this.save();
    }

    public void markAsAvailable() {
        this.status = STATUS_AVAILABLE;
        this.save();
    }

    public void markAsMaintenance() {
        this.status = STATUS_MAINTENANCE;
        this.save();
    }

    public static void reloadFromJson() {
        allEquipment = loadAllFromJson();
        updateNextId();
    }

    public static List<Equipment> getAllEquipment() {
        return new ArrayList<>(allEquipment);
    }

    public static Equipment findById(int equipmentId) {
        return allEquipment.stream()
                .filter(item -> item.getEquipmentId() == equipmentId)
                .findFirst()
                .orElse(null);
    }

    public static List<Equipment> findByStatus(String status) {
        return allEquipment.stream()
                .filter(item -> item.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    private static List<Equipment> loadAllFromJson() {
        List<Equipment> loadedEquipment = JsonOperations.loadListFromFile(EQUIPMENT_FILE, EQUIPMENT_LIST_TYPE);
        return (loadedEquipment != null) ? loadedEquipment : new ArrayList<>();
    }

    private static boolean saveAllToJson() {
        return JsonOperations.saveListToFile(allEquipment, EQUIPMENT_FILE);
    }

    private static int calculateNextId() {
        if (allEquipment.isEmpty()) {
            return 1;
        }
        return allEquipment.stream()
                .mapToInt(Equipment::getEquipmentId)
                .max()
                .orElse(0) + 1;
    }

    private static void updateNextId() {
        nextEquipmentId = calculateNextId();
    }

  
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