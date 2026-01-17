package ec.edu.espe.crealtivastudios.model;

public class Bill {
    private int billId;
    private int customerId;
    private int eventId;
    private double amount;
    private String notes;
    private boolean isPaid;

    public Bill(int billId, int customerId, int eventId, double amount, String notes, boolean isPaid) {
        this.billId = billId;
        this.customerId = customerId;
        this.eventId = eventId;
        this.amount = amount;
        this.notes = notes;
        this.isPaid = isPaid;
    }

    // --- Getters y Setters ---
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
    
    // Método auxiliar para mostrar estado en texto
    public String getStatusString() {
        return isPaid ? "PAGADO" : "PENDIENTE";
    }
}