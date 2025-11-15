package crealtivastudio.model;
import crealtivastudio.json.JsonOperations;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
/**
 * Class
 *
 * @author Object Masters, OOP, ESPE
 */

public class Event {
    private int eventId;
    private String eventName;
    private String eventDate;
    private String eventType;
    private int eventTypeCode;
    private double basePrice;
    private int customerId; 
 
    private List<String> reminders; 
    
    private static final String EVENTS_FILE = "events"; 
    private static final Type EVENT_LIST_TYPE = new TypeToken<List<Event>>(){}.getType();
    
    private static List<Event> allEvents = loadAllFromJson();
    
    private static List<Event> loadAllFromJson() {
        List<Event> loadedEvents = JsonOperations.loadListFromFile(EVENTS_FILE, EVENT_LIST_TYPE);
        return (loadedEvents != null) ? loadedEvents : new ArrayList<>();
    }
    
    public static List<Event> getAllEvents() {
        return new ArrayList<>(allEvents);
    }
    
    public static void reloadFromJson() {
        allEvents = loadAllFromJson();
    }

    public static boolean saveAllToJson() {
        return JsonOperations.saveListToFile(allEvents, EVENTS_FILE);
    }
    
    public static final int BODAS = 1;
    public static final int CUMPLEANOS = 2;
    public static final int BAUTIZOS = 3;
    public static final int GRADUACIONES = 4;

    public Event() {
        this.reminders = new ArrayList<>(); 
    }

    public Event(int eventId, String eventName, String eventDate, int eventTypeCode, int customerId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTypeCode = eventTypeCode;
        this.customerId = customerId;
        this.eventType = getEventTypeString(eventTypeCode);
        this.basePrice = calculateBasePrice();
        this.reminders = new ArrayList<>(); 
    }

    public Event(int eventId, String eventName, String eventDate, int eventTypeCode) {
        this(eventId, eventName, eventDate, eventTypeCode, 0); 
    }

    private double calculateBasePrice() {
        switch (this.eventTypeCode) {
            case BODAS: return 150.00;
            case CUMPLEANOS: return 80.00;
            case BAUTIZOS: return 20.00;
            case GRADUACIONES: return 60.00;
            default: return 50.00;
        }
    }

    public double calculateFinalPrice() {
        double finalPrice = this.basePrice;

        if (this.eventTypeCode == BODAS) {
            finalPrice *= 0.9; 
        }
        
        if (this.eventTypeCode == BAUTIZOS) {
            finalPrice *= 0.95;
        }
        
        return finalPrice;
    }
    
    public static String getEventTypeString(int eventTypeCode) {
        switch (eventTypeCode) {
            case BODAS: return "Bodas";
            case CUMPLEANOS: return "Cumpleaños";
            case BAUTIZOS: return "Bautizos";
            case GRADUACIONES: return "Graduaciones";
            default: return "Otro";
        }
    }
    
    public boolean isValidEvent() {
        return this.eventName != null && !this.eventName.trim().isEmpty() &&
                this.eventDate != null && !this.eventDate.trim().isEmpty() &&
                this.eventTypeCode >= 1 && this.eventTypeCode <= 4;
    }

    // Getters y Setters 
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public int getEventTypeCode() { return eventTypeCode; }
    public void setEventTypeCode(int eventTypeCode) { 
        this.eventTypeCode = eventTypeCode;
        this.eventType = getEventTypeString(eventTypeCode);
        this.basePrice = calculateBasePrice();
    }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public void addReminder(String reminder) {
        if (this.reminders == null) {
            this.reminders = new ArrayList<>();
        }
        this.reminders.add(reminder);
    }
    

    public List<String> getReminders() {
        if (this.reminders == null) {
            this.reminders = new ArrayList<>();
        }
        
        List<String> currentReminders = new ArrayList<>(this.reminders);

        String automaticAppt = scheduleAutomaticAppointment();
        if (!currentReminders.contains(automaticAppt) && automaticAppt.startsWith("listo")) {
            currentReminders.add(0, automaticAppt); 
        }
        
        return currentReminders;
    }

    
    public String scheduleAutomaticAppointment() {
    try {
        LocalDate eventDate = LocalDate.parse(this.eventDate);
        LocalDate appointmentDate = eventDate.minusDays(7); 
        return "  Cita automatica programada para el " + 
               appointmentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
               " (1 semana antes del evento \"" + this.eventName + "\")";
    } catch (DateTimeParseException e) {
        return " Error: formato de fecha invalido para el evento " + this.eventName;
    }
}

public boolean isUpcoming() {
    try {
        LocalDate eventDate = LocalDate.parse(this.eventDate);
        LocalDate today = LocalDate.now();
        
        return !eventDate.isBefore(today) && (eventDate.isBefore(today.plusDays(3)) || eventDate.isEqual(today.plusDays(3)));
    } catch (Exception e) {
        return false;
    }
}

    public boolean save() {

        Event existing = allEvents.stream()
            .filter(e -> e.getEventId() == this.eventId)
            .findFirst().orElse(null);
            
        if (existing != null) {
            allEvents.remove(existing);
        } else if (this.eventId == 0) {

        }
        
        allEvents.add(this);
        return saveAllToJson(); 
    }
    
}