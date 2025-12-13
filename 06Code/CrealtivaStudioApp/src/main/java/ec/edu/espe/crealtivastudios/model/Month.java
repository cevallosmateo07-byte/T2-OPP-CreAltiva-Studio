package ec.edu.espe.crealtivastudios.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Kevin Chalan, OBJECT MASTER, OOP
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

 
    public boolean addEvent(Event event) {
        if (event == null || !isEventInMonth(event)) {
            return false;
        }

        if (!events.contains(event)) {
            events.add(event);
            return true;
        }
        return false;
    }

    public boolean addVideoCall(VideoCall videoCall) {
        if (videoCall == null || !isVideoCallInMonth(videoCall)) {
            return false;
        }

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


    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }

    public List<VideoCall> getVideoCalls() {
        return new ArrayList<>(videoCalls);
    }

    public List<Event> getEventsByType(int eventTypeCode) {
        return events.stream()
                .filter(event -> event.getEventTypeCode() == eventTypeCode)
                .collect(Collectors.toList());
    }

    public List<VideoCall> getVideoCallsByCustomer(int customerId) {
        return videoCalls.stream()
                .filter(vc -> vc.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }


    public int getTotalEvents() {
        return events.size();
    }


    public int getTotalVideoCalls() {
        return videoCalls.size();
    }


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

   
    private Customer findCustomerByEvent(Event event) {
        for (Customer customer : Customer.getAllCustomers()) {
            if (customer.getEventById(event.getEventId()) != null) {
                return customer;
            }
        }
        return null;
    }

    
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

    public static Month getCurrentMonth() {
        Month currentMonth = new Month(YearMonth.now());
        currentMonth.loadAllFromSystem();
        return currentMonth;
    }

 
    public static Month getMonth(int year, int month) {
        Month monthObj = new Month(year, month);
        monthObj.loadAllFromSystem();
        return monthObj;
    }

   
    public static List<Month> getAllMonthsWithActivities() {
        List<Month> monthsWithActivities = new ArrayList<>();

        
        List<YearMonth> uniqueMonths = new ArrayList<>();

        for (Customer customer : Customer.getAllCustomers()) {
            for (Event event : customer.getEvents()) {
                try {
                    LocalDate eventDate = LocalDate.parse(event.getEventDate());
                    YearMonth ym = YearMonth.from(eventDate);
                    if (!uniqueMonths.contains(ym)) {
                        uniqueMonths.add(ym);
                    }
                } catch (DateTimeParseException e) {
                   
                }
            }
        }

      
        for (VideoCall videoCall : VideoCall.getAllVideoCalls()) {
            try {
                LocalDate callDate = LocalDate.parse(videoCall.getVideoCallDate());
                YearMonth ym = YearMonth.from(callDate);
                if (!uniqueMonths.contains(ym)) {
                    uniqueMonths.add(ym);
                }
            } catch (DateTimeParseException e) {
                
            }
        }

        
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
