package ec.edu.espe.crealtivastudios.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.awt.Component;
import java.awt.Desktop;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import utils.MongoConnection;

public class ReportEventController {

    /**
     * BUCLE MAESTRO: Cruza las colecciones para armar el reporte.
     * Mantiene la vista limpia de lógica de "Joins" manuales.
     */
    public DefaultTableModel getReportTableModel() {
        String[] columns = {"Fotógrafo", "Cliente", "Evento Asignado", "Fecha Evento", "Tipo Evento", "Medio de contacto", "Videollamada", "Pago", "Equipos que lleva"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            MongoCollection<Document> photoColl = MongoConnection.getPhotographerCollection();
            MongoCollection<Document> eventColl = MongoConnection.getEventsCollection();
            MongoCollection<Document> customersColl = MongoConnection.getConnection().getCollection("Customers");
            MongoCollection<Document> videocallsColl = MongoConnection.getConnection().getCollection("Videocalls");

            for (Document photoDoc : photoColl.find()) {
                String photoName = photoDoc.getString("name");
                String eventName = photoDoc.getString("assignedEvent");
                boolean isAssigned = photoDoc.getBoolean("assigned", false);

                // Valores por defecto
                String clientName = "---", dateStr = "---", typeStr = "---", contactMedium = "---", videoCall = "NO", paymentStatus = "PENDIENTE", equipStr = "Ninguno";

                if (isAssigned && eventName != null && !eventName.isEmpty() && !eventName.equals("Bloqueado por disponibilidad")) {
                    Document eventDoc = eventColl.find(Filters.eq("eventName", eventName)).first();
                    
                    if (eventDoc != null) {
                        // Lógica de Evento
                        Date date = eventDoc.getDate("date");
                        if (date != null) dateStr = dateFormat.format(date);
                        typeStr = eventDoc.getString("eventType");
                        paymentStatus = (eventDoc.getBoolean("paid", false)) ? "PAGADO" : "PENDIENTE";

                        // Lógica de Cliente
                        Integer customerId = eventDoc.getInteger("customerId");
                        if (customerId != null) {
                            Document customerDoc = customersColl.find(Filters.eq("id", customerId)).first();
                            if (customerDoc != null) {
                                clientName = customerDoc.getString("name");
                                contactMedium = customerDoc.getString("medium");
                            }
                            // Lógica de Videollamada
                            Document videoDoc = videocallsColl.find(Filters.eq("id", customerId)).first();
                            if (videoDoc != null) {
                                videoCall = "SI";
                                contactMedium = videoDoc.getString("medium");
                            }
                        }
                    }
                } else {
                    eventName = isAssigned ? "BLOQUEADO" : "DISPONIBLE";
                }

                // Lógica de Equipos
                try {
                    List<String> equipmentList = (List<String>) photoDoc.get("equipment");
                    if (equipmentList != null && !equipmentList.isEmpty()) equipStr = String.join(", ", equipmentList);
                } catch (Exception e) { equipStr = "Sin equipos"; }

                model.addRow(new Object[]{photoName, clientName, eventName, dateStr, typeStr, contactMedium, videoCall, paymentStatus, equipStr});
            }
        } catch (Exception e) {
            System.err.println("Error generando reporte: " + e.getMessage());
        }
        return model;
    }

    // =========================================================================
    //  LÓGICA DE EXPORTACIÓN (Escritura de archivos)
    // =========================================================================

    public void exportToHTML(JTable table, File file, Component view) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            bw.write("<html><head><title>Reporte CreAltiva</title><style>");
            bw.write("body{font-family:Arial;padding:20px;} table{width:100%;border-collapse:collapse;}");
            bw.write("th,td{border:1px solid #999;padding:8px;} th{background:#336699;color:white;}");
            bw.write("</style></head><body><h2>Reporte General de Eventos</h2><table><tr>");
            
            for (int i = 0; i < table.getColumnCount(); i++) bw.write("<th>" + table.getColumnName(i) + "</th>");
            bw.write("</tr>");

            for (int i = 0; i < table.getRowCount(); i++) {
                bw.write("<tr>");
                for (int j = 0; j < table.getColumnCount(); j++) bw.write("<td>" + table.getValueAt(i, j) + "</td>");
                bw.write("</tr>");
            }
            bw.write("</table></body></html>");
            Desktop.getDesktop().open(file);
        } catch (Exception e) { JOptionPane.showMessageDialog(view, "Error HTML: " + e.getMessage()); }
    }

    public void exportToCSV(JTable table, File file, Component view) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            bw.write("\uFEFF"); // Soporte para acentos en Excel
            for (int i = 0; i < table.getColumnCount(); i++) {
                bw.write(table.getColumnName(i) + (i < table.getColumnCount() - 1 ? ";" : ""));
            }
            bw.newLine();
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    String val = String.valueOf(table.getValueAt(i, j)).replace(";", ",");
                    bw.write("\"" + val + "\"" + (j < table.getColumnCount() - 1 ? ";" : ""));
                }
                bw.newLine();
            }
            JOptionPane.showMessageDialog(view, "Reporte CSV guardado.");
        } catch (Exception e) { JOptionPane.showMessageDialog(view, "Error CSV: " + e.getMessage()); }
    }
}