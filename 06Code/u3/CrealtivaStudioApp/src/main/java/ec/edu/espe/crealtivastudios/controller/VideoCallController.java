package ec.edu.espe.crealtivastudios.controller;

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

    public DefaultTableModel getTableModel() {
        String[] columns = {"ID", "ID Cliente", "Plataforma", "Fecha", "Hora", "Link"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (VideoCall v : getAllVideoCalls()) {
            model.addRow(new Object[]{v.getId(), v.getCustomerId(), v.getPlatform(), v.getDate(), v.getTime(), v.getLink()});
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
            // Conversión segura de tipos numéricos (Double/Int) desde MongoDB
            int id = (doc.get("id") != null) ? ((Number) doc.get("id")).intValue() : 0;
            int cId = (doc.get("customerId") != null) ? ((Number) doc.get("customerId")).intValue() : 0;
            String platform = doc.getString("platform") != null ? doc.getString("platform") : "Zoom";
            String link = doc.getString("link") != null ? doc.getString("link") : "";
            String date = doc.getString("date") != null ? doc.getString("date") : "";
            String time = doc.getString("time") != null ? doc.getString("time") : "";
            return new VideoCall(id, cId, platform, link, date, time);
        } catch (Exception e) { return null; }
    }

    public void saveFromUI(String idS, String cSel, String plat, String link, Date dRaw, String t, boolean isE, Runnable ok, Component v) {
        try {
            int cId = Integer.parseInt(cSel.split(" - ")[0]);
            String dStr = (dRaw != null) ? new SimpleDateFormat("yyyy-MM-dd").format(dRaw) : "";
            int id = isE ? Integer.parseInt(idS) : generateNextId();
            Document doc = new Document("id", id).append("customerId", cId).append("platform", plat).append("link", link).append("date", dStr).append("time", t);
            if (isE ? CrudOperations.update(COLLECTION, "id", id, doc) : CrudOperations.insert(COLLECTION, doc)) ok.run();
        } catch (Exception e) { JOptionPane.showMessageDialog(v, "Error: " + e.getMessage()); }
    }

    public void findForEdit(String idRaw, java.util.function.Consumer<VideoCall> onFound, Component view) {
        if (idRaw == null) return;
        Document doc = CrudOperations.searchOne(COLLECTION, new Document("id", Integer.parseInt(idRaw)));
        if (doc != null) onFound.accept(toObject(doc));
    }

    private int generateNextId() {
        int max = 0;
        for (VideoCall v : getAllVideoCalls()) if (v.getId() > max) max = v.getId();
        return max + 1;
    }
}