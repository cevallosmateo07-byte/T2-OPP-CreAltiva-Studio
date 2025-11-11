



package ec.edu.espe.crealtivastudio.controller;

import ec.edu.espe.crealtivastudio.model.Event;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/**
 *
 * @author Daniel
 */

public class JsonEventDao implements EventDao {
    
    private static final String FOLDER_PATH = "data";
    private static final String FILE_PATH = FOLDER_PATH + File.separator + "events.json"; // Archivo renombrado

    // --- Metodos de Archivos ---
    private String readFileContent() {
        new File(FOLDER_PATH).mkdirs(); 
        File file = new File(FILE_PATH);
        if (!file.exists() || file.length() == 0) { return "[]"; }
        try (Scanner scanner = new Scanner(file)) {
            scanner.useDelimiter("\\Z");
            return scanner.next();
        } catch (IOException e) {
            System.err.println("ERROR DE LECTURA DE ARCHIVO: " + e.getMessage());
            return "[]";
        }
    }
    
    private void writeFileContent(String content) {
        new File(FOLDER_PATH).mkdirs(); 
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("ERROR DE ESCRITURA JSON: " + e.getMessage());
        }
    }
    
    // --- Serializacion/Deserializacion Manual de Eventos ---
    private Event jsonToEvent(String jsonObject) {
        Pattern idPattern = Pattern.compile("\"eventId\"\\s*:\\s*(\\d+)");
        Pattern custIdPattern = Pattern.compile("\"customerId\"\\s*:\\s*(\\d+)");
        Pattern namePattern = Pattern.compile("\"eventName\"\\s*:\\s*\"([^\"]*)\"");
        Pattern datePattern = Pattern.compile("\"eventDate\"\\s*:\\s*\"([^\"]*)\"");
        Pattern detailsPattern = Pattern.compile("\"eventDetails\"\\s*:\\s*\"([^\"]*)\"");
        
        Matcher m;
        int eventId = 0, customerId = 0;
        String eventName = "", eventDate = "", eventDetails = "";

        m = idPattern.matcher(jsonObject); if (m.find()) eventId = Integer.parseInt(m.group(1));
        m = custIdPattern.matcher(jsonObject); if (m.find()) customerId = Integer.parseInt(m.group(1));
        m = namePattern.matcher(jsonObject); if (m.find()) eventName = m.group(1);
        m = datePattern.matcher(jsonObject); if (m.find()) eventDate = m.group(1);
        m = detailsPattern.matcher(jsonObject); if (m.find()) eventDetails = m.group(1);

        return new Event(eventId, customerId, eventName, eventDate, eventDetails);
    }
    
    private String eventToJson(Event event) {
        return String.format(
            "\t{\n\t\t\"eventId\": %d,\n\t\t\"customerId\": %d,\n\t\t\"eventName\": \"%s\",\n\t\t\"eventDate\": \"%s\",\n\t\t\"eventDetails\": \"%s\"\n\t}",
            event.getEventId(),
            event.getCustomerId(),
            event.getEventName(),
            event.getEventDate(),
            event.getEventDetails()
        );
    }
    
    private String formatListToJsonArray(List<Event> events) {
        String innerContent = events.stream()
            .map(this::eventToJson)
            .collect(Collectors.joining(",\n"));
            
        return "[\n" + innerContent + "\n]";
    }

    // --- Implementacion de la Interfaz EventDao ---
    @Override
    public List<Event> findAll() {
        String jsonArrayContent = readFileContent();
        List<Event> events = new ArrayList<>();
        
        if (jsonArrayContent.length() > 2) {
            Pattern eventPattern = Pattern.compile("\\{[^{}]+\\}");
            Matcher m = eventPattern.matcher(jsonArrayContent);
            
            while (m.find()) {
                events.add(jsonToEvent(m.group()));
            }
        }
        return events;
    }

    @Override
    public boolean save(Event event) {
        List<Event> events = findAll();
        if (events.stream().anyMatch(e -> e.getEventId() == event.getEventId())) { return false; }
        events.add(event);
        writeFileContent(formatListToJsonArray(events));
        return true;
    }

    @Override
    public boolean update(int id, Event updatedEvent) {
        List<Event> events = findAll();
        boolean found = false;
        
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventId() == id) {
                updatedEvent.setEventId(id);
                events.set(i, updatedEvent);
                found = true;
                break;
            }
        }

        if (found) {
             writeFileContent(formatListToJsonArray(events));
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        List<Event> events = findAll();
        int sizeBefore = events.size();
        
        events.removeIf(e -> e.getEventId() == id);
        
        if (events.size() < sizeBefore) {
            writeFileContent(formatListToJsonArray(events));
            return true;
        }
        return false;
    }
}