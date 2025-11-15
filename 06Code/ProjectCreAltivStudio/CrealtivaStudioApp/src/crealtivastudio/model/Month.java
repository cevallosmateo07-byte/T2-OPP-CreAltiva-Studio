package crealtivastudio.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Mateo Cevallos
 */
public class Month {

    private YearMonth yearMonth;
    private List<Event> events;
    private List<VideoCall> videoCalls;

    public Month(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
        this.events = new ArrayList<>();
        this.videoCalls = new ArrayList<>();
    }

    public Month(int year, int month) {
        this(YearMonth.of(year, month));
    }

    /**
     * Agrega un evento al mes si corresponde a la fecha del mes
     */
    public boolean addEvent(Event event) {
        if (event == null || !isEventInMonth(event)) {
            return false;
        }

        // Evitar duplicados
        if (!events.contains(event)) {
            events.add(event);
            return true;
        }
        return false;
    }

    /**
     * Agrega una videollamada al mes si corresponde a la fecha del mes
     */
    public boolean addVideoCall(VideoCall videoCall) {
        if (videoCall == null || !isVideoCallInMonth(videoCall)) {
            return false;
        }

        // Evitar duplicados
        if (!videoCalls.contains(videoCall)) {
            videoCalls.add(videoCall);
            return true;
        }
        return false;
    }

    public void loadAllFromSystem() {
        loadEventsFromSystem();
        loadVideoCallsFromSystem();
    }

    private void loadEventsFromSystem() {
        this.events.clear();

        List<Customer> allCustomers = Customer.getAllCustomers();
        for (Customer customer : allCustomers) {
            for (Event event : customer.getEvents()) {
                if (isEventInMonth(event)) {
                    events.add(event);
                }
            }
        }
    }

    private void loadVideoCallsFromSystem() {
        this.videoCalls.clear();

        List<VideoCall> allVideoCalls = VideoCall.getAllVideoCalls();
        for (VideoCall videoCall : allVideoCalls) {
            if (isVideoCallInMonth(videoCall)) {
                videoCalls.add(videoCall);
            }
        }
    }

