package crealtivastudio.model;

import crealtivastudio.json.JsonOperations;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;

/**
 *
 * @author Mateo Cevallos
 */
public class Bill {
    private int billId;
    private int customerId;
    private int eventId;    
    private boolean isPaid;
    private String notes;
    private double amount;
    
    // Constantes para archivos JSON
    private static final String BILLS_FILE = "bills";
    private static final Type BILL_LIST_TYPE = new TypeToken<List<Bill>>(){}.getType();
    
    // Lista estática para simular base de datos
    private static List<Bill> allBills = loadAllFromJson();
    private static int nextBillId = calculateNextBillId();

    // Constructor vacío necesario para Gson
    public Bill() {}

    public Bill(int customerId, int eventId, String notes) {
        this.billId = nextBillId++;
        this.customerId = customerId;
        this.eventId = eventId;
        this.isPaid = false;
        this.notes = notes;
        this.amount = calculateAmount();
    }
    
    public Bill(int billId, int customerId, int eventId, boolean isPaid, String notes, double amount) {
        this.billId = billId;
        this.customerId = customerId;
        this.eventId = eventId;
        this.isPaid = isPaid;
        this.notes = notes;
        this.amount = amount;
        updateNextBillId();
    }

    // --- MÉTODOS DE PAGO ---
    
    public void confirmPayment() {
        this.isPaid = true;
        this.save();
    }

    public void markAsPending() {
        this.isPaid = false;
        this.save();
    }
  
    public boolean isPaid() {
        return isPaid;
    }

    public boolean isPending() {
        return !isPaid;
    }
    
    // Método para calcular el monto basado en el evento
    private double calculateAmount() {
        Customer customer = Customer.findCustomerById(this.customerId);
        if (customer == null) return 0.0;
        
        Event event = customer.getEventById(this.eventId);
        if (event == null) return 0.0;
        
        return event.calculateFinalPrice();
    }
    
    // --- MÉTODOS CRUD ---
    
    public boolean save() {
        // Verificar que el customer y event existan
        Customer customer = Customer.findCustomerById(this.customerId);
        if (customer == null || customer.getEventById(this.eventId) == null) {
            System.out.println("Error: Cliente o evento no encontrado");
            return false;
        }
        
        // Verificar si ya existe (para update)
        Bill existing = findBillById(this.billId);
        if (existing != null) {
            allBills.remove(existing);
        } else {
            // Si es nuevo, asegurar que el ID sea único
            if (this.billId == 0) {
                this.billId = nextBillId++;
            }
        }
        
        // Recalcular amount si es necesario
        if (this.amount == 0) {
            this.amount = calculateAmount();
        }
        
        allBills.add(this);
        return saveAllToJson();
    }
    
    public boolean delete() {
        boolean removed = allBills.removeIf(b -> b.getBillId() == this.billId);
        if (removed) {
            saveAllToJson();
            updateNextBillId();
        }
        return removed;
    }
    
    // --- MÉTODOS ESTÁTICOS ---
    
    public static List<Bill> getAllBills() {
        return new ArrayList<>(allBills);
    }
    
    public static Bill findBillById(int billId) {
        return allBills.stream()
                .filter(b -> b.getBillId() == billId)
                .findFirst()
                .orElse(null);
    }
    
    public static List<Bill> findBillsByCustomer(int customerId) {
        return allBills.stream()
                .filter(b -> b.getCustomerId() == customerId)
                .toList();
    }
    
    public static List<Bill> findPendingBills() {
        return allBills.stream()
                .filter(b -> !b.isPaid)
                .toList();
    }
    
    public static List<Bill> findPaidBills() {
        return allBills.stream()
                .filter(b -> b.isPaid)
                .toList();
    }
    
    public static void reloadFromJson() {
        allBills = loadAllFromJson();
        updateNextBillId();
    }
    
    // --- MÉTODOS JSON ---
    
    private static List<Bill> loadAllFromJson() {
        List<Bill> loadedBills = JsonOperations.loadListFromFile(BILLS_FILE, BILL_LIST_TYPE);
        return loadedBills != null ? loadedBills : new ArrayList<>();
    }
    
    private static boolean saveAllToJson() {
        return JsonOperations.saveListToFile(allBills, BILLS_FILE);
    }
    
    private static int calculateNextBillId() {
        if (allBills.isEmpty()) {
            return 1;
        }
        return allBills.stream()
                .mapToInt(Bill::getBillId)
                .max()
                .orElse(0) + 1;
    }
    
    private static void updateNextBillId() {
        nextBillId = calculateNextBillId();
    }
    
    // --- MÉTODOS DE INFORMACIÓN ---
    
    public String confirmPaymentStatus() {
        if (this.isPaid) {
            return "La factura ID " + this.billId + " está PAGADA.";
        } else {
            return "La factura ID " + this.billId + " está PENDIENTE de pago.";
        }
    }
    
    public String showPendingPayment() {
        if (this.isPaid) {
            return "No hay pagos pendientes para la factura ID " + this.billId;
        }
        
        Customer customer = Customer.findCustomerById(this.customerId);
        Event event = customer != null ? customer.getEventById(this.eventId) : null;
        
        return "PAGO PENDIENTE:\n" +
               "Factura ID: " + this.billId + "\n" +
               "Cliente: " + (customer != null ? customer.getName() : "N/A") + "\n" +
               "Evento: " + (event != null ? event.getEventName() : "N/A") + "\n" +
               "Tipo: " + (event != null ? event.getEventType() : "N/A") + "\n" +
               "Monto a pagar: $" + String.format("%.2f", this.amount) + "\n" +
               "Vencimiento: " + (event != null ? event.getEventDate() : "Por definir");
    }
    
    public String getPaymentSummary() {
        Customer customer = Customer.findCustomerById(this.customerId);
        Event event = customer != null ? customer.getEventById(this.eventId) : null;
        String status = this.isPaid ? "PAGADO" : "PENDIENTE";
        
        return "RESUMEN DE PAGO:\n" +
               "--------------------------------------\n" +
               "Factura ID: " + this.billId + "\n" +
               "Cliente: " + (customer != null ? customer.getName() : "N/A") + "\n" +
               "Evento: " + (event != null ? event.getEventName() : "N/A") + "\n" +
               "Tipo: " + (event != null ? event.getEventType() : "N/A") + "\n" +
               "Estado: " + status + "\n" +
               "Monto: $" + String.format("%.2f", this.amount) + "\n" +
               "Notas: " + (this.notes.isEmpty() ? "Ninguna" : this.notes) + "\n" +
               "--------------------------------------";
    }

    // --- GETTERS Y SETTERS (necesarios para Gson) ---
    
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public boolean isIsPaid() { return isPaid; }
    public void setIsPaid(boolean isPaid) { this.isPaid = isPaid; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    // --- MÉTODOS DE PRESENTACIÓN ---
    
    @Override
    public String toString() {
        String separator = "--------------------------------------";
        String status = this.isPaid ? "PAGADO" : "PENDIENTE";
        
        return separator + "\n" +
               "| FACTURA ID:\t" + this.billId + "\n" +
               "| Cliente ID:\t" + this.customerId + "\n" +
               "| Evento ID:\t" + this.eventId + "\n" +
               "| Estado:\t" + status + "\n" +
               "| Monto:\t$" + String.format("%.2f", this.amount) + "\n" +
               "| Notas:\t" + (this.notes.isEmpty() ? "Ninguna" : this.notes) + "\n" +
               separator;
    }
    
    public void displayBillInfo() {
        System.out.println(this.getPaymentSummary());
    }
    
}

