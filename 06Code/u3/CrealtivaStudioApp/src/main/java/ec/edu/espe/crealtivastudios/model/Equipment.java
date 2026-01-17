package ec.edu.espe.crealtivastudios.model;

public class Equipment {
    private int id;
    private String name;
    private String description;
    private String status; // "Disponible", "En Uso", "Mantenimiento"

    public Equipment(int id, String name, String description, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return name + " [" + status + "]";
    }
}