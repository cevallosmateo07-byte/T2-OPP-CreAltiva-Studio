package ec.edu.espe.crealtivastudios.model;

public class Photographer {
    private int id;
    private String name;
    private String phone;
    private String specialty;
    private boolean isAssigned;
    private String assignedEvent;
    private boolean attending; 

    public Photographer(int id, String name, String phone, String specialty, boolean isAssigned) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.specialty = specialty;
        this.isAssigned = isAssigned;
        this.assignedEvent = "";
        this.attending = false;
    }

  
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public boolean isAssigned() { return isAssigned; }
    public void setAssigned(boolean isAssigned) { this.isAssigned = isAssigned; }
    public String getAssignedEvent() { return assignedEvent; }
    public void setAssignedEvent(String assignedEvent) { this.assignedEvent = assignedEvent; }
    
   
    public boolean isAttending() { return attending; }
    public void setAttending(boolean attending) { this.attending = attending; }
}