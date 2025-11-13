package crealtivastudio.view;

// Importaciones del modelo, incluyendo la nueva clase Equipment
import crealtivastudio.model.Customer;
import crealtivastudio.model.Event;
import crealtivastudio.model.Bill;
import crealtivastudio.model.Photographer;
import crealtivastudio.model.Equipment;
import crealtivastudio.model.VideoCall;

import java.util.ArrayList; // Importado para la selección de equipo
import java.util.List;
import java.util.Scanner;

/**
 * Aplicación principal para Creative Studio
 * @author Daniel
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
        Equipment.reloadFromJson(); // <-- MODIFICACIÓN
        Photographer.reloadFromJson();
        VideoCall.reloadFromJson();// <-- MODIFICACIÓN
        
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
            int option = getIntInput("Seleccione una opcion: ");
            
            switch (option) {
                case 1 -> manageCustomers();
                case 2 -> manageEvents();
                case 3 -> manageBills();
                case 4 -> generateReportsMenu();
                case 5 -> managePhotographers();
                case 6 -> manageEquipment(); // <-- MODIFICACIÓN
                case 7 -> manageVideoCalls();
                case 8 -> { 
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
        System.out.println("6. Gestion de Equipamiento"); // <-- MODIFICACIÓN
        System.out.println("7. Gestion de Videollamadas");
        System.out.println("8. Salir"); // <-- MODIFICACIÓN (número)
    }
    
    // ==================== GESTIÓN DE CLIENTES ====================
    // (Sin cambios, se mantiene el código original)
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
    // (Sin cambios, se mantiene el código original)
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
    // (Sin cambios, se mantiene el código original)
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
    // (Sin cambios el menú, pero generateGeneralSystemReport() se actualiza más abajo)
    private static void generateReportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- REPORTES E INFORMES ---");
            System.out.println("1. Informe General del Sistema");
            System.out.println("2. Facturas pendientes de pago");
            System.out.println("3. Facturas pagadas");
            System.out.println("4. Resumen financiero");
            System.out.println("5. Volver al menu principal");
            
            int option = getIntInput("Seleccione una opcion: ");
            
            switch (option) {
                case 1 -> generateGeneralSystemReport();
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

    // ==================== GESTIÓN DE FOTÓGRAFOS (MODIFICADA) ====================
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
                case 1 -> registerPhotographer(); // <-- MÉTODO REFACTORIZADO
                case 2 -> listAllPhotographers();
                case 3 -> findPhotographerById();
                case 4 -> deletePhotographer(); // <-- MÉTODO REFACTORIZADO
                case 5 -> back = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }
    
    /**
     * REFACTORIZADO: Ahora usa el inventario de Equipment y asigna por ID.
     */
    private static void registerPhotographer() {
        System.out.println("\n--- REGISTRAR NUEVO FOTOGRAFO ---");

        int id = getIntInput("ID del fotografo (debe ser unico): ");
        // Verificar si el ID ya existe
        if (Photographer.findById(id) != null) {
            System.out.println("Error: Ya existe un fotógrafo con el ID " + id);
            return;
        }
        
        String name = getStringInput("Nombre: ");
        if (!name.matches("[a-zA-ZÁÉÍÓÚáéíóúÑñ ]+")) {
            System.out.println("Nombre invalido. Use solo letras y espacios.");
            return;
        }
        
        String phone = getStringInput("Telefono (10 digitos): ");
        if (!phone.matches("\\d{10}")) {
            System.out.println("Telefono invalido. Use 10 numeros.");
            return;
        }

        // --- SELECCIÓN DE EVENTO ---
        System.out.println("\n--- Asignar Evento ---");
        List<Customer> customers = Customer.getAllCustomers();
        List<Event> allEvents = customers.stream()
                .flatMap(c -> c.getEvents().stream())
                .toList();

        if (allEvents.isEmpty()) {
            System.out.println("No hay eventos registrados. Registre un evento antes de asignar un fotografo.");
            return;
        }

        System.out.println("Eventos disponibles:");
        for (Event e : allEvents) {
            System.out.println("ID: " + e.getEventId() + " - " + e.getEventName() + " (" + e.getEventType() + ")");
        }

        int eventId = getIntInput("Ingrese el ID del evento a asignar: ");
        // Aquí asumimos que tienes el método Customer.findEventById(id) como te indiqué
        Event selectedEvent = Customer.findEventById(eventId); 
        
        if (selectedEvent == null) {
            System.out.println("Evento no encontrado.");
            return;
        }

        // --- SELECCIÓN DE EQUIPO ---
        System.out.println("\n--- Asignar Equipamiento ---");
        List<Equipment> availableEquipment = Equipment.findByStatus(Equipment.STATUS_AVAILABLE);
        List<Integer> selectedEquipmentIds = new ArrayList<>();

        if (availableEquipment.isEmpty()) {
            System.out.println("No hay equipamiento disponible en este momento.");
        } else {
            boolean addMoreEquipment = true;
            while (addMoreEquipment) {
                System.out.println("\nEquipamiento Disponible:");
                for (Equipment item : availableEquipment) {
                    System.out.println("ID: " + item.getEquipmentId() + " - " + item.getName());
                }
                System.out.println("0 - Terminar seleccion");
                
                int equipmentId = getIntInput("Ingrese el ID del equipo a asignar (0 para terminar): ");
                if (equipmentId == 0) {
                    addMoreEquipment = false;
                } else {
                    Equipment selectedItem = Equipment.findById(equipmentId);
                    if (selectedItem != null && selectedItem.isAvailable()) {
                        // Marcar como en uso y añadir a la lista
                        selectedItem.markAsInUse(); // Esto guarda el estado del equipo
                        selectedEquipmentIds.add(selectedItem.getEquipmentId());
                        // Quitar de la lista de disponibles para no seleccionarlo de nuevo
                        availableEquipment.remove(selectedItem); 
                        System.out.println(selectedItem.getName() + " asignado.");
                    } else {
                        System.out.println("ID de equipo no valido o no disponible.");
                    }
                }
            }
        }
        
        System.out.println("Equipos seleccionados: " + selectedEquipmentIds.size());

        // --- CONFIRMACIÓN ---
        String confirm = getStringInput("¿Confirma asistencia? (s/n): ");
        boolean attending = confirm.equalsIgnoreCase("s") || confirm.equalsIgnoreCase("si");

        // --- GUARDAR FOTÓGRAFO ---
        Photographer photographer = new Photographer(id, name, phone, eventId, selectedEquipmentIds, attending);
        if (photographer.save()) {
            System.out.println("¡Fotógrafo registrado y asignado exitosamente!");
            System.out.println(photographer.toString()); // Muestra el resumen
        } else {
            System.out.println("Error al registrar fotógrafo. Verifique los datos.");
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
        Photographer found = Photographer.findById(id); // Usa el método estático
        if (found != null) {
            System.out.println(found.toString());
        } else {
            System.out.println("Fotógrafo no encontrado.");
        }
    }
    
    /**
     * REFACTORIZADO: Ahora usa el método estático que libera el equipo.
     */
    private static void deletePhotographer() {
        int id = getIntInput("Ingrese el ID del fotógrafo a eliminar: ");
        
        // El nuevo método estático Photographer.deletePhotographer(id)
        // se encarga de encontrar al fotógrafo, liberar su equipo y luego eliminarlo.
        
        if (Photographer.deletePhotographer(id)) {
            System.out.println("¡Fotógrafo eliminado exitosamente!");
            System.out.println("El equipo que tenía asignado ha sido marcado como 'Disponible'.");
        } else {
            System.out.println("Error: Fotógrafo no encontrado o no se pudo eliminar.");
        }
    }

    // ==================== REPORTE GENERAL DEL SISTEMA (MODIFICADO) ====================
    /**
     * MODIFICADO: Actualizado para usar p.getAssignedEventName()
     */
    private static void generateGeneralSystemReport() {
        System.out.println("\n==========================================");
        System.out.println(" REPORTE GENERAL DEL SISTEMA CREATIVE STUDIO");
        System.out.println("==========================================");

        // --- CLIENTES ---
        List<Customer> customers = Customer.getAllCustomers();
        System.out.println("\n CLIENTES REGISTRADOS: " + customers.size());
        for (Customer c : customers) {
            System.out.println("• " + c.getName() + " (ID: " + c.getId() + ")");
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
        System.out.println("     Total pendiente: $" + String.format("%.2f", totalPending));

        // --- FOTÓGRAFOS ---
        List<Photographer> photographers = Photographer.getAllPhotographers();
        System.out.println("\n FOTÓGRAFOS REGISTRADOS: " + photographers.size());

        int attending = 0;
        for (Photographer p : photographers) {
            if (p.isAttending()) attending++;
            // MODIFICACIÓN: Se usa getAssignedEventName() en lugar de getAssignedEvent()
            System.out.println("• " + p.getName() + " (ID: " + p.getId() + ") - Evento: " + p.getAssignedEventName() +
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
    
    // ==================== GESTIÓN DE EQUIPAMIENTO (NUEVO) ====================
    // (Estos 5 métodos son nuevos)

    private static void manageEquipment() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- GESTION DE EQUIPAMIENTO ---");
            System.out.println("1. Registrar nuevo equipo");
            System.out.println("2. Listar todo el equipamiento");
            System.out.println("3. Actualizar estado de equipo");
            System.out.println("4. Eliminar equipo");
            System.out.println("5. Volver al menu principal");
            
            int option = getIntInput("Seleccione una opcion: ");
            
            switch (option) {
                case 1 -> registerEquipment();
                case 2 -> listAllEquipment();
                case 3 -> updateEquipmentStatus();
                case 4 -> deleteEquipment();
                case 5 -> back = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void registerEquipment() {
        System.out.println("\n--- REGISTRAR NUEVO EQUIPO ---");
        String name = getStringInput("Nombre del equipo (ej. Canon R5): ");
        String description = getStringInput("Descripcion (ej. Camara mirrorless): ");

        if (name.isEmpty()) {
            System.out.println("El nombre es obligatorio.");
            return;
        }

        Equipment newEquipment = new Equipment(name, description);
        if (newEquipment.save()) {
            System.out.println("¡Equipo registrado exitosamente! ID: " + newEquipment.getEquipmentId());
        } else {
            System.out.println("Error al guardar el equipo.");
        }
    }

    private static void listAllEquipment() {
        System.out.println("\n--- INVENTARIO DE EQUIPAMIENTO ---");
        List<Equipment> allEquipment = Equipment.getAllEquipment();

        if (allEquipment.isEmpty()) {
            System.out.println("No hay equipamiento registrado en el inventario.");
            return;
        }

        for (Equipment item : allEquipment) {
            System.out.println(item.toString());
        }
        System.out.println("\nTotal de items: " + allEquipment.size());
    }

    private static void updateEquipmentStatus() {
        System.out.println("\n--- ACTUALIZAR ESTADO DE EQUIPO ---");
        int equipmentId = getIntInput("Ingrese el ID del equipo: ");
        Equipment item = Equipment.findById(equipmentId);

        if (item == null) {
            System.out.println("Equipo no encontrado.");
            return;
        }

        System.out.println("Equipo actual: " + item.toSimpleString());
        System.out.println("Seleccione el nuevo estado:");
        System.out.println("1. " + Equipment.STATUS_AVAILABLE);
        System.out.println("2. " + Equipment.STATUS_IN_USE);
        System.out.println("3. " + Equipment.STATUS_MAINTENANCE);
        
        int option = getIntInput("Opcion: ");
        switch (option) {
            case 1 -> {
                item.markAsAvailable();
                System.out.println("Estado actualizado a: " + Equipment.STATUS_AVAILABLE);
            }
            case 2 -> {
                item.markAsInUse();
                System.out.println("Estado actualizado a: " + Equipment.STATUS_IN_USE);
            }
            case 3 -> {
                item.markAsMaintenance();
                System.out.println("Estado actualizado a: " + Equipment.STATUS_MAINTENANCE);
            }
            default -> System.out.println("Opcion no valida.");
        }
    }

    private static void deleteEquipment() {
        System.out.println("\n--- ELIMINAR EQUIPO ---");
        int equipmentId = getIntInput("Ingrese el ID del equipo a eliminar: ");
        Equipment item = Equipment.findById(equipmentId);

        if (item == null) {
            System.out.println("Equipo no encontrado.");
            return;
        }
        
        if (!item.isAvailable()) {
             System.out.println("Error: El equipo esta " + item.getStatus() + ". No se puede eliminar.");
             System.out.println("Marque el equipo como 'Disponible' antes de eliminarlo.");
             return;
        }

        if (item.delete()) {
            System.out.println("¡Equipo eliminado exitosamente!");
        } else {
            System.out.println("Error al eliminar el equipo.");
        }
    }
   // ==================== GESTIÓN DE VIDEOLLAMADAS ====================

private static void manageVideoCalls() {
    boolean back = false;
    while (!back) {
        System.out.println("\n--- GESTION DE VIDEOLLAMADAS ---");
        System.out.println("1. Programar nueva videollamada");
        System.out.println("2. Listar todas las videollamadas");
        System.out.println("3. Buscar videollamadas por cliente");
        System.out.println("4. Eliminar videollamada");
        System.out.println("5. Volver al menu principal");
        
        int option = getIntInput("Seleccione una opcion: ");
        
        switch (option) {
            case 1 -> scheduleVideoCall();
            case 2 -> listAllVideoCalls();
            case 3 -> findVideoCallsByCustomer();
            case 4 -> deleteVideoCall();
            case 5 -> back = true;
            default -> System.out.println("Opción no valida.");
        }
    }
}

private static void scheduleVideoCall() {
    System.out.println("\n--- PROGRAMAR NUEVA VIDEOLLAMADA ---");
    
    
    int customerId = getIntInput("ID del cliente: ");
    Customer customer = Customer.findCustomerById(customerId);
    if (customer == null) {
        System.out.println(" Cliente no encontrado.");
        return;
    }
    
   
    List<Event> events = customer.getEvents();
    if (events.isEmpty()) {
        System.out.println(" El cliente no tiene eventos. Registre un evento primero.");
        return;
    }
    
    System.out.println("\nEventos del cliente " + customer.getName() + " (Obligatorio seleccionar uno):");
    for (Event event : events) {
        System.out.println("ID: " + event.getEventId() + " - " + event.getEventName() + 
                           " (Fecha: " + event.getEventDate() + ")");
    }
    
    int eventId = 0;
    Event selectedEvent = null;
    
    
    while (selectedEvent == null) {
        eventId = getIntInput("Ingrese el ID del evento asociado: ");
        selectedEvent = customer.getEventById(eventId);
        
        if (selectedEvent == null) {
            System.out.println("️ ID de evento no válido o no pertenece a este cliente. Intente de nuevo.");
        }
    }

    
    String medium = getStringInput("Medio de la llamada (Zoom/Meet): ");
    String callDate = getStringInput("Fecha de la videollamada (YYYY-MM-DD): ");
    

    VideoCall call = new VideoCall(customerId, eventId, callDate, medium);
    
    System.out.println("\nIntentando guardar la videollamada...");
    if (call.save()) {
        System.out.println(" ¡Videollamada programada exitosamente!");
        System.out.println("ID asignado: " + call.getCallId());
    } else {
        System.out.println(" Error al programar la videollamada. Verifique que la fecha sea ANTES del evento.");
    }
}
private static void listAllVideoCalls() {
    System.out.println("\n--- LISTA DE TODAS LAS VIDEOLLAMADAS ---");
    List<VideoCall> calls = VideoCall.getAllVideoCalls();
    
    if (calls.isEmpty()) {
        System.out.println("No hay videollamadas registradas.");
    } else {
        for (VideoCall call : calls) {
            System.out.println(call.getCallInfo());
            System.out.println("---");
        }
        System.out.println("\nTotal de videollamadas: " + calls.size());
    }
}

private static void findVideoCallsByCustomer() {
    int customerId = getIntInput("Ingrese el ID del cliente: ");
    List<VideoCall> calls = VideoCall.findByCustomer(customerId);
    
    if (calls.isEmpty()) {
        System.out.println("No se encontraron videollamadas para el cliente ID " + customerId + ".");
    } else {
        System.out.println("\n--- VIDEOLLAMADAS DEL CLIENTE ID " + customerId + " ---");
        for (VideoCall call : calls) {
            System.out.println(call.getCallInfo());
            System.out.println("---");
        }
    }
}

private static void deleteVideoCall() {
    int callId = getIntInput("ID de la videollamada a eliminar: ");
    VideoCall call = VideoCall.findById(callId);
    
    if (call != null && call.delete()) {
        System.out.println(" Videollamada eliminada exitosamente!");
    } else {
        System.out.println(" Error: Videollamada no encontrada o no se pudo eliminar.");
    }
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