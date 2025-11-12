package crealtivastudio.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pablo
 */
public class Photographer {
    
  
    private static final List<Photographer> photographers = new ArrayList<>();

    private final int id;
    private final String name;
    private final String assignedEvent;
    private final List<String> equipment;
    private boolean attending;

    // Constructor corregido para aceptar el String de equipos y la bandera de asistencia
    public Photographer(int id, String name, String assignedEvent, String equipmentString, boolean attending) {
        this.id = id;
        this.name = name;
        this.assignedEvent = assignedEvent;
        this.equipment = new ArrayList<>();
        this.attending = attending;
        
        // Convertir la cadena de equipos (separados por comas) en una lista
        if (equipmentString != null && !equipmentString.trim().isEmpty()) {
            this.equipment.addAll(
                Arrays.stream(equipmentString.split(","))
                      .map(String::trim)
                      .filter(s -> !s.isEmpty())
                      .collect(Collectors.toList())
            );
        }
        
     
    }

    // ==================== Getters y Setters ====================
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAssignedEvent() { return assignedEvent; }
    public List<String> getEquipment() { return equipment; }
    public boolean isAttending() { return attending; }

    public void setAttending(boolean attending) {
        this.attending = attending;
    }

    // ==================== Métodos principales ====================
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

    /**
    
     * @return true si se guardó (es nuevo), false si ya existe un ID.
     */
    public boolean save() {
        // Lógica de guardado: verifica si el ID ya existe (asumiendo que los objetos son inmutables)
        if (findById(this.id) == null) {
            photographers.add(this);
            return true;
        }
        // Si ya existe, no lo guarda. Para actualizar, se necesitaría un método diferente.
        return false;
    }

    /**
     * Devuelve la lista completa de fotógrafos.
     * @return Lista de Photographer.
     */
    public static List<Photographer> getAllPhotographers() {
        // En una aplicación real, aquí se leerían los datos desde un archivo (ej. JSON)
        return photographers;
    }

    /**
     * Busca un fotógrafo por su ID.
     * @param id El ID a buscar.
     * @return El objeto Photographer o null si no se encuentra.
     */
    public static Photographer findById(int id) {
        for (Photographer p : photographers) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    /**
     * Implementación corregida del método de eliminación.
     * Elimina el fotógrafo de la lista estática.
     * @param id ID del fotógrafo a eliminar.
     * @return true si se eliminó, false si no se encontró.
     */
    public static boolean deletePhotographer(int id) {
         // Uso de removeIf que es eficiente y seguro para listas
         return photographers.removeIf(p -> p.getId() == id);
    }
    
    // Método para recargar datos (simulación de carga de JSON)
    // Se deja vacío ya que la carga real desde archivo no está implementada.
    public static void reloadFromJson() {
        // Aquí iría la lógica para leer y cargar desde un archivo JSON al inicio de la app.
    }

    // ==================== Mostrar información ====================
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