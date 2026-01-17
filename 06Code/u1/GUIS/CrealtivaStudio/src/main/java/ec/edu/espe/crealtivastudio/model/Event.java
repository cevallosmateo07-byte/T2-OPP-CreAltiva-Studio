package ec.edu.espe.crealtivastudio.model;

/**
 *
 * @author Mateo Cevallos
 */
public class Event {
    private int eventId;
    private String eventName;
    private String eventDate;
    private String eventType;
    private int eventTypeCode;
    private double basePrice;
    private int customerId;

    public Event(int eventId, String eventName, String eventDate, String eventType, int eventTypeCode, double basePrice, int customerId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.eventTypeCode = eventTypeCode;
        this.basePrice = basePrice;
        this.customerId = customerId;
    }
        
    /**
     * @return the eventId
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * @param eventId the eventId to set
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * @return the eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @param eventName the eventName to set
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * @return the eventDate
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * @param eventDate the eventDate to set
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * @return the eventType
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * @param eventType the eventType to set
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * @return the eventTypeCode
     */
    public int getEventTypeCode() {
        return eventTypeCode;
    }

    /**
     * @param eventTypeCode the eventTypeCode to set
     */
    public void setEventTypeCode(int eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    /**
     * @return the basePrice
     */
    public double getBasePrice() {
        return basePrice;
    }

    /**
     * @param basePrice the basePrice to set
     */
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    
}
