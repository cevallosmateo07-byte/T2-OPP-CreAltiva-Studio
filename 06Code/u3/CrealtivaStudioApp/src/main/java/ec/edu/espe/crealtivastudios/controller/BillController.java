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

    public DefaultTableModel getTableModel() {
        String[] cols = {"ID", "Cliente", "Evento", "Monto", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Bill b : getAllBills()) {
            model.addRow(new Object[]{b.getBillId(), b.getCustomerId(), b.getEventId(), String.format("$%.2f", b.getAmount()), b.isPaid() ? "CANCELADO" : "PENDIENTE"});
        }
        return model;
    }

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
        return new Bill(doc.getInteger("billId"), doc.getInteger("customerId"), doc.getInteger("eventId"), amountNum != null ? amountNum.doubleValue() : 0.0, doc.getString("notes"), doc.getBoolean("isPaid", false));
    }
}