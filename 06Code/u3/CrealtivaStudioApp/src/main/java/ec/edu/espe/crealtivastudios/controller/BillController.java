package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Bill;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import utils.CrudOperations;

public class BillController {
    private final String COLLECTION = "Bills";
    private final String EVENTS_COLLECTION = "Events"; // Referencia a eventos

    // Constructor (Si no tienes uno, agrégalo así)
    public BillController() {
        // Esto buscará eventos huerfanos (sin factura) y les creará una
        syncEventsToBills(); 
    }

    public DefaultTableModel getTableModel() {
        // Aseguramos sincronización antes de mostrar
        syncEventsToBills(); 
        
        String[] cols = {"ID", "Cliente", "Evento", "Monto", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Bill b : getAllBills()) {
            model.addRow(new Object[]{
                b.getBillId(), 
                b.getCustomerId(), 
                b.getEventId(), 
                String.format("$%.2f", b.getAmount()), 
                b.isPaid() ? "CANCELADO" : "PENDIENTE"
            });
        }
        return model;
    }

    // --- NUEVO MÉTODO PARA CORREGIR TUS DATOS EXISTENTES ---
    public void syncEventsToBills() {
        List<Document> events = CrudOperations.findAll(EVENTS_COLLECTION);
        List<Document> bills = CrudOperations.findAll(COLLECTION);

        for (Document eventDoc : events) {
            int eventId = eventDoc.getInteger("eventId");
            
            // Verificar si este evento YA tiene factura
            boolean hasBill = false;
            for (Document bill : bills) {
                if (bill.getInteger("eventId") == eventId) {
                    hasBill = true;
                    break;
                }
            }

            // Si NO tiene factura, la creamos ahora mismo
            if (!hasBill) {
                int newBillId = generateNextBillId();
                
                // Intentamos obtener el precio o asignamos un base
                double amount = 0.0;
                // Lógica simple de precio basada en tu EventController anterior
                String type = eventDoc.getString("eventType"); // "Bautizo"
                if ("Bautizo".equalsIgnoreCase(type)) amount = 20.0;
                else if ("Bodas".equalsIgnoreCase(type)) amount = 135.0;
                else amount = 50.0;

                // Recuperar ID Cliente (con manejo de nulos por si acaso)
                int custId = eventDoc.containsKey("customerId") ? eventDoc.getInteger("customerId") : 1;

                Document newBill = new Document("billId", newBillId)
                        .append("customerId", custId)
                        .append("eventId", eventId)
                        .append("amount", amount)
                        .append("notes", "Generada por Sincronización")
                        .append("isPaid", false); // Por defecto pendiente

                CrudOperations.insert(COLLECTION, newBill);
                System.out.println("Factura creada para evento antiguo: " + eventDoc.getString("eventName"));
            }
        }
    }

    private int generateNextBillId() {
        int max = 0;
        for (Document d : CrudOperations.findAll(COLLECTION)) {
            if (d.containsKey("billId") && d.getInteger("billId") > max) {
                max = d.getInteger("billId");
            }
        }
        return max + 1;
    }
    // -------------------------------------------------------

    public void updateStatusFromUI(String idRaw, String statusText, Runnable onSuccess, Component view) {
        if (idRaw == null) return;
        boolean isPaid = "CANCELADO".equals(statusText);
        if (CrudOperations.update(COLLECTION, "billId", Integer.parseInt(idRaw), new Document("isPaid", isPaid))) onSuccess.run();
    }

    public List<Bill> getAllBills() {
        List<Bill> list = new ArrayList<>();
        for (Document d : CrudOperations.findAll(COLLECTION)) list.add(documentToBill(d));
        return list;
    }

    private Bill documentToBill(Document doc) {
        if (doc == null) return null;
        Number amountNum = (Number) doc.get("amount");
        return new Bill(
            doc.getInteger("billId"), 
            doc.getInteger("customerId"), 
            doc.getInteger("eventId"), 
            amountNum != null ? amountNum.doubleValue() : 0.0, 
            doc.getString("notes"), 
            doc.getBoolean("isPaid", false)
        );
    }
}