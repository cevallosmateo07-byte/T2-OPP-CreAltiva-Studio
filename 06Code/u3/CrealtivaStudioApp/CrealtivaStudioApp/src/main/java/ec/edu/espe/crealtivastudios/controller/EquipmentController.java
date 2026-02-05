package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Equipment;
import org.bson.Document;
import utils.CrudOperations;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class EquipmentController {
    private final String COLLECTION = "Equipments";

    public DefaultTableModel getTableModel() {
        String[] cols = {"ID", "Nombre", "Descripción", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Document d : CrudOperations.findAll(COLLECTION)) {
            model.addRow(new Object[]{d.getInteger("id"), d.getString("name"), d.getString("description"), d.getString("status")});
        }
        return model;
    }

    public void saveFromUI(String idS, String n, String d, String s, boolean isE, Runnable ok, Component v) {
        int id = isE ? Integer.parseInt(idS) : generateNextId();
        Document doc = new Document("id", id).append("name", n).append("description", d).append("status", s);
        if (isE ? CrudOperations.update(COLLECTION, "id", id, doc) : CrudOperations.insert(COLLECTION, doc)) ok.run();
    }

    public void findForEdit(String idRaw, java.util.function.Consumer<Equipment> onFound, Component view) {
        if (idRaw == null) return;
        Document d = CrudOperations.searchOne(COLLECTION, new Document("id", Integer.parseInt(idRaw)));
        if (d != null) onFound.accept(new Equipment(d.getInteger("id"), d.getString("name"), d.getString("description"), d.getString("status")));
    }

    public void deleteFromUI(String idRaw, Runnable ok, Component v) {
        if (idRaw != null && CrudOperations.delete(COLLECTION, "id", Integer.parseInt(idRaw))) ok.run();
    }

    private int generateNextId() {
        int max = 0;
        for (Document d : CrudOperations.findAll(COLLECTION)) if (d.getInteger("id") > max) max = d.getInteger("id");
        return max + 1;
    }
}