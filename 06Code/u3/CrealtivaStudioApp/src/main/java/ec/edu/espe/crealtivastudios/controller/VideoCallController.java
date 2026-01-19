package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Customer;
import ec.edu.espe.crealtivastudios.model.VideoCall;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import utils.CrudOperations;

public class VideoCallController {
    private final String COLLECTION = "VideoCalls";
    public final CustomerController customerController = new CustomerController();

    // Tabla lista para mostrar
    public DefaultTableModel getVideoCallsTableModel() {
        String[] columns = {"ID", "Cliente", "Plataforma", "Fecha", "Hora"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        List<Customer> customers = customerController.getAllCustomers();

        for (VideoCall v : getAllVideoCalls()) {
            String customerName = customers.stream()
                    .filter(c -> c.getId() == v.getCustomerId())
                    .map(Customer::getName)
                    .findFirst()
                    .orElse("No registrado");

            model.addRow(new Object[]{
                    v.getId(),
                    customerName,
                    v.getPlatform(),
                    v.getDate(),
                    v.getTime()
            });
        }
        return model;
    }

    public List<VideoCall> getAllVideoCalls() {
        List<VideoCall> list = new ArrayList<>();
        for (Document doc : CrudOperations.findAll(COLLECTION)) {
            VideoCall v = toObject(doc);
            if (v != null) list.add(v);
        }
        return list;
    }

    private VideoCall toObject(Document doc) {
        if (doc == null) return null;
        try {
            int id = ((Number) doc.get("id")).intValue();
            int customerId = ((Number) doc.get("customerId")).intValue();
            String platform = doc.getString("platform");
            String link = doc.getString("link");
            String date = doc.getString("date");
            String time = doc.getString("time");
            return new VideoCall(id, customerId, platform, link, date, time);
        } catch (Exception e) { return null; }
    }

    public void saveVideoCall(String idStr, String customerCombo, String platform, Date date, String time, boolean isEditing, Runnable onSuccess, Component view) {
        try {
            int customerId = parseCustomerId(customerCombo);
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
            int id = isEditing ? Integer.parseInt(idStr) : generateNextId();

            Document doc = new Document("id", id)
                    .append("customerId", customerId)
                    .append("platform", platform)
                    .append("link", "Enlace Pendiente")
                    .append("date", dateStr)
                    .append("time", time);

            boolean ok = isEditing
                    ? CrudOperations.update(COLLECTION, "id", id, doc)
                    : CrudOperations.insert(COLLECTION, doc);

            if (ok) onSuccess.run();
            else JOptionPane.showMessageDialog(view, "No se pudo guardar la videollamada");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error: " + e.getMessage());
        }
    }

    public VideoCall getVideoCallById(int id) {
        Document doc = CrudOperations.searchOne(COLLECTION, new Document("id", id));
        return toObject(doc);
    }

    public void deleteVideoCall(int id, Runnable onSuccess, Component view) {
        boolean deleted = CrudOperations.delete(COLLECTION, "id", id);
        if (deleted) onSuccess.run();
        else JOptionPane.showMessageDialog(view, "No se pudo eliminar la videollamada");
    }

    public int parseCustomerId(String comboValue) {
        return Integer.parseInt(comboValue.split(" - ")[0]);
    }

    private int generateNextId() {
        return getAllVideoCalls().stream().mapToInt(VideoCall::getId).max().orElse(0) + 1;
    }
}
