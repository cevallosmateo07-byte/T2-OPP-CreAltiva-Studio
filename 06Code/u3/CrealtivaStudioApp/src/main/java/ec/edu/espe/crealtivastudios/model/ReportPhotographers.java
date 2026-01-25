package ec.edu.espe.crealtivastudios.model;

/**
 *
 * @author Kevin Chalan, OBJECT MASTER, OOP
 */
public class ReportPhotographers {
    private String eventName;
    private String photographerName;
    private String equipment;
    private String contractorName;

    public ReportPhotographers(String eventName, String photographerName, String equipment, String contractorName) {
        this.eventName = eventName;
        this.photographerName = photographerName;
        this.equipment = equipment;
        this.contractorName = contractorName;
    }

    public String getEventName() { return eventName; }
    public String getPhotographerName() { return photographerName; }
    public String getEquipment() { return equipment; }
    public String getContractorName() { return contractorName; }
}