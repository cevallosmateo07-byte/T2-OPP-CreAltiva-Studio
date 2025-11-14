
package crealtivastudio.model;




import crealtivastudio.json.JsonOperations;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
/**
 * Modelo para gestionar las Videollamadas programadas con persistencia independiente.
 * @author Daniel
 */

public class VideoCall {

    // --- Atributos ---
    private int callId;
    private int customerId;
    private int eventId;
    private String videoCallDate;
    private String medium;

    // --- Constantes para JSON: Archivo INDEPENDIENTE ---
    private static final String VIDEOCALLS_FILE = "videocalls"; // Esto crea videocalls.json
    private static final Type VIDEOCALL_LIST_TYPE = new TypeToken<List<VideoCall>>(){}.getType();
    
    private static List<VideoCall> allVideoCalls = loadAllFromJson();
    private static int nextCallId = calculateNextId();

    // --- Constructor vacío y Constructor Principal ---
    public VideoCall() {}

    public VideoCall(int customerId, int eventId, String videoCallDate, String medium) {
        this.callId = nextCallId++;
        this.customerId = customerId;
        this.eventId = eventId;
        this.videoCallDate = videoCallDate;
        this.medium = medium;
    }

    // --- MÉTODOS CRUD con Validación ---

    public boolean save() {
        Customer customer = Customer.findCustomerById(this.customerId);
        if (customer == null) {
             System.out.println("Error: Cliente referenciado no existe.");
             return false;
        }
        Event event = customer.getEventById(this.eventId);
        if (event == null) {
            System.out.println("Error: Evento referenciado (ID: " + this.eventId + ") no existe o no pertenece al cliente.");
            return false;
        }

        // Validación de Fechas Clave (Debe ser ANTES del evento)
        try {
            LocalDate callDateLD = LocalDate.parse(this.videoCallDate);
            LocalDate eventDateLD = LocalDate.parse(event.getEventDate());
            
            if (!callDateLD.isBefore(eventDateLD)) {
                System.out.println("⚠️ Error de Validación: La videollamada debe ser antes de la fecha del evento (" + event.getEventDate() + ").");
                return false;
            }

        } catch (DateTimeParseException e) {
            System.out.println("⚠️ Error de formato: La fecha de la videollamada o del evento es inválida (debe ser yyyy-MM-dd).");
            return false;
        }
        
        // Lógica de Guardado
        VideoCall existing = findById(this.callId);
        if (existing != null) {
            allVideoCalls.remove(existing);
        } else if (this.callId == 0) {
            this.callId = nextCallId++; 
        }
        
        allVideoCalls.add(this);
        // Persistencia garantizada en 'videocalls.json'
        return saveAllToJson(); 
    }

    public boolean delete() {
        boolean removed = allVideoCalls.removeIf(c -> c.getCallId() == this.callId);
        if (removed) {
            saveAllToJson();
            updateNextId();
        }
        return removed;
    }

    // --- MÉTODOS ESTÁTICOS Y DE PERSISTENCIA ---

    private static List<VideoCall> loadAllFromJson() {
        // Carga desde 'videocalls.json'
        List<VideoCall> loadedCalls = JsonOperations.loadListFromFile(VIDEOCALLS_FILE, VIDEOCALL_LIST_TYPE);
        return (loadedCalls != null) ? loadedCalls : new ArrayList<>();
    }

    private static boolean saveAllToJson() {
        // Guarda en 'videocalls.json'
        return JsonOperations.saveListToFile(allVideoCalls, VIDEOCALLS_FILE);
    }
    
    // El resto de métodos estáticos y Getters/Setters siguen siendo los mismos.
    // ... (getAllVideoCalls, findById, findByCustomer, reloadFromJson, calculateNextId, updateNextId, Getters/Setters)
    
    public static void reloadFromJson() {
        allVideoCalls = loadAllFromJson();
        updateNextId();
    }
    
    public static List<VideoCall> getAllVideoCalls() {
        return new ArrayList<>(allVideoCalls);
    }

    public static VideoCall findById(int callId) {
        return allVideoCalls.stream()
                .filter(c -> c.getCallId() == callId)
                .findFirst()
                .orElse(null);
    }

    public static List<VideoCall> findByCustomer(int customerId) {
        return allVideoCalls.stream()
                .filter(c -> c.getCustomerId() == customerId)
                .toList();
    }

    private static int calculateNextId() {
        if (allVideoCalls.isEmpty()) {
            return 1;
        }
        return allVideoCalls.stream()
                .mapToInt(VideoCall::getCallId)
                .max()
                .orElse(0) + 1;
    }

    private static void updateNextId() {
        nextCallId = calculateNextId();
    }
    
    public String getCallInfo() {
        Customer customer = Customer.findCustomerById(customerId);
        String customerName = (customer != null) ? customer.getName() : "Cliente No Encontrado";
        
        return String.format("ID: %d | Cliente: %s | Evento ID: %d | Medio: %s | Fecha: %s",
                callId, customerName, eventId, medium, videoCallDate);
    }

    
@Override
public String toString() {
    Customer customer = Customer.findCustomerById(customerId);
    String customerName = (customer != null) ? customer.getName() : "Cliente No Encontrado";
    
    return String.format(
        "Videollamada ID: %d | Cliente: %s (ID: %d) | Evento ID: %d | Medio: %s | Fecha: %s",
        callId, customerName, customerId, eventId, medium, videoCallDate
    );
}
    
    

    public int getCallId() { return callId; }
    public void setCallId(int callId) { this.callId = callId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getVideoCallDate() { return videoCallDate; }
    public void setVideoCallDate(String videoCallDate) { this.videoCallDate = videoCallDate; }

    public String getMedium() { return medium; }
    public void setMedium(String medium) { this.medium = medium; }

    public String getDate() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setDate(String newDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}