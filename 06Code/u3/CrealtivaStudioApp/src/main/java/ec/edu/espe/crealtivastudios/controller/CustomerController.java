package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Customer;
import ec.edu.espe.crealtivastudios.view.FrmEvent;
import org.bson.Document;
import utils.CrudOperations;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JFrame;

public class CustomerController {

    private final String COLLECTION = "Customers";

    public DefaultTableModel getTableModel() {
        String[] columns = {"ID", "Nombre", "Teléfono", "Email", "Dirección"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        for (Customer c : getAllCustomers()) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), c.getEmail(), c.getAddress()});
        }
        return model;
    }

    public void saveFromUI(String idStr, String name, String phone, String email, String address, boolean isEditing, Runnable onSuccess, Component view) {
        if (name.trim().isEmpty() || phone.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nombre y Teléfono requeridos.");
            return;
        }
        try {
            int id = isEditing ? Integer.parseInt(idStr) : generateNextId();
            Document doc = new Document("id", id).append("name", name).append("phone", phone).append("email", email).append("address", address);
            boolean success = isEditing ? CrudOperations.update(COLLECTION, "id", id, doc) : CrudOperations.insert(COLLECTION, doc);
            if (success) { onSuccess.run(); }
        } catch (Exception e) { JOptionPane.showMessageDialog(view, "Error: " + e.getMessage()); }
    }

    public void findForEdit(String idRaw, Consumer<Customer> onFound, Component view) {
        if (idRaw == null) return;
        Document doc = CrudOperations.searchOne(COLLECTION, new Document("id", Integer.parseInt(idRaw)));
        if (doc != null) onFound.accept(documentToCustomer(doc));
    }

    public void deleteFromUI(String idRaw, Runnable onSuccess, Component view) {
        if (idRaw != null && CrudOperations.delete(COLLECTION, "id", Integer.parseInt(idRaw))) onSuccess.run();
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        for (Document d : CrudOperations.findAll(COLLECTION)) list.add(documentToCustomer(d));
        return list;
    }

    private int generateNextId() {
        int max = 0;
        for (Customer c : getAllCustomers()) if (c.getId() > max) max = c.getId();
        return max + 1;
    }

    private Customer documentToCustomer(Document d) {
        return new Customer(d.getInteger("id"), d.getString("name"), d.getString("phone"), d.getString("email"), d.getString("address"));
    }

    public void selectCustomerForEvent(JTable table, JFrame currentView, Component parentView) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(currentView, "Seleccione un cliente", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Object idVal = table.getValueAt(selectedRow, 0);
            int id = Integer.parseInt(idVal.toString());
            String name = table.getValueAt(selectedRow, 1).toString();
            int opt = JOptionPane.showConfirmDialog(currentView, "¿Asignar evento a " + name + " (ID: " + id + ")?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                FrmEvent.selectedCustomerId = id;
                currentView.dispose();
                if (parentView != null) {
                    JOptionPane.showMessageDialog(parentView, "Cliente seleccionado: " + name);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}