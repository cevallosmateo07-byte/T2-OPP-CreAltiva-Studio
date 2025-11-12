package crealtivastudio.view;

import crealtivastudio.model.Customer;
import crealtivastudio.model.Event;
import crealtivastudio.model.Bill;
import crealtivastudio.model.Photographer; 
import java.util.List;
import java.util.Scanner;

/**
 * Aplicación principal para Creative Studio
 * @author Daniel, Mateo
 */
public class CreativeStudioApp {
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("      CREATIVE STUDIO MANAGEMENT SYSTEM");
        System.out.println("==========================================");
        
        Customer.reloadFromJson();
        Bill.reloadFromJson();
       
        Photographer.getAllPhotographers();
        
        // Revisar recordatorios automáticos al inicio
        for (Customer c : Customer.getAllCustomers()) {
            c.showUpcomingAppointments();
        }
        for (Bill b : Bill.getAllBills()) {
            b.checkImportantDateAlert();
        }
        
        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            int option = getIntInput("Seleccione una opción: ");
            
            switch (option) {
                case 1 -> manageCustomers();
                case 2 -> manageEvents();
                case 3 -> manageBills();
                case 4 -> generateReportsMenu(); // Modificado: lleva al menú de reportes
                case 5 -> managePhotographers();
                case 6 -> {
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
        System.out.println("5. Gestion de Fotografos");
        System.out.println("6. Salir");
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
        System.out.println("2. Cumpleanos ($800.00)");
        System.out.println("3. Bautizos ($2000.00) - 5% descuento");
        System.out.println("4. Graduaciones ($600.00)");
        
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
        
        // Mostrar eventos del cliente
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
    // Modificado para que sea un submenú
    private static void generateReportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- REPORTES E INFORMES ---");
            System.out.println("1. Informe General del Sistema"); // Nueva opción
            System.out.println("2. Facturas pendientes de pago");
            System.out.println("3. Facturas pagadas");
            System.out.println("4. Resumen financiero");
            System.out.println("5. Volver al menu principal");
            
            int option = getIntInput("Seleccione una opcion: ");
            
            switch (option) {
                case 1 -> generateGeneralSystemReport(); // Llama a la nueva función
                case 2 -> showPendingBills();
                case 3 -> showPaidBills();
                case 4 -> showFinancialSummary();
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
                totalPending += bill.getAmount();
            }
            System.out.println("TOTAL PENDIENTE: $" + String.format("%.2f", totalPending));
            System.out.println("Numero de facturas pendientes: " + pendingBills.size());
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
                System.out.println(bill.getPaymentSummary());
                System.out.println("---");
                totalPaid += bill.getAmount();
            }
            System.out.println("TOTAL PAGADO: $" + String.format("%.2f", totalPaid));
            System.out.println("Numero de facturas pagadas: " + paidBills.size());
        }
    }
    
    private static void showFinancialSummary() {
        System.out.println("\n--- RESUMEN FINANCIERO ---");
        
        List<Customer> customers = Customer.getAllCustomers();
        List<Bill> allBills = Bill.getAllBills();
        List<Bill> paidBills = Bill.findPaidBills();
        List<Bill> pendingBills = Bill.findPendingBills();
        
        double totalRevenue = paidBills.stream().mapToDouble(Bill::getAmount).sum();
        double pendingRevenue = pendingBills.stream().mapToDouble(Bill::getAmount).sum();
        double totalEventValue = customers.stream().mapToDouble(Customer::getTotalEventCost).sum();
        
        System.out.println("ESTADISTICAS GENERALES:");
        System.out.println("• Total de clientes: " + customers.size());
        System.out.println("• Total de eventos: " + customers.stream().mapToInt(Customer::getEventCount).sum());
        System.out.println("• Total de facturas: " + allBills.size());
        System.out.println("• Facturas pagadas: " + paidBills.size());
        System.out.println("• Facturas pendientes: " + pendingBills.size());
        
        System.out.println("\nINFORMACION FINANCIERA:");
        System.out.println("• Ingresos recibidos: $" + String.format("%.2f", totalRevenue));
        System.out.println("• Ingresos pendientes: $" + String.format("%.2f", pendingRevenue));
        System.out.println("• Valor total de eventos: $" + String.format("%.2f", totalEventValue));
        System.out.println("• Porcentaje cobrado: " + 
            (totalEventValue > 0 ? String.format("%.1f", (totalRevenue / totalEventValue) * 100) : "0") + "%");
    }

    // ==================== GESTIÓN DE FOTÓGRAFOS (AGREGADA) ====================
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

    // ==================== REPORTE GENERAL DEL SISTEMA (NUEVA FUNCIÓN) ====================
    private static void generateGeneralSystemReport() {
        System.out.println("\n==========================================");
        System.out.println(" REPORTE GENERAL DEL SISTEMA CREATIVE STUDIO");
        System.out.println("==========================================");

        // --- CLIENTES ---
        List<Customer> customers = Customer.getAllCustomers();
        System.out.println("\n CLIENTES REGISTRADOS: " + customers.size());
        for (Customer c : customers) {
            System.out.println("• " + c.getName() + " (ID: " + c.getId() + ")"); // Agregando ID para referencia
        }

        // --- EVENTOS ---
        int totalEvents = customers.stream().mapToInt(Customer::getEventCount).sum();
        System.out.println("\n🎉 EVENTOS REGISTRADOS: " + totalEvents);
        for (Customer c : customers) {
            for (Event e : c.getEvents()) {
                System.out.println("• " + e.getEventName() + " (ID Evento: " + e.getEventId() + 
                                   ") - Cliente: " + c.getName() + " - Fecha: " + e.getEventDate());
            }
        }

        // --- FACTURAS ---
        List<Bill> allBills = Bill.getAllBills();
        List<Bill> paidBills = Bill.findPaidBills();
        List<Bill> pendingBills = Bill.findPendingBills();

        System.out.println("\n FACTURAS: " + allBills.size());
        System.out.println("     Pagadas: " + paidBills.size());
        System.out.println("     Pendientes: " + pendingBills.size());

        double totalPaid = paidBills.stream().mapToDouble(Bill::getAmount).sum();
        double totalPending = pendingBills.stream().mapToDouble(Bill::getAmount).sum();

        System.out.println("     Total pagado: $" + String.format("%.2f", totalPaid));
        System.out.println("    Total pendiente: $" + String.format("%.2f", totalPending));

        // --- FOTÓGRAFOS ---
        List<Photographer> photographers = Photographer.getAllPhotographers();
        System.out.println("\n FOTÓGRAFOS REGISTRADOS: " + photographers.size());

        int attending = 0;
        for (Photographer p : photographers) {
            // Asumiendo que la clase Photographer tiene un método isAttending()
            if (p.isAttending()) attending++;
            System.out.println("• " + p.getName() + " (ID: " + p.getId() + ") - Evento: " + p.getAssignedEvent() +
                                 " - Asistencia: " + (p.isAttending() ? "Sí" : "No"));
        }
        System.out.println("     Confirmados: " + attending);
        System.out.println("     No asistirán: " + (photographers.size() - attending));

        // --- RESUMEN GLOBAL ---
        double systemTotal = totalPaid + totalPending;
        System.out.println("\n==========================================");
        System.out.println(" RESUMEN GLOBAL");
        System.out.println("Clientes: " + customers.size());
        System.out.println("Eventos: " + totalEvents);
        System.out.println("Facturas: " + allBills.size() + " (Total: $" + String.format("%.2f", systemTotal) + ")");
        System.out.println("Fotógrafos: " + photographers.size());
        System.out.println("==========================================");
    }
    
    // ==================== MÉTODOS UTILITARIOS ====================
    private static int getIntInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un numero valido.");
            }
        }
    }
    
    private static String getStringInput(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }
}