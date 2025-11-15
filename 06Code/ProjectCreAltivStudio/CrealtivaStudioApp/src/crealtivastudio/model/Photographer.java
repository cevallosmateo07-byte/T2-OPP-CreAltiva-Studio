package crealtivastudio.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Class
 *
 * @author Object Masters, OOP, ESPE
 */
public class Photographer {
    
  
    private static final List<Photographer> photographers = new ArrayList<>();

    private final int id;
    private final String name;
    private final String assignedEvent;
    private final List<String> equipment;
    private boolean attending;

    public Photographer(int id, String name, String assignedEvent, String equipmentString, boolean attending) {
        this.id = id;
        this.name = name;
        this.assignedEvent = assignedEvent;
        this.equipment = new ArrayList<>();
        this.attending = attending;
        
        if (equipmentString != null && !equipmentString.trim().isEmpty()) {
            this.equipment.addAll(
                Arrays.stream(equipmentString.split(","))
                      .map(String::trim)
                      .filter(s -> !s.isEmpty())
                      .collect(Collectors.toList())
            );
        }
        
     
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAssignedEvent() { return assignedEvent; }
    public List<String> getEquipment() { return equipment; }
    public boolean isAttending() { return attending; }

    public void setAttending(boolean attending) {
        this.attending = attending;
    }
    public void addEquipment(String item) {
        if (item != null && !item.trim().isEmpty()) {
            equipment.add(item.trim());
        }
    }

    public void removeEquipment(String item) {
        equipment.remove(item);
    }

    public void confirmAttendance(boolean attending) {
        this.attending = attending;
    }
 public boolean save() {
        if (findById(this.id) == null) {
            photographers.add(this);
            return true;
        }
        return false;
    }

    public static List<Photographer> getAllPhotographers() {
        return photographers;
    }
    public static Photographer findById(int id) {
        for (Photographer p : photographers) {
            if (p.getId() == id) return p;
        }
        return null;
    }
    public static boolean deletePhotographer(int id) {
         return photographers.removeIf(p -> p.getId() == id);
    }
    public static void reloadFromJson() {
    }
    @Override
    public String toString() {
        return "\n Fotógrafo ID: " + id +
               "\nNombre: " + name +
               "\nEvento asignado: " + assignedEvent +
               "\nEquipos: " + (equipment.isEmpty() ? "Ninguno registrado" : equipment.toString().replace("[", "").replace("]", "")) +
               "\nAsistencia confirmada: " + (attending ? "✅ Sí" : "❌ No");
    }

    public String toSimpleString() {
        return "ID: " + id + " | " + name + " | Evento: " + assignedEvent +
               " | Asistencia: " + (attending ? "Sí" : "No");
    }
}