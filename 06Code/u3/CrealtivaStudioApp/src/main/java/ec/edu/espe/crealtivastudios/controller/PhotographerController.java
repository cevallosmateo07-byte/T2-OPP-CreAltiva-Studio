package ec.edu.espe.crealtivastudios.controller;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import utils.CrudOperations;

public class PhotographerController {
    private final String COLL_PHOTO = "PhotographerDB";
    private final String COLL_EVENTS = "Events";

    // --- Usado por FrmPhotographer ---
    public void fillListModel(DefaultListModel<String> model) {
        model.clear();
        for (Document d : CrudOperations.findAll(COLL_PHOTO)) {
            String s = d.getBoolean("assigned", false) ? "OCUPADO" : "DISPONIBLE";
            model.addElement(d.getInteger("id", 0) + " - " + d.getString("name") + " [" + s + "]");
        }
    }

    public void updateStatusLabel(JLabel label, String name) {
        Document d = CrudOperations.searchOne(COLL_PHOTO, new Document("name", name));
        if (d != null) {
            boolean a = d.getBoolean("assigned", false);
            label.setText(a ? "OCUPADO" : "DISPONIBLE");
            label.setForeground(a ? Color.RED : new Color(0, 153, 51));
        }
    }

    public void selectInListByName(JList<String> list, String name) {
        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).contains(name)) { list.setSelectedIndex(i); return; }
        }
    }

    public void deletePhotographer(String selected, Runnable onSuccess, Component view) {
        if (selected == null || !selected.contains(" - ")) return;
        if (JOptionPane.showConfirmDialog(view, "¿Eliminar fotógrafo?") == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(selected.split(" - ")[0]);
            if (CrudOperations.delete(COLL_PHOTO, "id", id)) onSuccess.run();
        }
    }

    // --- Usado por FrmAssignEvent ---
    public DefaultTableModel getAssignTableModel() {
        String[] cols = {"ID Evento", "Nombre Evento", "Asignado A"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Document d : CrudOperations.findAll(COLL_EVENTS)) {
            String a = d.getString("assignedPhotographer");
            model.addRow(new Object[]{d.getInteger("eventId"), d.getString("eventName"), (a == null || a.isEmpty()) ? "NO ASIGNADO" : a});
        }
        return model;
    }

    public DefaultComboBoxModel<String> getPhotographersComboModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("- Seleccione -");
        for (Document d : CrudOperations.findAll(COLL_PHOTO)) if (!d.getBoolean("assigned", false)) model.addElement(d.getString("name"));
        return model;
    }

    public DefaultComboBoxModel<String> getEventsComboModel() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("- Seleccione -");
        for (Document d : CrudOperations.findAll(COLL_EVENTS)) model.addElement(d.getString("eventName"));
        return model;
    }

    public void assignEvent(String photo, String event, Runnable ok, Component v) {
        if (photo.startsWith("-") || event.startsWith("-")) return;
        if (CrudOperations.update(COLL_PHOTO, "name", photo, new Document("assigned", true).append("assignedEvent", event)) &&
            CrudOperations.update(COLL_EVENTS, "eventName", event, new Document("assignedPhotographer", photo))) {
            JOptionPane.showMessageDialog(v, "Asignación exitosa.");
            ok.run();
        }
    }

    public void removeAssignment(String event, String photo, Runnable ok, Component v) {
        if (CrudOperations.update(COLL_EVENTS, "eventName", event, new Document("assignedPhotographer", "")) &&
            CrudOperations.update(COLL_PHOTO, "name", photo, new Document("assigned", false).append("assignedEvent", ""))) {
            ok.run();
        }
    }

    // --- Usado por FrmAvailable y Asistencia ---
    public void initDefaultPhotographers() {
        String[] names = {"Micaela Garcia", "Luisa Andrade", "Alexis Fares", "Paola Maza"};
        for (String n : names) if (CrudOperations.searchOne(COLL_PHOTO, new Document("name", n)) == null)
            CrudOperations.insert(COLL_PHOTO, new Document("id", (int)(Math.random()*1000)).append("name", n).append("assigned", false).append("assignedEvent", "").append("attending", false));
    }

    public boolean isPhotographerAssigned(String n) {
        Document d = CrudOperations.searchOne(COLL_PHOTO, new Document("name", n));
        return d != null && d.getBoolean("assigned", false);
    }

    public void updateAvailabilityManual(String n, String sel) {
        boolean a = sel.trim().equalsIgnoreCase("No disponible");
        CrudOperations.update(COLL_PHOTO, "name", n, new Document("assigned", a).append("assignedEvent", a ? "Bloqueado Manual" : ""));
    }

    public DefaultTableModel getAttendanceTableModel() {
        String[] cols = {"ID", "Nombre", "Evento", "Asistencia"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public Class<?> getColumnClass(int c) { return c == 3 ? Boolean.class : String.class; }
            @Override public boolean isCellEditable(int r, int c) { return c == 3; }
        };
        for (Document d : CrudOperations.findAll(COLL_PHOTO)) model.addRow(new Object[]{d.getInteger("id"), d.getString("name"), d.getString("assignedEvent"), d.getBoolean("attending", false)});
        return model;
    }

    public void updateAttendance(int id, boolean att) { 
        CrudOperations.update(COLL_PHOTO, "id", id, new Document("attending", att)); 
    }
}