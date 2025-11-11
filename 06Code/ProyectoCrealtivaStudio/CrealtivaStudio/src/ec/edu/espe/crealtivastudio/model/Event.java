



package ec.edu.espe.crealtivastudio.model;
/**
 *
 * @author Daniel
 */
public class Event {
    private int eventId;
    private int customerId; // Enlace al cliente
    private String eventName;
    private String eventDate;
    private String eventDetails;

    public Event(int eventId, int customerId, String eventName, String eventDate, String eventDetails) {
        this.eventId = eventId;
        this.customerId = customerId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventDetails = eventDetails;
    }
    
    // Getters y Setters
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }
    public String getEventDetails() { return eventDetails; }
    public void setEventDetails(String eventDetails) { this.eventDetails = eventDetails; }

    @Override
    public String toString() {
        String separator = "--------------------------------------";
        return separator + "\n" +
               "| EVENTO ID:\t" + this.eventId + "\n" +
               "| Cliente ID:\t" + this.customerId + "\n" +
               "| Nombre:\t" + this.eventName + "\n" +
               "| Fecha:\t" + this.eventDate + "\n" +
               "| Detalles:\t" + this.eventDetails + "\n" +
               separator;
    }
}