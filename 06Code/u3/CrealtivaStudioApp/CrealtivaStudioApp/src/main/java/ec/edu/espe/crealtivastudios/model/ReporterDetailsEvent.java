package ec.edu.espe.crealtivastudios.model;

/**
 *
 * @author Kevin Chalan, OBJECT MASTER, OOP
 */
public class ReporterDetailsEvent {
    private String userName;
    private String eventName;
    private String organizationDate;
    private boolean isCanceled;

    public ReporterDetailsEvent(String userName, String eventName, String organizationDate, boolean isCanceled) {
        this.userName = userName;
        this.eventName = eventName;
        this.organizationDate = organizationDate;
        this.isCanceled = isCanceled;
    }

    public String getUserName() { return userName; }
    public String getEventName() { return eventName; }
    public String getOrganizationDate() { return organizationDate; }
    public boolean isCanceled() { return isCanceled; }
}