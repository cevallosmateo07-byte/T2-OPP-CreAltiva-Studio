package crealtivastudio.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase Calendar que organiza automáticamente los meses,
 * eventos y videollamadas con clientes.
 * 
 * No requiere agregar datos manualmente, usa los registros
 * cargados desde Event y VideoCall.
 */
public class Calendar {

    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * Genera un mapa con todos los meses que contienen eventos o videollamadas.
     */
    public Map<String, List<String>> getFullSchedule() {
        Map<String, List<String>> schedule = new HashMap<>();

      
        for (Event event : Event.getAllEvents()) {
            try {
                LocalDate date = LocalDate.parse(event.getEventDate());
                String monthKey = date.format(MONTH_FORMAT);

                String info = String.format(
                    " [EVENTO] %s | Tipo: %s | Cliente ID: %d | Fecha: %s",
                    event.getEventName(),
                    event.getEventType(),
                    event.getCustomerId(),
                    event.getEventDate()
                );

                schedule.computeIfAbsent(monthKey, k -> new ArrayList<>()).add(info);

            } catch (DateTimeParseException e) {
                System.err.println("️ Fecha de evento inválida: " + event.getEventDate());
            }
        }

        
        for (VideoCall call : VideoCall.getAllVideoCalls()) {
            try {
                LocalDate date = LocalDate.parse(call.getVideoCallDate());
                String monthKey = date.format(MONTH_FORMAT);

                String info = String.format(
                    "💻 [VIDEOLLAMADA] Cliente ID: %d | Fecha: %s | %s",
                    call.getCustomerId(),
                    call.getVideoCallDate(),
                    call.getCallInfo()
                );

                schedule.computeIfAbsent(monthKey, k -> new ArrayList<>()).add(info);

            } catch (DateTimeParseException e) {
                System.err.println("⚠️ Fecha de videollamada inválida: " + call.getVideoCallDate());
            }
        }

        
        for (List<String> items : schedule.values()) {
            Collections.sort(items);
        }

        return schedule;
    }

    /**
     * Imprime los eventos y videollamadas de un mes específico.
     * @param yearMonth Formato "yyyy-MM", ejemplo: "2025-11"
     */
    public void printMonthlySchedule(String yearMonth) {
        Map<String, List<String>> schedule = getFullSchedule();
        List<String> monthItems = schedule.getOrDefault(yearMonth, Collections.emptyList());

        System.out.println("\n🗓️ --- Agenda del mes " + yearMonth + " ---");
        if (monthItems.isEmpty()) {
            System.out.println(" No hay eventos ni videollamadas registradas para este mes.");
        } else {
            monthItems.forEach(System.out::println);
        }
        System.out.println("--------------------------------------");
    }

    /**
     * Imprime todo el calendario con todos los meses registrados.
     */
    public void printFullCalendar() {
        Map<String, List<String>> schedule = getFullSchedule();
        if (schedule.isEmpty()) {
            System.out.println("📭 No hay ningún evento ni videollamada registrada.");
            return;
        }

        List<String> sortedMonths = schedule.keySet().stream().sorted().collect(Collectors.toList());
        for (String month : sortedMonths) {
            printMonthlySchedule(month);
        }
    }
}
