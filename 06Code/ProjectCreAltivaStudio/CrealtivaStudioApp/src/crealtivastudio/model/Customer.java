package crealtivastudio.model;

import crealtivastudio.json.JsonOperations;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.time.LocalDate;
/**
 *
 * @author Daniel
 */
public class Customer {
    private int id; 
    private String name;
    private String phone;
    private String email;
    private String address;
    private List<Event> events;

    // Constantes para archivos JSON
    private static final String CUSTOMERS_FILE = "customers";
    private static final Type CUSTOMER_LIST_TYPE = new TypeToken<List<Customer>>(){}.getType();
    
    // Lista estática para simular base de datos
    private static List<Customer> allCustomers = loadAllFromJson();
    private static int nextCustomerId = calculateNextId();
    private static int nextEventId = calculateNextEventId();

    // Constructor vacío necesario para Gson
    public Customer() {
        this.events = new ArrayList<>();
    }

    public Customer(String name, String phone, String email, String address) {
        this.id = nextCustomerId++;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.events = new ArrayList<>();
    }

    public Customer(int id, String name, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.events = new ArrayList<>();
        updateNextIds();
    }

    // --- MÉTODOS CRUD PARA CUSTOMER ---
    
    public boolean save() {
        if (!isValidCustomer()) {
            System.out.println("Error: Cliente no válido");
            return false;
        }
        
        // Verificar si ya existe (para update)
        Customer existing = findCustomerById(this.id);
        if (existing != null) {
            allCustomers.remove(existing);
        } else {
            // Si es nuevo, asegurar que el ID sea único
            if (this.id == 0) {
                this.id = nextCustomerId++;
            }
        }
        
        allCustomers.add(this);
        return saveAllToJson();
    }
    
    public boolean delete() {
        boolean removed = allCustomers.removeIf(c -> c.getId() == this.id);
        if (removed) {
            saveAllToJson();
            updateNextIds();
        }
        return removed;
    }
    
    public Event addEvent(String eventName, String eventDate, int eventTypeCode) {
        Event event = new Event(nextEventId++, eventName, eventDate, eventTypeCode);
        if (event.isValidEvent()) {
            this.events.add(event);
            saveAllToJson();
            return event;
        }
        System.out.println("Error: Evento no válido");
        return null;
    }
    
    public boolean removeEvent(int eventId) {
        boolean removed = this.events.removeIf(e -> e.getEventId() == eventId);
        if (removed) {
            saveAllToJson();
        }
        return removed;
    }

    // --- MÉTODOS ESTÁTICOS (para operaciones globales) ---
    
    public static List<Customer> getAllCustomers() {
        return new ArrayList<>(allCustomers);
    }
    
    public static Customer findCustomerById(int customerId) {
        return allCustomers.stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .orElse(null);
    }
    
    public static List<Customer> findCustomersByName(String name) {
        return allCustomers.stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }
    
    public static boolean deleteCustomer(int customerId) {
        boolean removed = allCustomers.removeIf(c -> c.getId() == customerId);
        if (removed) {
            saveAllToJson();
            updateNextIds();
        }
        return removed;
    }
    
    public static void reloadFromJson() {
        allCustomers = loadAllFromJson();
        updateNextIds();
    }
    
    // --- MÉTODOS DE VALIDACIÓN ---
    
    public boolean isValidEmail() {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return this.email != null && this.email.matches(emailRegex);
    }

    public boolean isValidPhone() {
        return this.phone != null && this.phone.matches("^[0-9]{10}$");
    }
    
    public boolean isValidCustomer() {
        return this.name != null && !this.name.trim().isEmpty() && 
               isValidEmail() && isValidPhone();
    }
    
    // --- MÉTODOS JSON ---
    
    private static List<Customer> loadAllFromJson() {
        List<Customer> loadedCustomers = JsonOperations.loadListFromFile(CUSTOMERS_FILE, CUSTOMER_LIST_TYPE);
        return loadedCustomers != null ? loadedCustomers : new ArrayList<>();
    }
    
    private static boolean saveAllToJson() {
        return JsonOperations.saveListToFile(allCustomers, CUSTOMERS_FILE);
    }
    
    private static int calculateNextId() {
        if (allCustomers.isEmpty()) {
            return 1;
        }
        return allCustomers.stream()
                .mapToInt(Customer::getId)
                .max()
                .orElse(0) + 1;
    }
    
    private static int calculateNextEventId() {
        int maxEventId = 0;
        for (Customer customer : allCustomers) {
            for (Event event : customer.getEvents()) {
                if (event.getEventId() > maxEventId) {
                    maxEventId = event.getEventId();
                }
            }
        }
        return maxEventId + 1;
    }
    
    private static void updateNextIds() {
        nextCustomerId = calculateNextId();
        nextEventId = calculateNextEventId();
    }
    
    // --- GETTERS Y SETTERS (necesarios para Gson) ---
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<Event> getEvents() { 
        return events != null ? new ArrayList<>(events) : new ArrayList<>(); 
    }
    
    public void setEvents(List<Event> events) { 
        this.events = events != null ? new ArrayList<>(events) : new ArrayList<>(); 
    }
    
    public Event getEventById(int eventId) {
        if (events == null) return null;
        return events.stream()
                .filter(e -> e.getEventId() == eventId)
                .findFirst()
                .orElse(null);
    }

    // --- MÉTODOS DE PRESENTACIÓN ---
    
    public String toSimpleString() {
        String separator = "--------------------------------------";
        return separator + "\n" +
               "| ID:\t\t" + this.getId() + "\n" +
               "| Nombre:\t" + this.getName() + "\n" +
               "| Telefono:\t" + this.getPhone() + "\n" +
               "| Email:\t" + this.getEmail() + "\n" +
               "| Direccion:\t" + this.getAddress() + "\n" +
               "| Eventos:\t" + (this.events != null ? this.events.size() : 0) + "\n" +
               separator;
    }
    
    public void displayCustomerInfo() {
        System.out.println(this.toSimpleString());
        if (events != null && !events.isEmpty()) {
            System.out.println("\nEventos del cliente:");
            for (Event event : events) {
                System.out.println("  - " + event.getEventName() + " (" + event.getEventType() + ")");
                System.out.println("    Fecha: " + event.getEventDate());
                System.out.println("    Precio: $" + event.calculateFinalPrice());
            }
        }
    }
    
    // --- MÉTODOS DE INFORMES ---
    
    public double getTotalEventCost() {
        if (events == null) return 0.0;
        return events.stream()
                .mapToDouble(Event::calculateFinalPrice)
                .sum();
    }
    
    public int getEventCount() {
        return events != null ? events.size() : 0;
    }
    
    public List<Event> getEventsByType(int eventTypeCode) {
        if (events == null) return new ArrayList<>();
        return events.stream()
                .filter(e -> e.getEventTypeCode() == eventTypeCode)
                .toList();
    }
    
    public void showUpcomingAppointments() {
    if (events == null || events.isEmpty()) {
        System.out.println("El cliente " + this.name + " no tiene eventos registrados.");
        return;
    }

    System.out.println("\n📅 Recordatorios para el cliente: " + this.name);
    boolean found = false;

    for (Event e : events) {
        if (e.isUpcoming()) {
            System.out.println("🔔 Evento próximo: " + e.getEventName() + 
                               " (" + e.getEventType() + ") - Fecha: " + e.getEventDate());
            System.out.println(e.scheduleAutomaticAppointment());
            System.out.println("--------------------------------------");
            found = true;
        }
    }

    if (!found) {
        System.out.println("✅ No hay eventos próximos en los próximos 3 días para este cliente.");
    }
}
}