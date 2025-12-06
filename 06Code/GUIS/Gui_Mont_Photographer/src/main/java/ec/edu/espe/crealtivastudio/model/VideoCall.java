package ec.edu.espe.crealtivastudio.model;

/**
 *
 * @author Daniel
 */
public class VideoCall {
    private int callId;
    private int customerid;
    private int eventId;
    private String costumerName;
    private String videoCallDate;
    private String videoCallHour;
    private String medium;       // Zoom, Meet, Teams
    private String note;
    
    
    @Override
    public String toString() {
        return  """
                Call ID: """ + callId + "\n"
                + "Customer ID: " + customerid + "\n"
                + "Event ID: " + eventId + "\n"
                + "Customer Name: " + costumerName + "\n"
                + "Date: " + videoCallDate + "\n"
                + "Hour: " + videoCallHour + "\n"
                + "Medium: " + medium + "\n"
                + "Note: " + note + "\n";
    }

    
    public VideoCall(){
        
    }
    public VideoCall(int callId, int customerid, int eventId, String costumerName, String videoCallDate, String videoCallHour, String medium, String note) {
        this.callId = callId;
        this.customerid = customerid;
        this.eventId = eventId;
        this.costumerName = costumerName;
        this.videoCallDate = videoCallDate;
        this.videoCallHour = videoCallHour;
        this.medium = medium;
        this.note = note;
    }
    
    

    /**
     * @return the callId
     */
    public int getCallId() {
        return callId;
    }

    /**
     * @param callId the callId to set
     */
    public void setCallId(int callId) {
        this.callId = callId;
    }

    /**
     * @return the customerid
     */
    public int getCustomerid() {
        return customerid;
    }

    /**
     * @param customerid the customerid to set
     */
    public void setCustomerid(int customerid) {
        this.customerid = customerid;
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
     * @return the costumerName
     */
    public String getCostumerName() {
        return costumerName;
    }

    /**
     * @param costumerName the costumerName to set
     */
    public void setCostumerName(String costumerName) {
        this.costumerName = costumerName;
    }

    /**
     * @return the videoCallDate
     */
    public String getVideoCallDate() {
        return videoCallDate;
    }

    /**
     * @param videoCallDate the videoCallDate to set
     */
    public void setVideoCallDate(String videoCallDate) {
        this.videoCallDate = videoCallDate;
    }

    /**
     * @return the videoCallHour
     */
    public String getVideoCallHour() {
        return videoCallHour;
    }

    /**
     * @param videoCallHour the videoCallHour to set
     */
    public void setVideoCallHour(String videoCallHour) {
        this.videoCallHour = videoCallHour;
    }

    /**
     * @return the medium
     */
    public String getMedium() {
        return medium;
    }

    /**
     * @param medium the medium to set
     */
    public void setMedium(String medium) {
        this.medium = medium;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }
    
 
    
    
}
