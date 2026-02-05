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
    private final String EVENTS_COLLECTION = "Events"; 
    private final String CUSTOMERS_COLLECTION = "Customers";

    public BillController() {
        syncEventsToBills(); 
    }

    public DefaultTableModel getTableModel() {
        syncEventsToBills(); 
        
        String[] cols = {"ID Factura", "Cliente", "Evento", "Monto", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        
        for (Bill b : getAllBills()) {
            
            Document custDoc = CrudOperations.searchOne(CUSTOMERS_COLLECTION, new Document("id", b.getCustomerId()));
            String clientName = (custDoc != null) ? custDoc.getString("name") : "Cliente ID: " + b.getCustomerId();

            Document eventDoc = CrudOperations.searchOne(EVENTS_COLLECTION, new Document("eventId", b.getEventId()));
            String eventName = (eventDoc != null) ? eventDoc.getString("eventName") : "Evento ID: " + b.getEventId();

            model.addRow(new Object[]{
                b.getBillId(), 
                clientName, 
                eventName, 
                String.format("$%.2f", b.getAmount()), 
                b.isPaid() ? "CANCELADO" : "PENDIENTE"
            });
        }
        return model;
    }

    public void syncEventsToBills() {
        List<Document> events = CrudOperations.findAll(EVENTS_COLLECTION);
        List<Document> bills = CrudOperations.findAll(COLLECTION);

        for (Document eventDoc : events) {
            int eventId = eventDoc.getInteger("eventId");
            int customerId = eventDoc.getInteger("customerId", 0);

            boolean hasBill = false;
            for (Document bill : bills) {
                if (bill.getInteger("eventId") == eventId) {
                    hasBill = true;
                    break;
                }
            }

            if (!hasBill) {
                int newBillId = eventId; 

                double amount = 0.0;
                
                if (eventDoc.containsKey("eventTypeCode")) {
                    int typeCode = eventDoc.getInteger("eventTypeCode");
                    if (typeCode == 1) amount = 135.0; 
                    else if (typeCode == 3) amount = 20.0; 
                    else amount = 50.0;
                }

                Document newBill = new Document("billId", newBillId)
                        .append("customerId", customerId)
                        .append("eventId", eventId)
                        .append("amount", amount)
                        .append("notes", "Generada autom")
                        .append("isPaid", false); 

                CrudOperations.insert(COLLECTION, newBill);
            }
        }
    }

    public void updateStatusFromUI(String idRaw, String statusText, Runnable onSuccess, Component view) {
        if (idRaw == null) return;
        boolean isPaid = "CANCELADO".equals(statusText);
        
        int billId = Integer.parseInt(idRaw);
        
        if (CrudOperations.update(COLLECTION, "billId", billId, new Document("isPaid", isPaid))) {
            onSuccess.run();
        }
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