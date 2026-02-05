package ec.edu.espe.crealtivastudios.model;

public class VideoCall {
    private int id;
    private int customerId;
    private String platform; 
    private String link;
    private String date;     
    private String time;     

    public VideoCall(int id, int customerId, String platform, String link, String date, String time) {
        this.id = id;
        this.customerId = customerId;
        this.platform = platform;
        this.link = link;
        this.date = date;
        this.time = time;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    
    @Override
    public String toString() {
        return platform + " - " + date;
    }
}