package ec.edu.espe.crealtivastudio.model;

/**
 *
 * @author Mateo Cevallos
 */
public class Bill {
    
    private int billId;
    private int customerId;    // Enlace al cliente
    private int eventId;       // Enlace al evento
    private boolean isPaid;
    private String notes;
    
    // Métodos de confirmación de pago
    public void confirmPayment() {
        this.isPaid = true;
    }

    public void confirmPayment(String paymentDate) {
        this.isPaid = true;
    }

    public void markAsPending() {
        this.isPaid = false;
    }

    // Métodos de verificación
    public boolean isPaid() {
        return isPaid;
    }

    public boolean isPending() {
        return !isPaid;
    }

    public Bill(int billId, int customerId, int eventId, boolean isPaid, String paymentDate, String notes) {
        this.billId = billId;
        this.customerId = customerId;
        this.eventId = eventId;
        this.isPaid = isPaid;
        this.notes = notes;
    }
    

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public boolean isIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        String separator = "--------------------------------------";
        String status = this.isPaid ? "PAGADO" : "PENDIENTE";
        
        return separator + "\n" +
               "| FACTURA ID:\t" + this.billId + "\n" +
               "| Cliente ID:\t" + this.customerId + "\n" +
               "| Evento ID:\t" + this.eventId + "\n" +
               "| Estado:\t" + status + "\n" +
               "| Notas:\t" + (this.notes.isEmpty() ? "Ninguna" : this.notes) + "\n" +
               separator;
    }
    
    
}
