package crealtivastudio.view;

// Importaciones del modelo
import crealtivastudio.model.Customer;
import crealtivastudio.model.Event;
import crealtivastudio.model.Bill;
import crealtivastudio.model.Photographer;
import crealtivastudio.model.Equipment;
import crealtivastudio.model.VideoCall;

import java.util.List;
import java.util.Scanner;

/**
 * Aplicación principal para Creative Studio
 *
 * @author Daniel, Mateo
 */
public class CreativeStudioApp {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("     CREATIVE STUDIO MANAGEMENT SYSTEM");
        System.out.println("==========================================");

        // Cargar todos los datos al iniciar
        Customer.reloadFromJson();
        Bill.reloadFromJson();
        Equipment.reloadFromJson();
        Photographer.reloadFromJson();
        VideoCall.reloadFromJson();
        Event.reloadFromJson();

        // Revisar recordatorios automáticos al inicio
        System.out.println("\n--- ALERTAS DE INICIO (CALENDARIO AUTOMÁTICO) ---");
        boolean hasAlerts = false;
        for (Customer c : Customer.getAllCustomers()) {
            // Este método llama a showUpcomingAppointments() que muestra las alertas
            c.showUpcomingAppointments();
            hasAlerts = true;
        }
        for (Bill b : Bill.getAllBills()) {
            hasAlerts = true;
        }
        if (!hasAlerts) {
            System.out.println("No hay alertas de eventos o facturas importantes hoy.");
        }
        System.out.println("-------------------------------------------------");

        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            int option = getIntInput("Seleccione una opcion: ");

            switch (option) {
                case 1 ->
                    manageCustomers();
                case 2 ->
                    manageEvents();
                case 3 ->
                    manageBills();
                case 4 ->
                    generateReportsMenu();
                case 5 ->
                    managePhotographers();
                case 6 ->
                    manageEquipment();
                case 7 ->
                    manageVideoCalls();
                case 8 ->
                    manageReminders();
                case 9 -> {
                    exit = true;
                    System.out.println("Gracias por usar Creative Studio Management System!");
                }
                default ->
                    System.out.println("Opcion no valida. Intente nuevamente.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Gestion de Clientes");
        System.out.println("2. Gestion de Eventos");
        System.out.println("3. Gestion de Facturas");
        System.out.println("4. Reportes e Informes");
        System.out.println("5. Gestion de Fotografos");
        System.out.println("6. Gestion de Equipamiento");
        System.out.println("7. Gestion de Videollamadas");
        System.out.println("8. Gestión de Calendario/Recordatorios");
        System.out.println("9. Salir");
    }

    // ==========================================================
    // === GESTIÓN DE CALENDARIO/RECORDATORIOS ===
    // ==========================================================
    private static void manageReminders() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- GESTIÓN DE CALENDARIO Y RECORDATORIOS ---");
            System.out.println("1. Ver todos los eventos próximos (Recordatorios Automáticos)");
            System.out.println("2. Agregar Recordatorio Manual a un Evento");
            System.out.println("3. Ver Recordatorios de un Evento Específico");
            System.out.println("4. Volver al menu principal");

            int option = getIntInput("Seleccione una opcion: ");