    private boolean isEventInMonth(Event event) {
        try {
            LocalDate eventDate = LocalDate.parse(event.getEventDate());
            return eventDate.getYear() == yearMonth.getYear()
                    && eventDate.getMonth() == yearMonth.getMonth();
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isVideoCallInMonth(VideoCall videoCall) {
        try {
            LocalDate callDate = LocalDate.parse(videoCall.getVideoCallDate());
            return callDate.getYear() == yearMonth.getYear()
                    && callDate.getMonth() == yearMonth.getMonth();
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Obtiene todos los eventos del mes
     */
    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }

    /**
     * Obtiene todas las videollamadas del mes
     */
    public List<VideoCall> getVideoCalls() {
        return new ArrayList<>(videoCalls);
    }

    /**
     * Obtiene eventos por tipo
     */
    public List<Event> getEventsByType(int eventTypeCode) {
        return events.stream()
                .filter(event -> event.getEventTypeCode() == eventTypeCode)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene videollamadas por cliente
     */
    public List<VideoCall> getVideoCallsByCustomer(int customerId) {
        return videoCalls.stream()
                .filter(vc -> vc.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el total de eventos en el mes
     */
    public int getTotalEvents() {
        return events.size();
    }

    /**
     * Obtiene el total de videollamadas en el mes
     */
    public int getTotalVideoCalls() {
        return videoCalls.size();
    }

    /**
     * Obtiene eventos para un día específico del mes
     */
    public List<Event> getEventsByDay(int day) {
        return events.stream()
                .filter(event -> {
                    try {
                        LocalDate eventDate = LocalDate.parse(event.getEventDate());
                        return eventDate.getDayOfMonth() == day;
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Obtiene videollamadas para un día específico del mes
     */
    public List<VideoCall> getVideoCallsByDay(int day) {
        return videoCalls.stream()
                .filter(videoCall -> {
                    try {
                        LocalDate callDate = LocalDate.parse(videoCall.getVideoCallDate());
                        return callDate.getDayOfMonth() == day;
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Genera resumen del mes
     */
    public String getMonthSummary() {
        return String.format(
                "Resumen de %s:\n"
                + "Eventos: %d\n"
                + "Videollamadas: %d\n"
                + "Total de actividades: %d\n",
                getMonthName(),
                getTotalEvents(),
                getTotalVideoCalls(),
                getTotalEvents() + getTotalVideoCalls()
        );
    }

    /**
     * Genera un reporte detallado del mes
     */
    public String getDetailedReport() {
        StringBuilder report = new StringBuilder();
        report.append("═".repeat(50)).append("\n");
        report.append("REPORTE DETALLADO - ").append(getMonthName().toUpperCase()).append("\n");
        report.append("═".repeat(50)).append("\n");

        report.append("\n EVENTOS (").append(getTotalEvents()).append("):\n");
        if (events.isEmpty()) {
            report.append("   No hay eventos programados este mes.\n");
        } else {
            for (Event event : events) {
                Customer customer = findCustomerByEvent(event);
                String customerName = (customer != null) ? customer.getName() : "Cliente no encontrado";
                report.append(String.format("   • %s | %s | Cliente: %s\n",
                        event.getEventDate(), event.getEventName(), customerName));
            }
        }

        report.append("\n VIDEOLIAMADAS (").append(getTotalVideoCalls()).append("):\n");
        if (videoCalls.isEmpty()) {
            report.append("   No hay videollamadas programadas este mes.\n");
        } else {
            for (VideoCall videoCall : videoCalls) {
                report.append("   • ").append(videoCall.getCallInfo()).append("\n");
            }
        }

        report.append("\n RESUMEN FINAL:\n");
        report.append("   Total de actividades: ").append(getTotalEvents() + getTotalVideoCalls()).append("\n");
        report.append("═".repeat(50));

        return report.toString();
    }

    /**
     * Encuentra el cliente dueño de un evento
     */
    private Customer findCustomerByEvent(Event event) {
        for (Customer customer : Customer.getAllCustomers()) {
            if (customer.getEventById(event.getEventId()) != null) {
                return customer;
            }
        }
        return null;
    }

    // --- Métodos de utilidad ---
    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public String getMonthName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return yearMonth.format(formatter);
    }

    public int getYear() {
        return yearMonth.getYear();
    }

    public int getMonthValue() {
        return yearMonth.getMonthValue();
    }

    // --- Métodos estáticos para crear meses ---
    /**
     * Crea un objeto Month para el mes actual
     */
    public static Month getCurrentMonth() {
        Month currentMonth = new Month(YearMonth.now());
        currentMonth.loadAllFromSystem();
        return currentMonth;
    }

    /**
     * Crea un objeto Month para un mes específico
     */
    public static Month getMonth(int year, int month) {
        Month monthObj = new Month(year, month);
        monthObj.loadAllFromSystem();
        return monthObj;
    }

    /**
     * Obtiene todos los meses que tienen actividades
     */
    public static List<Month> getAllMonthsWithActivities() {
        List<Month> monthsWithActivities = new ArrayList<>();

        // Obtener todos los años y meses únicos con eventos o videollamadas
        List<YearMonth> uniqueMonths = new ArrayList<>();

        // De eventos
        for (Customer customer : Customer.getAllCustomers()) {
            for (Event event : customer.getEvents()) {
                try {
                    LocalDate eventDate = LocalDate.parse(event.getEventDate());
                    YearMonth ym = YearMonth.from(eventDate);
                    if (!uniqueMonths.contains(ym)) {
                        uniqueMonths.add(ym);
                    }
                } catch (DateTimeParseException e) {
                    // Ignorar fechas inválidas
                }
            }
        }

        // De videollamadas
        for (VideoCall videoCall : VideoCall.getAllVideoCalls()) {
            try {
                LocalDate callDate = LocalDate.parse(videoCall.getVideoCallDate());
                YearMonth ym = YearMonth.from(callDate);
                if (!uniqueMonths.contains(ym)) {
                    uniqueMonths.add(ym);
                }
            } catch (DateTimeParseException e) {
                // Ignorar fechas inválidas
            }
        }

        // Crear objetos Month para cada mes único
        for (YearMonth ym : uniqueMonths) {
            Month month = new Month(ym);
            month.loadAllFromSystem();
            monthsWithActivities.add(month);
        }

        return monthsWithActivities;
    }

    @Override
    public String toString() {
        return getMonthName() + " - " + getTotalEvents() + " eventos, "
                + getTotalVideoCalls() + " videollamadas";
    }

}
