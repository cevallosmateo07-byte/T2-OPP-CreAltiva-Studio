package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Calendar;
import ec.edu.espe.crealtivastudios.model.Event; // Necesitamos buscar eventos
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import utils.CrudOperations;

public class CalendarController {

    private final String CALENDAR_COLLECTION = "Calendar";
    private final String EVENTS_COLLECTION = "Events"; // Para buscar eventos

    // --- LÓGICA PARA LA VISTA ---

    // 1. BUSCAR EVENTO Y MOSTRAR DETALLES
    public void searchEventForUI(String idRaw, javax.swing.JTextArea txaDetails, Component view) {
        if (idRaw == null || idRaw.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Ingrese un ID de evento.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int eventId = Integer.parseInt(idRaw);
            Document doc = CrudOperations.searchOne(EVENTS_COLLECTION, new Document("eventId", eventId));

            if (doc != null) {
                String details = String.format(
                    "Evento Encontrado:\nID: %d\nNombre: %s\nFecha: %s\nTipo: %d",
                    doc.getInteger("eventId"),
                    doc.getString("eventName"),
                    doc.getString("eventDate"),
                    doc.getInteger("eventTypeCode")
                );
                txaDetails.setText(details);
            } else {
                txaDetails.setText("");
                JOptionPane.showMessageDialog(view, "No se encontró el evento con ID: " + eventId, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "El ID debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 2. GUARDAR RECORDATORIO (Botón SÍ)
    public void saveReminderFromUI(String eventIdRaw, Date dateRaw, Runnable onSuccess, Component view) {
        if (eventIdRaw.isEmpty() || dateRaw == null) {
            JOptionPane.showMessageDialog(view, "Busque un evento y seleccione una fecha.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int eventId = Integer.parseInt(eventIdRaw);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = sdf.format(dateRaw);

            Calendar calendar = new Calendar(generateNextId(), eventId, "Recordatorio Auto", dateStr);

            if (CrudOperations.insert(CALENDAR_COLLECTION, toDocument(calendar))) {
                JOptionPane.showMessageDialog(view, "Recordatorio guardado.");
                onSuccess.run();
            } else {
                JOptionPane.showMessageDialog(view, "Error al guardar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error: " + e.getMessage());
        }
    }

    // 3. GENERAR TABLA DE RECORDATORIOS
    public DefaultTableModel getTableModel() {
        String[] columns = {"ID Rec", "ID Evento", "Fecha Recordatorio"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        for (Document doc : CrudOperations.findAll(CALENDAR_COLLECTION)) {
            model.addRow(new Object[]{
                doc.getInteger("id"),
                doc.getInteger("eventId"),
                doc.getString("date")
            });
        }
        return model;
    }

    // --- HELPERS ---
    private int generateNextId() {
        int max = 0;
        for (Document doc : CrudOperations.findAll(CALENDAR_COLLECTION)) {
            int id = doc.getInteger("id", 0);
            if (id > max) max = id;
        }
        return max + 1;
    }

    private Document toDocument(Calendar c) {
        return new Document("id", c.getId())
                .append("eventId", c.getEventId())
                .append("description", c.getDescription())
                .append("date", c.getDate());
    }
}