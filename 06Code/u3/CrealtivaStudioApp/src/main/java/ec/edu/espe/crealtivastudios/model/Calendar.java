package ec.edu.espe.crealtivastudios.model;

public class Calendar {
    private int id;
    private int eventId; // Relación con el Evento
    private String description;
    private String date; // YYYY-MM-DD

    public Calendar(int id, int eventId, String description, String date) {
        this.id = id;
        this.eventId = eventId;
        this.description = description;
        this.date = date;
    }

    // --- Getters y Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    @Override
    public String toString() {
        return description + " (" + date + ")";
    }
}