package crealtivastudio.view;

import crealtivastudio.model.Customer;
import crealtivastudio.model.Event;
import crealtivastudio.model.Bill;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Scanner;

/**
 * Aplicación principal para Creative Studio
 * Gestión de clientes, eventos y facturas
 * Incluye programación automática y recordatorios de eventos
 * 
 * @author 
 */
public class CreativeStudioApp {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("     CREATIVE STUDIO MANAGEMENT SYSTEM");
        System.out.println("==========================================");
        
        // Cargar datos existentes
        Customer.reloadFromJson();
        Bill.reloadFromJson();

        // 🔔 NUEVO: mostrar recordatorios automáticos
        showUpcomingEventsReminder();

        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            int option = getIntInput("Seleccione una opción: ");
            
            switch (option) {
                case 1 -> manageCustomers();
                case 2 -> manageEvents();
                case 3 -> manageBills();
                case 4 -> generateReports();
                case 5 -> {
                    exit = true;
                    System.out.println("Gracias por usar Creative Studio Management System!");
                }
                default -> System.out.println("Opcion no valida. Intente nuevamente.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Gestion de Clientes");
        System.out.println("2. Gestion de Eventos");
        System.out.println("3. Gestion de Facturas");
        System.out.println("4. Reportes e Informes");
        System.out.println("5. Salir");
    }

