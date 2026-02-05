package ec.edu.espe.crealtivastudios.controller;

import com.google.gson.Gson;
import ec.edu.espe.crealtivastudios.model.ReportPhotographers;
import ec.edu.espe.crealtivastudios.model.ReporterDetailsEvent;
import java.awt.Component;
import java.awt.Desktop;
import java.io.*;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import utils.CrudOperations;
import utils.JsonOperations;

public class ReportEventController {

    private final String COLLECTION_REPORTS = "Reports";

    public DefaultTableModel getReportTableModel() {
        String[] columns = {"Cliente", "Evento", "Fecha", "Estado de Pago"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        List<Document> events = CrudOperations.findAll("Events");

        for (Document eventDoc : events) {
            String eventName = eventDoc.getString("eventName");
            String date = eventDoc.getString("eventDate");
            int customerId = eventDoc.getInteger("customerId", 0);
            int eventId = eventDoc.getInteger("eventId", 0);

            Document custDoc = CrudOperations.searchOne("Customers", new Document("id", customerId));
            String clientName = (custDoc != null) ? custDoc.getString("name") : "Desconocido";

            Document billDoc = CrudOperations.searchOne("Bills", new Document("eventId", eventId));
            boolean isPaid = (billDoc != null) && billDoc.getBoolean("isPaid", false);
            String status = isPaid ? "CANCELADO" : "PENDIENTE"; 

            model.addRow(new Object[]{clientName, eventName, date, status});
        }
        return model;
    }

    public DefaultTableModel getPhotographerTableModel() {
        String[] columns = {"Evento", "Fotógrafo", "Equipos", "Cliente"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        List<Document> photographers = CrudOperations.findAll("PhotographerDB");
        List<Document> allEvents = CrudOperations.findAll("Events");

        for (Document photoDoc : photographers) {
            if (photoDoc.getBoolean("assigned", false)) {
                String photoName = photoDoc.getString("name");
                String eventNameRaw = photoDoc.getString("assignedEvent");
                
                String equipStr = "Ninguno";
                try {
                    List<String> equipmentList = (List<String>) photoDoc.get("equipment");
                    if (equipmentList != null && !equipmentList.isEmpty()) {
                        equipStr = String.join(", ", equipmentList);
                    }
                } catch (Exception e) { equipStr = "Sin datos"; }

                String clientName = "---";
                
                if (eventNameRaw != null && !eventNameRaw.contains("Bloqueado") && !eventNameRaw.equals("Disponible")) {
                    
                    Document eventDoc = null;
                    for (Document e : allEvents) {
                        if (eventNameRaw.equalsIgnoreCase(e.getString("eventName"))) {
                            eventDoc = e;
                            break;
                        }
                    }

                    if (eventDoc != null) {
                        int customerId = eventDoc.getInteger("customerId", 0);
                        Document custDoc = CrudOperations.searchOne("Customers", new Document("id", customerId));
                        if (custDoc != null) {
                            clientName = custDoc.getString("name");
                        }
                    } else {
                        clientName = "No encontrado en BD";
                    }
                } else {
                    clientName = "Bloqueo Administrativo";
                }

                model.addRow(new Object[]{eventNameRaw, photoName, equipStr, clientName});
            }
        }
        return model;
    }

    public void generateJsonReports(JTable table) {
        Gson gson = new Gson();
        String firstColumn = table.getColumnName(0);
        
        try {
            if ("Cliente".equals(firstColumn)) {
                for (int i = 0; i < table.getRowCount(); i++) {
                    String client = String.valueOf(table.getValueAt(i, 0));
                    String event = String.valueOf(table.getValueAt(i, 1));
                    String date = String.valueOf(table.getValueAt(i, 2));
                    boolean isSettled = "CANCELADO".equals(String.valueOf(table.getValueAt(i, 3)));

                    ReporterDetailsEvent report = new ReporterDetailsEvent(client, event, date, isSettled);
                    saveJsonAndMongo(report, "EventReport_" + event, gson);
                }
                JOptionPane.showMessageDialog(null, "Reporte de Eventos guardado.");
            } 
            else if ("Evento".equals(firstColumn)) {
                for (int i = 0; i < table.getRowCount(); i++) {
                    String event = String.valueOf(table.getValueAt(i, 0));
                    String photo = String.valueOf(table.getValueAt(i, 1));
                    String equip = String.valueOf(table.getValueAt(i, 2));
                    String client = String.valueOf(table.getValueAt(i, 3));

                    ReportPhotographers report = new ReportPhotographers(event, photo, equip, client);
                    saveJsonAndMongo(report, "PhotoReport_" + event, gson);
                }
                JOptionPane.showMessageDialog(null, "Reporte de Fotógrafos guardado.");
            }
        } catch (Exception e) {
            System.err.println("Error generando JSON: " + e.getMessage());
        }
    }

    private void saveJsonAndMongo(Object reportObj, String fileName, Gson gson) {
        JsonOperations.saveObjectToFile(reportObj, fileName);
        CrudOperations.insert(COLLECTION_REPORTS, Document.parse(gson.toJson(reportObj)));
    }

    public void exportToHTML(JTable table, File file, Component view) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            bw.write("<html><head><title>Reporte</title><style>");
            bw.write("body{font-family:Arial;padding:20px;} table{width:100%;border-collapse:collapse;}");
            bw.write("th,td{border:1px solid #999;padding:8px;} th{background:#336699;color:white;}");
            bw.write("</style></head><body><h2>Reporte Generado</h2><table><tr>");
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
            bw.write("\uFEFF");
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