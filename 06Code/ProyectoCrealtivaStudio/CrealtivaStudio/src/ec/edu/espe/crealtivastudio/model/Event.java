package ec.edu.espe.crealtivastudio.model;

/**
 *
 * @author Daniel
 */
public class Event {
    private int eventId;
    private String eventName;
    private String eventDate;
    private String eventType;
    private int eventTypeCode;
    private double basePrice;

    // Constantes para los tipos de evento
    public static final int BODAS = 1;
    public static final int CUMPLEANOS = 2;
    public static final int BAUTIZOS = 3;
    public static final int GRADUACIONES = 4;

    // Constructor vacío necesario para Gson
    public Event() {}

    public Event(int eventId, String eventName, String eventDate, int eventTypeCode) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTypeCode = eventTypeCode;
        this.eventType = getEventTypeString(eventTypeCode);
        this.basePrice = calculateBasePrice();
    }

    // Método para calcular precio base según tipo de evento
    private double calculateBasePrice() {
        switch (this.eventTypeCode) {
            case BODAS: return 1500.00;
            case CUMPLEANOS: return 800.00;
            case BAUTIZOS: return 2000.00;
            case GRADUACIONES: return 600.00;
            default: return 500.00;
        }
    }
    
    // Método para aplicar descuentos
    public double calculateFinalPrice() {
        double finalPrice = this.basePrice;
        
        // Descuento para bodas
        if (this.eventTypeCode == BODAS) {
            finalPrice *= 0.9; // 10% de descuento
        }
        
        // Descuento para bautizos
        if (this.eventTypeCode == BAUTIZOS) {
            finalPrice *= 0.95; // 5% de descuento
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

    // Getters y Setters (necesarios para Gson)
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
    
}