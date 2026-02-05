package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Calendar;
import ec.edu.espe.crealtivastudios.model.Event;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import utils.CrudOperations;

public class CalendarController {

    private final String CALENDAR_COLLECTION = "Calendar";
    private final String EVENTS_COLLECTION = "Events";

 
    private List<Event> eventsCache = new ArrayList<>();


public void loadEventsIntoCombo(javax.swing.JComboBox combo) {
    combo.removeAllItems();

    for (Document doc : CrudOperations.findAll(EVENTS_COLLECTION)) {

        int eventId = doc.getInteger("eventId", 0);
        String eventName = doc.getString("eventName");
        String eventDate = doc.getString("eventDate");
        int eventTypeCode = doc.getInteger("eventTypeCode", 0);
        int customerId = doc.getInteger("customerId", 0);

        if (eventId == 0 || eventName == null) {
            continue;
        }

        Event event = new Event(
                eventId,
                eventName,
                eventDate,
                eventTypeCode,
                customerId
        );

        combo.addItem(event); 
    }
}


    public void showSelectedEventFromCombo(
            JComboBox<String> combo,
            JTextField txtEventId,
            JTextArea txaDetails) {

        int index = combo.getSelectedIndex();

        if (index < 0 || index >= eventsCache.size()) {
            return;
        }

        Event event = eventsCache.get(index);

        txtEventId.setText(String.valueOf(event.getEventId()));

        String details = "Evento Seleccionado:\n"
                + "ID: " + event.getEventId() + "\n"
                + "Nombre: " + event.getEventName() + "\n"
                + "Fecha: " + event.getEventDate() + "\n"
                + "Tipo: " + event.getEventTypeCode();

        txaDetails.setText(details);
    }

    public void saveReminderFromUI(
            String eventIdRaw,
            Date dateRaw,
            Runnable onSuccess,
            Component view) {

        if (eventIdRaw.isEmpty() || dateRaw == null) {
            JOptionPane.showMessageDialog(view,
                    "Seleccione un evento y una fecha.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int eventId = Integer.parseInt(eventIdRaw);
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(dateRaw);

            Calendar calendar = new Calendar(
                    generateNextId(),
                    eventId,
                    "Recordatorio",
                    dateStr
            );

            if (CrudOperations.insert(CALENDAR_COLLECTION, toDocument(calendar))) {
                JOptionPane.showMessageDialog(view, "Recordatorio guardado correctamente.");
                onSuccess.run();
            } else {
                JOptionPane.showMessageDialog(view, "Error al guardar.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public DefaultTableModel getTableModel() {

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Evento", "Fecha"}, 0);

        for (Document doc : CrudOperations.findAll(CALENDAR_COLLECTION)) {
            model.addRow(new Object[]{
                doc.getInteger("id"),
                doc.getInteger("eventId"),
                doc.getString("date")
            });
        }
        return model;
    }


    private int generateNextId() {
        int max = 0;
        for (Document doc : CrudOperations.findAll(CALENDAR_COLLECTION)) {
            int id = doc.getInteger("id", 0);
            if (id > max) {
                max = id;
            }
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
