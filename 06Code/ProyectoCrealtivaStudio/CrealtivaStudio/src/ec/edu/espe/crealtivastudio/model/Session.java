



package ec.edu.espe.crealtivastudio.model;
/**
 *
 * @author Daniel
 */

public class Session {
    private int sessionId;
    private int customerId; // <--- ESTE ES EL ENLACE CLAVE
    private String sessionName;
    private String sessionDate;
    private String sessionDetails; // Ej. Tipo de sesión, duración, etc.

    // Constructor
    public Session(int sessionId, int customerId, String sessionName, String sessionDate, String sessionDetails) {
        this.sessionId = sessionId;
        this.customerId = customerId;
        this.sessionName = sessionName;
        this.sessionDate = sessionDate;
        this.sessionDetails = sessionDetails;
    }
    
    // Getters y Setters (Necesarios para la persistencia JSON)
    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getSessionName() { return sessionName; }
    public void setSessionName(String sessionName) { this.sessionName = sessionName; }

    public String getSessionDate() { return sessionDate; }
    public void setSessionDate(String sessionDate) { this.sessionDate = sessionDate; }

    public String getSessionDetails() { return sessionDetails; }
    public void setSessionDetails(String sessionDetails) { this.sessionDetails = sessionDetails; }

    @Override
    public String toString() {
        String separator = "--------------------------------------";
        return separator + "\n" +
               "| SESION ID:\t" + this.sessionId + "\n" +
               "| Cliente ID:\t" + this.customerId + "\n" +
               "| Nombre:\t" + this.sessionName + "\n" +
               "| Fecha:\t" + this.sessionDate + "\n" +
               "| Detalles:\t" + this.sessionDetails + "\n" +
               separator;
    }
}