    // ==================== FEATURE NUEVO ====================
    // 🔔 Recordatorios automáticos de eventos próximos
    private static void showUpcomingEventsReminder() {
        System.out.println("\n🔔 RECORDATORIOS DE EVENTOS PRÓXIMOS 🔔");
        List<Customer> customers = Customer.getAllCustomers();
        boolean found = false;

        for (Customer c : customers) {
            for (Event e : c.getEvents()) {
                if (isEventSoon(e.getEventDate(), 3)) {
                    System.out.println("Cliente: " + c.getName());
                    System.out.println("Evento: " + e.getEventName() + " (" + e.getEventType() + ")");
                    System.out.println("Fecha: " + e.getEventDate());
                    System.out.println("--------------------------------------");
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No hay eventos próximos en los próximos 3 días.");
        }
    }

    private static boolean isEventSoon(String eventDate, int daysAhead) {
        try {
            LocalDate date = LocalDate.parse(eventDate);
            LocalDate now = LocalDate.now();
            Period diff = Period.between(now, date);
            return !date.isBefore(now) && diff.getDays() <= daysAhead;
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== GESTIÓN DE CLIENTES ====================
    private static void manageCustomers() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- GESTION DE CLIENTES ---");
            System.out.println("1. Registrar nuevo cliente");
            System.out.println("2. Listar todos los clientes");
            System.out.println("3. Buscar cliente por ID");
            System.out.println("4. Buscar cliente por nombre");
            System.out.println("5. Actualizar cliente");
            System.out.println("6. Eliminar cliente");
            System.out.println("7. Ver detalles completos de cliente");
            System.out.println("8. Volver al menu principal");
            
            int option = getIntInput("Seleccione una opcion: ");
            
            switch (option) {
                case 1 -> registerCustomer();
                case 2 -> listAllCustomers();
                case 3 -> findCustomerById();
                case 4 -> findCustomerByName();
                case 5 -> updateCustomer();
                case 6 -> deleteCustomer();
                case 7 -> viewCustomerDetails();
                case 8 -> back = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void registerCustomer() {
        System.out.println("\n--- REGISTRAR NUEVO CLIENTE ---");
        String name = getStringInput("Nombre: ");
        String phone = getStringInput("Telefono (10 digitos): ");
        String email = getStringInput("Email: ");
        String address = getStringInput("Direccion: ");
        
        Customer customer = new Customer(name, phone, email, address);
        if (customer.save()) {
            System.out.println("Cliente registrado exitosamente!");
            System.out.println("ID asignado: " + customer.getId());
        } else {
            System.out.println("Error al registrar cliente. Verifique los datos.");
        }
    }

    private static void listAllCustomers() {
        System.out.println("\n--- LISTA DE CLIENTES ---");
        List<Customer> customers = Customer.getAllCustomers();
        
        if (customers.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer.toSimpleString());
            }
            System.out.println("\nTotal de clientes: " + customers.size());
        }
    }

    private static void findCustomerById() {
        int id = getIntInput("Ingrese el ID del cliente: ");
        Customer customer = Customer.findCustomerById(id);
        
        if (customer != null) {
            customer.displayCustomerInfo();
        } else {
            System.out.println("Cliente no encontrado.");
        }
    }

    private static void findCustomerByName() {
        String name = getStringInput("Ingrese el nombre a buscar: ");
        List<Customer> customers = Customer.findCustomersByName(name);
        
        if (customers.isEmpty()) {
            System.out.println("No se encontraron clientes con ese nombre.");
        } else {
            System.out.println("\n--- RESULTADOS DE BUSQUEDA ---");
            for (Customer customer : customers) {
                System.out.println(customer.toSimpleString());
            }
        }
    }

    private static void updateCustomer() {
        int id = getIntInput("Ingrese el ID del cliente a actualizar: ");
        Customer customer = Customer.findCustomerById(id);
        
        if (customer == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        
        System.out.println("Cliente actual:");
        customer.displayCustomerInfo();
        
        System.out.println("\nIngrese los nuevos datos (deje en blanco para mantener el actual):");
        String newName = getStringInput("Nuevo nombre [" + customer.getName() + "]: ");
        String newPhone = getStringInput("Nuevo telefono [" + customer.getPhone() + "]: ");
        String newEmail = getStringInput("Nuevo email [" + customer.getEmail() + "]: ");
        String newAddress = getStringInput("Nueva direccion [" + customer.getAddress() + "]: ");
        
        if (!newName.isEmpty()) customer.setName(newName);
        if (!newPhone.isEmpty()) customer.setPhone(newPhone);
        if (!newEmail.isEmpty()) customer.setEmail(newEmail);
        if (!newAddress.isEmpty()) customer.setAddress(newAddress);
        
        if (customer.save()) {
            System.out.println("Cliente actualizado exitosamente!");
        } else {
            System.out.println("Error al actualizar cliente.");
        }
    }

    private static void deleteCustomer() {
        int id = getIntInput("Ingrese el ID del cliente a eliminar: ");
        if (Customer.deleteCustomer(id)) {
            System.out.println("Cliente eliminado exitosamente!");
        } else {
            System.out.println("Error: Cliente no encontrado o no se pudo eliminar.");
        }
    }

    private static void viewCustomerDetails() {
        int id = getIntInput("Ingrese el ID del cliente: ");
        Customer customer = Customer.findCustomerById(id);
        
        if (customer != null) {
            customer.displayCustomerInfo();
        } else {
            System.out.println("Cliente no encontrado.");
        }
    }

    // ==================== GESTIÓN DE EVENTOS ====================
    private static void manageEvents() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- GESTION DE EVENTOS ---");
            System.out.println("1. Agregar evento a cliente");
            System.out.println("2. Ver eventos de un cliente");
            System.out.println("3. Eliminar evento");
            System.out.println("4. Volver al menu principal");
            
            int option = getIntInput("Seleccione una opcion: ");
            
            switch (option) {
                case 1 -> addEventToCustomer();
                case 2 -> viewCustomerEvents();
                case 3 -> deleteEvent();
                case 4 -> back = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void addEventToCustomer() {
        System.out.println("\n--- AGREGAR EVENTO A CLIENTE ---");
        int customerId = getIntInput("ID del cliente: ");
        Customer customer = Customer.findCustomerById(customerId);
        
        if (customer == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        
        System.out.println("Tipos de evento disponibles:");
        System.out.println("1. Bodas ($1500.00) - 10% descuento");
        System.out.println("2. Cumpleaños ($800.00)");
        System.out.println("3. Bautizos ($2000.00) - 5% descuento");
        System.out.println("4. Graduaciones ($600.00)");
        
        int eventType = getIntInput("Seleccione el tipo de evento (1-4): ");
        if (eventType < 1 || eventType > 4) {
            System.out.println("Tipo de evento no valido.");
            return;
        }
        
        String eventName = getStringInput("Nombre del evento: ");
        String eventDate = getStringInput("Fecha del evento (YYYY-MM-DD o vacío para automática): ");
        if (eventDate.isEmpty()) eventDate = null;
        
        Event event = customer.addEvent(eventName, eventDate, eventType);
        if (event != null) {
            System.out.println("Evento agregado exitosamente!");
            System.out.println("ID del evento: " + event.getEventId());
            System.out.println("Fecha programada: " + event.getEventDate());
            System.out.println("Precio final: $" + event.calculateFinalPrice());
        } else {
            System.out.println("Error al agregar evento.");
        }
    }

    private static void viewCustomerEvents() {
        int customerId = getIntInput("Ingrese el ID del cliente: ");
        Customer customer = Customer.findCustomerById(customerId);
        
        if (customer != null) {
            customer.displayCustomerInfo();
        } else {
            System.out.println("Cliente no encontrado.");
        }
    }

    private static void deleteEvent() {
        int customerId = getIntInput("ID del cliente: ");
        Customer customer = Customer.findCustomerById(customerId);
        
        if (customer == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        
        int eventId = getIntInput("ID del evento a eliminar: ");
        if (customer.removeEvent(eventId)) {
            System.out.println("Evento eliminado exitosamente!");
        } else {
            System.out.println("Error: Evento no encontrado.");
        }
    }

    // ==================== GESTIÓN DE FACTURAS ====================
    private static void manageBills() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- GESTION DE FACTURAS ---");
            System.out.println("1. Crear nueva factura");
            System.out.println("2. Listar todas las facturas");
            System.out.println("3. Buscar factura por ID");
            System.out.println("4. Buscar facturas por cliente");
            System.out.println("5. Marcar factura como pagada");
            System.out.println("6. Marcar factura como pendiente");
            System.out.println("7. Eliminar factura");
            System.out.println("8. Ver detalles de factura");
            System.out.println("9. Volver al menu principal");
            
            int option = getIntInput("Seleccione una opcion: ");
            
            switch (option) {
                case 1 -> createBill();
                case 2 -> listAllBills();
                case 3 -> findBillById();
                case 4 -> findBillsByCustomer();
                case 5 -> markBillAsPaid();
                case 6 -> markBillAsPending();
                case 7 -> deleteBill();
                case 8 -> viewBillDetails();
                case 9 -> back = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void createBill() {
        System.out.println("\n--- CREAR NUEVA FACTURA ---");
        int customerId = getIntInput("ID del cliente: ");
        Customer customer = Customer.findCustomerById(customerId);
        
        if (customer == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        
        List<Event> events = customer.getEvents();
        if (events.isEmpty()) {
            System.out.println("El cliente no tiene eventos registrados.");
            return;
        }
        
        System.out.println("\nEventos del cliente:");
        for (Event event : events) {
            System.out.println("ID: " + event.getEventId() + " - " + event.getEventName() + 
                             " (" + event.getEventType() + ") - $" + event.calculateFinalPrice());
        }
        
        int eventId = getIntInput("ID del evento para la factura: ");
        Event selectedEvent = customer.getEventById(eventId);
        if (selectedEvent == null) {
            System.out.println("Evento no encontrado.");
            return;
        }
        
        String notes = getStringInput("Notas (opcional): ");
        
        Bill bill = new Bill(customerId, eventId, notes);
        if (bill.save()) {
            System.out.println("Factura creada exitosamente!");
            System.out.println("ID de factura: " + bill.getBillId());
            System.out.println("Monto: $" + bill.getAmount());
            System.out.println("Estado: PENDIENTE");
        } else {
            System.out.println("Error al crear factura.");
        }
    }

    private static void listAllBills() {
        System.out.println("\n--- LISTA DE FACTURAS ---");
        List<Bill> bills = Bill.getAllBills();
        
        if (bills.isEmpty()) {
            System.out.println("No hay facturas registradas.");
        } else {
            for (Bill bill : bills) {
                System.out.println(bill.toString());
            }
            System.out.println("\nTotal de facturas: " + bills.size());
        }
    }

    private static void findBillById() {
        int id = getIntInput("Ingrese el ID de la factura: ");
        Bill bill = Bill.findBillById(id);
        
        if (bill != null) {
            bill.displayBillInfo();
        } else {
            System.out.println("Factura no encontrada.");
        }
    }

    private static void findBillsByCustomer() {
        int customerId = getIntInput("Ingrese el ID del cliente: ");
        List<Bill> bills = Bill.findBillsByCustomer(customerId);
        
        if (bills.isEmpty()) {
            System.out.println("No se encontraron facturas para este cliente.");
        } else {
            System.out.println("\n--- FACTURAS DEL CLIENTE ---");
            for (Bill bill : bills) {
                System.out.println(bill.toString());
            }
        }
    }

    private static void markBillAsPaid() {
        int billId = getIntInput("ID de la factura a marcar como PAGADA: ");
        Bill bill = Bill.findBillById(billId);
        
        if (bill != null) {
            bill.confirmPayment();
            System.out.println("Factura marcada como PAGADA.");
        } else {
            System.out.println("Factura no encontrada.");
        }
    }

    private static void markBillAsPending() {
        int billId = getIntInput("ID de la factura a marcar como PENDIENTE: ");
        Bill bill = Bill.findBillById(billId);
        
        if (bill != null) {
            bill.markAsPending();
            System.out.println("Factura marcada como PENDIENTE.");
        } else {
            System.out.println("Factura no encontrada.");
        }
    }

    private static void deleteBill() {
        int billId = getIntInput("ID de la factura a eliminar: ");
        Bill bill = Bill.findBillById(billId);
        
        if (bill != null && bill.delete()) {
            System.out.println("Factura eliminada exitosamente!");
        } else {
            System.out.println("Error: Factura no encontrada o no se pudo eliminar.");
        }
    }

    private static void viewBillDetails() {
        int billId = getIntInput("Ingrese el ID de la factura: ");
        Bill bill = Bill.findBillById(billId);
        
        if (bill != null) {
            bill.displayBillInfo();
        } else {
            System.out.println("Factura no encontrada.");
        }
    }

    // ==================== REPORTES E INFORMES ====================
    private static void generateReports() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- REPORTES E INFORMES ---");
            System.out.println("1. Facturas pendientes de pago");
            System.out.println("2. Facturas pagadas");
            System.out.println("3. Resumen financiero");
            System.out.println("4. Recordatorios de eventos próximos");
            System.out.println("5. Volver al menu principal");
            
            int option = getIntInput("Seleccione una opcion: ");
            
            switch (option) {
                case 1 -> showPendingBills();
                case 2 -> showPaidBills();
                case 3 -> showFinancialSummary();
                case 4 -> showUpcomingEventsReminder();
                case 5 -> back = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void showPendingBills() {
        System.out.println("\n--- FACTURAS PENDIENTES DE PAGO ---");
        List<Bill> pendingBills = Bill.findPendingBills();
        
        if (pendingBills.isEmpty()) {
            System.out.println("No hay facturas pendientes de pago.");
        } else {
            double totalPending = 0;
            for (Bill bill : pendingBills) {
                System.out.println(bill.showPendingPayment());
                System.out.println("---");
                totalPending += bill.get