            switch (option) {
                case 1 ->
                    viewUpcomingEventsAlerts();
                case 2 ->
                    addManualEventReminder();
                case 3 ->
                    viewSpecificEventReminders();
                case 4 ->
                    back = true;
                default ->
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private static void viewUpcomingEventsAlerts() {
        System.out.println("\n--- EVENTOS PRÓXIMOS (DÍAS 0 a 3) ---");
        boolean found = false;
        for (Customer c : Customer.getAllCustomers()) {
            c.showUpcomingAppointments();
            found = true;
        }
        if (!found) {
            System.out.println("No hay eventos próximos registrados en el sistema.");
        }
    }

    private static void addManualEventReminder() {
        System.out.println("\n--- AGREGAR RECORDATORIO MANUAL ---");
        int customerId = getIntInput("ID del cliente dueño del evento: ");
        Customer customer = Customer.findCustomerById(customerId);

        if (customer == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        // Muestra los eventos para que el usuario pueda elegir
        customer.displayCustomerInfo();

        int eventId = getIntInput("ID del evento al que desea añadir el recordatorio: ");
        Event event = customer.getEventById(eventId);

        if (event == null) {
            System.out.println("Error: Evento con ID " + eventId + " no encontrado para este cliente.");
            return;
        }

        String reminderDetails = getStringInput("Escriba el detalle del recordatorio (ej: Llamar a DJ para confirmar setlist): ");

        if (customer.addManualReminderToEvent(eventId, reminderDetails)) {
            System.out.println("🎉 Recordatorio manual añadido exitosamente al evento \"" + event.getEventName() + "\".");
        } else {
            System.out.println("Error al guardar el recordatorio.");
        }
    }

    private static void viewSpecificEventReminders() {
        System.out.println("\n--- VER RECORDATORIOS DE EVENTO ---");
        int customerId = getIntInput("ID del cliente dueño del evento: ");
        Customer customer = Customer.findCustomerById(customerId);

        if (customer == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        customer.displayCustomerInfo();

        int eventId = getIntInput("ID del evento cuyos recordatorios desea ver: ");

        customer.displayEventReminders(eventId);
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
                case 1 ->
                    registerCustomer();
                case 2 ->
                    listAllCustomers();
                case 3 ->
                    findCustomerById();
                case 4 ->
                    findCustomerByName();
                case 5 ->
                    updateCustomer();
                case 6 ->
                    deleteCustomer();
                case 7 ->
                    viewCustomerDetails();
                case 8 ->
                    back = true;
                default ->
                    System.out.println("Opcion no valida.");
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

        if (!newName.isEmpty()) {
            customer.setName(newName);
        }
        if (!newPhone.isEmpty()) {
            customer.setPhone(newPhone);
        }
        if (!newEmail.isEmpty()) {
            customer.setEmail(newEmail);
        }
        if (!newAddress.isEmpty()) {
            customer.setAddress(newAddress);
        }

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
                case 1 ->
                    addEventToCustomer();
                case 2 ->
                    viewCustomerEvents();
                case 3 ->
                    deleteEvent();
                case 4 ->
                    back = true;
                default ->
                    System.out.println("Opcion no valida.");
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
        System.out.println("1. Bodas ($150.00) - 10% descuento");
        System.out.println("2. Cumpleanos ($80.00)");
        System.out.println("3. Bautizos ($20.00) - 5% descuento");
        System.out.println("4. Graduaciones ($60.00)");

        int eventType = getIntInput("Seleccione el tipo de evento (1-4): ");
        if (eventType < 1 || eventType > 4) {
            System.out.println("Tipo de evento no valido.");
            return;
        }

        String eventName = getStringInput("Nombre del evento: ");
        String eventDate = getStringInput("Fecha del evento (YYYY-MM-DD): ");

        Event event = customer.addEvent(eventName, eventDate, eventType);
        if (event != null) {
            System.out.println("Evento agregado exitosamente!");
            System.out.println("ID del evento: " + event.getEventId());
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
                case 1 ->
                    createBill();
                case 2 ->
                    listAllBills();
                case 3 ->
                    findBillById();
                case 4 ->
                    findBillsByCustomer();
                case 5 ->
                    markBillAsPaid();
                case 6 ->
                    markBillAsPending();
                case 7 ->
                    deleteBill();
                case 8 ->
                    viewBillDetails();
                case 9 ->
                    back = true;
                default ->
                    System.out.println("Opcion no valida.");
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
            System.out.println("ID: " + event.getEventId() + " - " + event.getEventName()
                    + " (" + event.getEventType() + ") - $" + event.calculateFinalPrice());
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
    private static void generateReportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- REPORTES E INFORMES ---");
            System.out.println("1. Informe General del Sistema");
            System.out.println("2. Facturas pendientes de pago");
            System.out.println("3. Facturas pagadas");
            System.out.println("4. Volver al menu principal");

            int option = getIntInput("Seleccione una opcion: ");

            switch (option) {
                case 1 ->
                    generateGeneralSystemReport();
                case 2 ->
                    showPendingBills();
                case 3 ->
                    showPaidBills();
                case 4 ->
                    back = true;
                default ->
                    System.out.println("Opcion no valida.");
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
                System.out.println(bill.toString());
                totalPending += bill.getAmount();
            }
            System.out.println("\nTotal pendiente: $" + String.format("%.2f", totalPending));
        }
    }

    private static void showPaidBills() {
        System.out.println("\n--- FACTURAS PAGADAS ---");
        List<Bill> paidBills = Bill.findPaidBills();

        if (paidBills.isEmpty()) {
            System.out.println("No hay facturas pagadas.");
        } else {
            double totalPaid = 0;
            for (Bill bill : paidBills) {
                System.out.println(bill.toString());
                totalPaid += bill.getAmount();
            }
            System.out.println("\nTotal pagado: $" + String.format("%.2f", totalPaid));
        }
    }

    private static void generateGeneralSystemReport() {
        System.out.println("\n--- INFORME GENERAL DEL SISTEMA ---");
        System.out.println("Total Clientes: " + Customer.getAllCustomers().size());

        long totalEvents = Customer.getAllCustomers().stream()
                .mapToLong(Customer::getEventCount)
                .sum();
        System.out.println("Total Eventos Registrados: " + totalEvents);
        System.out.println("Total Facturas Emitidas: " + Bill.getAllBills().size());
        System.out.println("Total Fotógrafos: " + Photographer.getAllPhotographers().size());
        System.out.println("Total Equipo Registrado: " + Equipment.getAllEquipment().size());
        //System.out.println("Ingresos Totales (Pagado): $" + String.format("%.2f", Bill.getTotalRevenue()));
    }

    // ==================== GESTIÓN DE FOTÓGRAFOS ====================
    private static void managePhotographers() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- GESTION DE FOTOGRAFOS ---");
            System.out.println("1. Registrar nuevo fotografo");
            System.out.println("2. Listar todos los fotografos");
            System.out.println("3. Buscar fotografo por ID");
            System.out.println("4. Eliminar fotografo");
            System.out.println("5. Volver al menu principal");
            
            int option = getIntInput("Seleccione una opcion: ");
            
            switch (option) {
                case 1 -> registerPhotographer();
                case 2 -> listAllPhotographers();
                case 3 -> findPhotographerById();
                case 4 -> deletePhotographer();
                case 5 -> back = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }
    
    private static void registerPhotographer() {
        System.out.println("\n--- REGISTRAR NUEVO FOTOGRAFO ---");
        
        int id = getIntInput("ID del fotografo: ");
        String name = getStringInput("Nombre: ");
        // Validacion nombre: solo letras y espacios
        if (!name.matches("[a-zA-ZÁÉÍÓÚáéíóúÑñ ]+")) {
            System.out.println("Nombre invalido. Use solo letras y espacios.");
            return;
        }
        
        String phone = getStringInput("Telefono (opcional, solo digitos): ");
        if (!phone.isEmpty() && !phone.matches("\\d+")) {
            System.out.println("Telefono invalido. Use solo numeros.");
            return;
        }
        
        // Mostrar eventos disponibles para asignacion (tomando eventos existentes)
        List<Customer> customers = Customer.getAllCustomers();
        List<Event> allEvents = customers.stream()
                .flatMap(c -> c.getEvents().stream())
                .toList();
        
        if (allEvents.isEmpty()) {
            System.out.println("No hay eventos registrados. Registre un evento antes de asignar un fotografo.");
            return;
        }
        
        System.out.println("\nEventos disponibles:");
        for (Event e : allEvents) {
            System.out.println("ID: " + e.getEventId() + " - " + e.getEventName() + " (" + e.getEventType() + ")");
        }
        
        int eventId = getIntInput("Ingrese el ID del evento a asignar: ");
        Event selectedEvent = allEvents.stream()
                .filter(e -> e.getEventId() == eventId)
                .findFirst()
                .orElse(null);
        if (selectedEvent == null) {
            System.out.println("Evento no encontrado.");
            return;
        }
        String assignedEvent = selectedEvent.getEventName();
        
        String equipment = getStringInput("Equipos (separados por comas): ");
        
        String confirm = getStringInput("¿Confirma asistencia? (s/n): ");
        boolean attending = confirm.equalsIgnoreCase("s") || confirm.equalsIgnoreCase("si");
        
        Photographer photographer = new Photographer(id, name, assignedEvent, equipment, attending);
        if (photographer.save()) {
            System.out.println("Fotógrafo registrado exitosamente!");
            System.out.println("ID del fotógrafo: " + photographer.getId());
        } else {
            System.out.println("Error al registrar fotógrafo. Verifique el ID (no duplicado) y vuelva a intentar.");
        }
    }
    
    private static void listAllPhotographers() {
        System.out.println("\n--- LISTA DE FOTOGRAFOS ---");
        List<Photographer> photographers = Photographer.getAllPhotographers();
        
        if (photographers.isEmpty()) {
            System.out.println("No hay fotógrafos registrados.");
        } else {
            for (Photographer p : photographers) {
                System.out.println(p.toString());
                System.out.println("---");
            }
            System.out.println("\nTotal de fotógrafos: " + photographers.size());
        }
    }
    
    private static void findPhotographerById() {
        int id = getIntInput("Ingrese el ID del fotógrafo: ");
        Photographer found = Photographer.getAllPhotographers().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
        if (found != null) {
            System.out.println(found.toString());
        } else {
            System.out.println("Fotógrafo no encontrado.");
        }
    }
    
    private static void deletePhotographer() {
        int id = getIntInput("Ingrese el ID del fotógrafo a eliminar: ");
        // Intentamos usar un método estático si existe, si no, intentará fallar de forma controlada.
        try {
            boolean removed = Photographer.deletePhotographer(id); // si tu clase no tiene este método, impleméntalo o reemplaza por tu lógica
            if (removed) {
                System.out.println("Fotógrafo eliminado exitosamente!");
            } else {
                System.out.println("Error: Fotógrafo no encontrado o no se pudo eliminar.");
            }
        } catch (NoSuchMethodError e) {
            // Si la clase Photographer no tiene deletePhotographer, hacemos un fallback:
            List<Photographer> photographers = Photographer.getAllPhotographers();
            boolean removed = photographers.removeIf(p -> p.getId() == id);
            if (removed) {
                // intentamos guardar la lista si Photographer tiene método save() estático o similar,
                // si no existe, informamos al desarrollador que agregue persistencia.
                try {
                    // intentar escribir usando save() de cada fotógrafo reescribiendo archivo (si existe saveToJson en Photographer)
                    // si no existe, notificar:
                    System.out.println("Fotógrafo eliminado de la lista en memoria. Asegúrese de implementar la persistencia en Photographer.deletePhotographer(id).");
                } catch (Exception ex) {
                    System.out.println("Fotógrafo eliminado en memoria, pero no se pudo persistir. Implementa deletePhotographer en Photographer.");
                }
            } else {
                System.out.println("Fotógrafo no encontrado.");
            }
        }
    }


    // ==================== GESTIÓN DE EQUIPAMIENTO ====================
    private static void manageEquipment() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- GESTION DE EQUIPAMIENTO ---");
            System.out.println("1. Registrar nuevo equipo");
            System.out.println("2. Listar todo el equipo");
            System.out.println("3. Volver al menu principal");

            int option = getIntInput("Seleccione una opcion: ");

            switch (option) {
                case 1 ->
                    registerEquipment();
                case 2 ->
                    listAllEquipment();
                case 3 ->
                    back = true;
                default ->
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private static void registerEquipment() {
        System.out.println("\n--- REGISTRAR NUEVO EQUIPO ---");

        String name = getStringInput("Nombre del equipo: ");
        String serial = getStringInput("Número de serie: ");
        String status = getStringInput("Estado (Ej: Disponible, En uso, Reparación): ");

        Equipment equipment = new Equipment(name, serial, status);
        if (equipment.save()) {
            System.out.println("Equipo registrado exitosamente! ID: " + equipment.getId());
        } else {
            System.out.println("Error al registrar equipo. Verifique los datos.");
        }
    }

    private static void listAllEquipment() {
        System.out.println("\n--- LISTA DE EQUIPOS ---");
        List<Equipment> allEquipment = Equipment.getAllEquipment();

        if (allEquipment.isEmpty()) {
            System.out.println("No hay equipos registrados.");
        } else {
            for (Equipment e : allEquipment) {
                System.out.println(e.toSimpleString());
            }
            System.out.println("\nTotal de equipos: " + allEquipment.size());
        }
    }

    // ==================== GESTIÓN DE VIDEOLLAMADAS ====================
    private static void manageVideoCalls() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- GESTION DE VIDEOLLAMADAS ---");
            System.out.println("1. Programar nueva videollamada");
            System.out.println("2. Listar videollamadas");
            System.out.println("3. Volver al menu principal");

            int option = getIntInput("Seleccione una opcion: ");

            switch (option) {
                case 1 ->
                    createVideoCall();
                case 2 ->
                    listVideoCalls();
                case 3 ->
                    back = true;
                default ->
                    System.out.println("Opcion no valida.");
            }
        }
    }

private static void createVideoCall() {
    System.out.println("\n--- PROGRAMAR VIDEOLLAMADA ---");
    int customerId = getIntInput("ID del cliente: ");
    Customer customer = Customer.findCustomerById(customerId);
    
    if (customer == null) {
        System.out.println("Cliente no encontrado.");
        return;
    }
    
    // Muestra eventos del cliente para referencia
    customer.displayCustomerInfo();
    int eventId = getIntInput("ID del evento relacionado: ");
    
    // CORREGIDO: Usar nombres de variables consistentes
    String callDate = getStringInput("Fecha de la videollamada (YYYY-MM-DD): ");
    String medium = getStringInput("Medio (Zoom/Meet/Teams): ");
    
    // CORREGIDO: Constructor con 4 parámetros y condición completa en el if
    VideoCall videoCall = new VideoCall(customerId, eventId, callDate, medium);
    if (videoCall.save()) {
        System.out.println("Videollamada programada exitosamente! ID: " + videoCall.getCallId());
    } else {
        System.out.println("Error al programar videollamada. Verifique los datos.");
    }
}

private static void listVideoCalls() {
    System.out.println("\n--- LISTA DE VIDEOLLAMADAS ---");
    List<VideoCall> calls = VideoCall.getAllVideoCalls();

    if (calls.isEmpty()) {
        System.out.println("No hay videollamadas programadas.");
    } else {
        for (VideoCall call : calls) {
            // CORREGIDO: Usar getCallInfo() que sí existe en tu clase VideoCall
            System.out.println(call.getCallInfo());
        }
        System.out.println("\nTotal de videollamadas: " + calls.size());
    }
}

    // ==================== MÉTODOS DE UTILIDAD ====================
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada invalida. Por favor, ingrese un número entero.");
            scanner.next(); // consumir la entrada inválida
            System.out.print(prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // <<-- ESTO CONSUME EL \n PENDIENTE. ¡CLAVE PARA EL FUNCIONAMIENTO!
        return value;
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

}
