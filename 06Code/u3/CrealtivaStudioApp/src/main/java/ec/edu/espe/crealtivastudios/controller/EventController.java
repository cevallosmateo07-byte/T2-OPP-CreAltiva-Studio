package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Event;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.bson.Document;
import utils.CrudOperations;
import javax.swing.JComboBox;

public class EventController {
    
    private final String COLLECTION = "Events";

    // Constantes
    public static final int TYPE_BODAS = 1;
    public static final int TYPE_CUMPLEANOS = 2;
    public static final int TYPE_BAUTIZOS = 3;
    public static final int TYPE_GRADUACIONES = 4;

    // =========================================================================
    //  SECCION GUI (PARA LA VISTA) - AQUÍ ESTÁ TODA LA LÓGICA VISUAL
    // =========================================================================

    /**
     * Construye el texto completo para el JTextArea.
     * La vista solo tiene que hacer setText().
     */
    public String getEventsListText() {
        List<Event> events = getAllEvents();
        if (events.isEmpty()) return "No hay eventos registrados.";

        StringBuilder sb = new StringBuilder();
        for (Event e : events) {
            String typeStr = getEventTypeString(e.getEventTypeCode());
            double price = calculateEventPrice(e);
            
            sb.append(String.format(
                "ID: %d | Evento: %s | Tipo: %s | Fecha: %s | Precio Est.: $%.2f\n--------------------------------\n",
                e.getEventId(), e.getEventName(), typeStr, e.getEventDate(), price
            ));
        }
        return sb.toString();
    }

    /**
     * Valida, Convierte y Guarda. Maneja los mensajes de error/éxito internamente.
     */
    public void saveEventFromUI(String rawName, Date rawDate, int typeIndex, int customerId, Runnable onSuccess, Component view) {
        // 1. Validaciones (IFs movidos aquí)
        if (customerId <= 0) {
            // Lógica de negocio: Si no hay cliente, preguntar si usa genérico
            int opt = JOptionPane.showConfirmDialog(view, "No seleccionó cliente. ¿Usar genérico (ID 1)?", "Aviso", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                customerId = 1; 
            } else {
                return; // Cortamos ejecución
            }
        }
        
        if (rawName == null || rawName.trim().length() < 3) {
            JOptionPane.showMessageDialog(view, "El nombre debe tener al menos 3 letras.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (rawDate == null) {
            JOptionPane.showMessageDialog(view, "Seleccione una fecha válida.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Proceso de Guardado
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = sdf.format(rawDate);
            int typeCode = typeIndex + 1;
            
            Event newEvent = new Event(generateNextId(), rawName.trim(), dateStr, typeCode, customerId);

            if (createEvent(newEvent)) {
                JOptionPane.showMessageDialog(view, "Evento guardado exitosamente.");
                onSuccess.run(); // <--- Ejecuta limpieza de vista
            } else {
                JOptionPane.showMessageDialog(view, "Error al guardar en BD.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    //  LÓGICA DE NEGOCIO Y CRUD
    // =========================================================================

    public boolean createEvent(Event event) {
        return CrudOperations.insert(COLLECTION, eventToDocument(event));
    }

    public List<Event> getAllEvents() {
        List<Event> list = new ArrayList<>();
        for (Document doc : CrudOperations.findAll(COLLECTION)) list.add(documentToEvent(doc));
        return list;
    }

    public int generateNextId() {
        int max = 0;
        for (Event e : getAllEvents()) if (e.getEventId() > max) max = e.getEventId();
        return max + 1;
    }

    public double calculateEventPrice(Event event) {
        double basePrice = 50.00;
        switch (event.getEventTypeCode()) {
            case TYPE_BODAS: basePrice = 150.00 * 0.90; break; // Con descuento
            case TYPE_CUMPLEANOS: basePrice = 80.00; break;
            case TYPE_BAUTIZOS: basePrice = 20.00; break;
            case TYPE_GRADUACIONES: basePrice = 60.00; break;
        }
        return basePrice;
    }
    
    public String getEventTypeString(int typeCode) {
        switch (typeCode) {
            case TYPE_BODAS: return "Bodas";
            case TYPE_CUMPLEANOS: return "Cumpleaños";
            case TYPE_BAUTIZOS: return "Bautizos";
            case TYPE_GRADUACIONES: return "Graduaciones";
            default: return "Otro";
        }
    }

    // --- MAPPERS ---
    private Document eventToDocument(Event event) {
        return new Document("eventId", event.getEventId())
                .append("eventName", event.getEventName())
                .append("eventDate", event.getEventDate())
                .append("eventTypeCode", event.getEventTypeCode())
                .append("customerId", event.getCustomerId());
    }

    private Event documentToEvent(Document doc) {
        return new Event(
            doc.getInteger("eventId"),
            doc.getString("eventName"),
            doc.getString("eventDate"),
            doc.getInteger("eventTypeCode", 1),
            doc.getInteger("customerId", 0)
        );
    }
    
    public void loadEventsIntoComboBox(JComboBox<String> combo) {
    combo.removeAllItems();

    List<Event> events = getAllEvents();
    if (events.isEmpty()) {
        combo.addItem("No hay eventos registrados");
        return;
    }

    for (Event e : events) {
        combo.addItem(
            e.getEventId() + " - " + e.getEventName()
        );
    }
}
    
}