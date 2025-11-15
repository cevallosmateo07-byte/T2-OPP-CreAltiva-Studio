package crealtivastudio.model;

/**
 *
 * @author Object Master, ESPE
 */
import crealtivastudio.json.JsonOperations;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private int eventId;
    private String eventName;
    private String eventDate;
    private String eventType;
    private int eventTypeCode;
    private double basePrice;
    private int customerId; // Asumo que Event necesita un customerId para ser usado con VideoCall
    
    // ==========================================================
    // === ADICIÓN PARA CALENDARIO/RECORDATORIOS ===
    // ==========================================================
    private List<String> reminders; // <-- Nuevo campo para almacenar recordatorios manuales
    
    // --- ADICIONES PARA LA PERSISTENCIA Y CALENDARIO ---
    
    // Constantes para JSON: Archivo INDEPENDIENTE para Eventos
    private static final String EVENTS_FILE = "events"; 
    private static final Type EVENT_LIST_TYPE = new TypeToken<List<Event>>(){}.getType();
    
    private static List<Event> allEvents = loadAllFromJson();
    
    // Método estático para cargar todos los eventos desde el JSON
    private static List<Event> loadAllFromJson() {
        List<Event> loadedEvents = JsonOperations.loadListFromFile(EVENTS_FILE, EVENT_LIST_TYPE);
        return (loadedEvents != null) ? loadedEvents : new ArrayList<>();
    }
    
    // Método estático necesario para que el Calendar acceda a todos los Eventos
    public static List<Event> getAllEvents() {
        return new ArrayList<>(allEvents);
    }
    
    // Método estático para recargar (si es necesario)
    public static void reloadFromJson() {
        allEvents = loadAllFromJson();
    }
    
    // Método estático para guardar (si es necesario, aunque el save() de instancia debería manejar esto)
    public static boolean saveAllToJson() {
        return JsonOperations.saveListToFile(allEvents, EVENTS_FILE);
    }
    
    // --- FIN DE ADICIONES ---

    // Constantes para los tipos de evento
    public static final int BODAS = 1;
    public static final int CUMPLEANOS = 2;
    public static final int BAUTIZOS = 3;
    public static final int GRADUACIONES = 4;

    // Constructor vacío necesario para Gson
    public Event() {
        this.reminders = new ArrayList<>(); // Inicializar lista en constructor vacío
    }

    // He modificado el constructor para incluir customerId (necesario para la referencia en VideoCall)
    // Si tu constructor original no lo tenía, deberías ajustarlo en tu código principal.
    public Event(int eventId, String eventName, String eventDate, int eventTypeCode, int customerId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTypeCode = eventTypeCode;
        this.customerId = customerId;
        this.eventType = getEventTypeString(eventTypeCode);
        this.basePrice = calculateBasePrice();
        this.reminders = new ArrayList<>(); // Inicializar lista en constructor completo
    }
    
    // Constructor original sin customerId (si necesitas mantener compatibilidad)
    public Event(int eventId, String eventName, String eventDate, int eventTypeCode) {
        this(eventId, eventName, eventDate, eventTypeCode, 0); // Asigna 0 o un valor por defecto
    }

    // Método para calcular precio base según tipo de evento
    private double calculateBasePrice() {
        switch (this.eventTypeCode) {
            case BODAS: return 150.00;
            case CUMPLEANOS: return 80.00;
            case BAUTIZOS: return 20.00;
            case GRADUACIONES: return 60.00;
            default: return 50.00;
        }
    }
    
    // Método para aplicar descuentos
    public double calculateFinalPrice() {
        double finalPrice = this.basePrice;
        
        // Descuento para bodas
        if (this.eventTypeCode == BODAS) {
            finalPrice *= 0.9; // 10% de descuento
        }
        
        // Descuento para bautizos
        if (this.eventTypeCode == BAUTIZOS) {
            finalPrice *= 0.95; // 5% de descuento
        }
        
        return finalPrice;
    }
    
    public static String getEventTypeString(int eventTypeCode) {
        switch (eventTypeCode) {
            case BODAS: return "Bodas";
            case CUMPLEANOS: return "Cumpleaños";
            case BAUTIZOS: return "Bautizos";
            case GRADUACIONES: return "Graduaciones";
            default: return "Otro";
        }
    }
    
    public boolean isValidEvent() {
        return this.eventName != null && !this.eventName.trim().isEmpty() &&
                this.eventDate != null && !this.eventDate.trim().isEmpty() &&
                this.eventTypeCode >= 1 && this.eventTypeCode <= 4;
    }

    // Getters y Setters (necesarios para Gson)
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public int getEventTypeCode() { return eventTypeCode; }
    public void setEventTypeCode(int eventTypeCode) { 
        this.eventTypeCode = eventTypeCode;
        this.eventType = getEventTypeString(eventTypeCode);
        this.basePrice = calculateBasePrice();
    }

    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    
    // Getter y Setter para customerId
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    // ==========================================================
    // === MÉTODOS DE CALENDARIO/RECORDATORIOS ADICIONALES ===
    // ==========================================================
    
    /**
     * Añade un recordatorio manual o automático a la lista de recordatorios del evento.
     * @param reminder El detalle del recordatorio.
     */
    public void addReminder(String reminder) {
        if (this.reminders == null) {
            this.reminders = new ArrayList<>();
        }
        this.reminders.add(reminder);
    }
    
    /**
     * Obtiene todos los recordatorios (manuales y automáticos) para el evento.
     * @return Lista de Strings con los recordatorios.
     */
    public List<String> getReminders() {
        if (this.reminders == null) {
            this.reminders = new ArrayList<>();
        }
        
        // Lógica: Si el evento es próximo, se añade el recordatorio automático temporalmente
        // Esto evita guardar el recordatorio automático cada vez, ya que se calcula dinámicamente.
        List<String> currentReminders = new ArrayList<>(this.reminders);
        
        // Agregar recordatorio automático de cita 1 semana antes (si no está ya incluido)
        String automaticAppt = scheduleAutomaticAppointment();
        if (!currentReminders.contains(automaticAppt) && automaticAppt.startsWith("✅")) {
            currentReminders.add(0, automaticAppt); // Añadir al inicio
        }
        
        return currentReminders;
    }

    
    // El resto de los métodos se mantienen exactamente igual
    public String scheduleAutomaticAppointment() {
    try {
        LocalDate eventDate = LocalDate.parse(this.eventDate);
        LocalDate appointmentDate = eventDate.minusDays(7); // cita 1 semana antes del evento
        return "✅ Cita automática programada para el " + 
               appointmentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
               " (1 semana antes del evento \"" + this.eventName + "\")";
    } catch (DateTimeParseException e) {
        return "⚠️ Error: formato de fecha inválido para el evento " + this.eventName;
    }
}

public boolean isUpcoming() {
    try {
        LocalDate eventDate = LocalDate.parse(this.eventDate);
        LocalDate today = LocalDate.now();
        // Evento próximo es hoy o en los próximos 2 días (total 3 días incluyendo hoy)
        return !eventDate.isBefore(today) && (eventDate.isBefore(today.plusDays(3)) || eventDate.isEqual(today.plusDays(3)));
    } catch (Exception e) {
        return false;
    }
}

    // He modificado el save() para usar la lógica de persistencia
    public boolean save() {
        // Asumiendo que quieres guardar o actualizar el evento en la lista estática y en el JSON
        Event existing = allEvents.stream()
            .filter(e -> e.getEventId() == this.eventId)
            .findFirst().orElse(null);
            
        if (existing != null) {
            allEvents.remove(existing);
        } else if (this.eventId == 0) {
            // Asumiendo que tienes un nextEventId estático similar a VideoCall
            // Por simplicidad, si es nuevo, deberías asignarle un ID aquí.
            // Si tu sistema de IDs lo maneja en otro lado, deja el ID original.
        }
        
        allEvents.add(this);
        return saveAllToJson(); 
    }
    
}