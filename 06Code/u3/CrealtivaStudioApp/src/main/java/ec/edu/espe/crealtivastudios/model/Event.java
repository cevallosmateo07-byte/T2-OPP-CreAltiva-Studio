package ec.edu.espe.crealtivastudios.model;

/**
 * Entidad Event.
 * Responsabilidad: Únicamente contener los datos del evento.
 */
public class Event {
    
    private int eventId;
    private String eventName;
    private String eventDate; // Formato YYYY-MM-DD
    private int eventTypeCode; // 1:Bodas, 2:Cumpleaños, etc.
    private int customerId;    // FK: ID del cliente dueño del evento

    public Event(int eventId, String eventName, String eventDate, int eventTypeCode, int customerId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTypeCode = eventTypeCode;
        this.customerId = customerId;
    }

    // --- Getters y Setters (Encapsulamiento) ---

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public int getEventTypeCode() { return eventTypeCode; }
    public void setEventTypeCode(int eventTypeCode) { this.eventTypeCode = eventTypeCode; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    @Override
    public String toString() {
        return eventName;
    }
}