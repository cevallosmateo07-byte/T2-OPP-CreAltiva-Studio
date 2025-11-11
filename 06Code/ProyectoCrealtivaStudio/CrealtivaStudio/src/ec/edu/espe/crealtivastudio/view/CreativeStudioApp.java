


package ec.edu.espe.crealtivastudio.view;

import ec.edu.espe.crealtivastudio.model.Customer;
import ec.edu.espe.crealtivastudio.model.Event; 
import ec.edu.espe.crealtivastudio.controller.CustomerController;
import ec.edu.espe.crealtivastudio.controller.EventController; 
import java.util.Scanner;
import java.nio.charset.StandardCharsets; 
import java.util.List;

/**
 *
 * @author Daniel
 */

public class CreativeStudioApp {

    private static final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name()); 
    private static final CustomerController customerController = new CustomerController();
    private static final EventController eventController = new EventController(); 
    private static int nextCustomerId = 1;
    private static int nextEventId = 1; 

    public static void main(String[] args) {
        int option;

        // Cargar IDs iniciales
        nextCustomerId = getNextAvailableCustomerId(); 
        nextEventId = getNextAvailableEventId(); 

        System.out.println("=========================================");
        System.out.println("   CREATIVE STUDIO - SISTEMA DE GESTION");
        System.out.println("=========================================");

        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Gestion de Clientes"); 
            System.out.println("2. Gestion de Eventos"); 
            System.out.println("3. Gestion de Facturacion (Proximamente)");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opcion: "); 

            option = readIntInput(); 

            switch (option) {
                case 1:
                    handleCustomerMenu();
                    break;
                case 2:
                    handleEventMenu();
                    break;
                case 3:
                    System.out.println("Funcionalidad de Facturacion en desarrollo.");
                    break;
                case 4:
                    System.out.println("\nGracias por usar Creative Studio! Saliendo..."); 
                    break;
                default:
                    System.out.println("Opcion no valida. Intente de nuevo."); 
            }
        } while (option != 4);
        scanner.close();
    }
    
    // ===============================================
    //               UTILITIES
    // ===============================================
    
    private static int getNextAvailableCustomerId() {
        return customerController.getAllCustomers().stream()
                .mapToInt(Customer::getId)
                .max()
                .orElse(0) + 1;
    }
    
    private static int getNextAvailableEventId() { 
        return eventController.getAllEvents().stream()
                .mapToInt(Event::getEventId)
                .max()
                .orElse(0) + 1;
    }
    
    private static int readIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Entrada invalida. Ingrese un numero: ");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); 
        return input;
    }
    
    private static String readStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim(); 
    }
    
    // ===============================================
    //               MENU DE CLIENTES (Op. 1)
    // ===============================================
    
    private static void handleCustomerMenu() {
        int option;
        do {
            System.out.println("\n--- SUBMENU: GESTION DE CLIENTES ---");
            System.out.println("1. Registrar Nuevo Cliente");
            System.out.println("2. Ver Todos los Clientes");
            System.out.println("3. Editar Cliente Existente");
            System.out.println("4. Eliminar Cliente");
            System.out.println("5. Volver al Menu Principal");
            System.out.print("Seleccione una opcion: ");
            
            option = readIntInput();
            
            switch (option) {
                case 1: registerNewCustomer(); break;
                case 2: viewAllCustomers(); break;
                case 3: editCustomer(); break;
                case 4: deleteCustomer(); break;
                case 5: System.out.println("Volviendo al Menu Principal..."); break;
                default: System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (option != 5);
    }

    private static void registerNewCustomer() {
        System.out.println("\n--- REGISTRO DE CLIENTE ---");
        
        String name = readStringInput("Nombre Completo: ");
        String phone;
        String email;
        String address = readStringInput("Direccion: "); 
        
        do {
            phone = readStringInput("Telefono (10 digitos): ");
            if (!customerController.isValidPhone(phone)) {
                System.out.println("ERROR: El telefono debe tener 10 digitos numericos. Vuelva a intentarlo.");
            }
        } while (!customerController.isValidPhone(phone));

        do {
            email = readStringInput("Email: ");
            if (!customerController.isValidEmail(email)) {
                System.out.println("ERROR: El formato del email es invalido. Vuelva a intentarlo.");
            }
        } while (!customerController.isValidEmail(email));

        Customer newCustomer = new Customer(nextCustomerId, name, phone, email, address);
        if (customerController.registerCustomer(newCustomer)) {
            System.out.println("Cliente registrado exitosamente con ID: " + nextCustomerId);
            nextCustomerId++;
        } else {
            System.out.println("Error al registrar el cliente.");
        }
    }

    private static void viewAllCustomers() {
        if (customerController.getAllCustomers().isEmpty()) {
            System.out.println("\n--- No hay clientes registrados. ---");
            return;
        }
        
        System.out.println("\n--- LISTA DE CLIENTES REGISTRADOS ---");
        System.out.println("\nClientes: ");
        customerController.getAllCustomers().forEach(System.out::println);
    }
    
    private static void editCustomer() {
        System.out.print("\nIngrese el ID del cliente a editar: ");
        int idToEdit = readIntInput();
        
        Customer existingCustomer = customerController.findCustomerById(idToEdit);
        if (existingCustomer == null) {
            System.out.println("ERROR: Cliente con ID " + idToEdit + " no encontrado.");
            return;
        }

        System.out.println("\n--- EDITANDO CLIENTE ID " + idToEdit + " ---");
        System.out.println("Deje el campo vacio si no desea modificarlo.");
        
        String newName = readStringInput("Nombre (" + existingCustomer.getName() + "): ");
        String newAddress = readStringInput("Direccion (" + existingCustomer.getAddress() + "): ");
        
        // Validation for Phone
        String newPhone = "";
        boolean phoneValid = false;
        do {
            newPhone = readStringInput("Telefono (" + existingCustomer.getPhone() + "): ");
            if (newPhone.isEmpty()) { phoneValid = true; } 
            else if (customerController.isValidPhone(newPhone)) { phoneValid = true; } 
            else { System.out.println("ERROR: El telefono debe tener 10 digitos numericos. Vuelva a intentarlo."); }
        } while (!phoneValid);
        
        // Validation for Email
        String newEmail = "";
        boolean emailValid = false;
        do {
            newEmail = readStringInput("Email (" + existingCustomer.getEmail() + "): ");
            if (newEmail.isEmpty()) { emailValid = true; } 
            else if (customerController.isValidEmail(newEmail)) { emailValid = true; } 
            else { System.out.println("ERROR: El formato del email es invalido. Vuelva a intentarlo."); }
        } while (!emailValid);
        
        Customer updatedCustomer = new Customer(
            idToEdit,
            newName.isEmpty() ? existingCustomer.getName() : newName,
            newPhone.isEmpty() ? existingCustomer.getPhone() : newPhone,
            newEmail.isEmpty() ? existingCustomer.getEmail() : newEmail,
            newAddress.isEmpty() ? existingCustomer.getAddress() : newAddress
        );
        
        if (customerController.updateCustomerDetails(idToEdit, updatedCustomer)) {
            System.out.println("Cliente ID " + idToEdit + " actualizado con exito.");
        } else {
            System.out.println("Error desconocido al actualizar.");
        }
    }
    
    private static void deleteCustomer() {
        System.out.print("\nIngrese el ID del cliente a eliminar: ");
        int idToDelete = readIntInput();
        
        if (customerController.deleteCustomer(idToDelete)) {
            System.out.println("Cliente ID " + idToDelete + " eliminado exitosamente.");
        } else {
            System.out.println("ERROR: Cliente con ID " + idToDelete + " no encontrado.");
        }
    }
    
    // ===============================================
    //               MENU DE EVENTOS (Op. 2)
    // ===============================================
    
    private static void handleEventMenu() {
        int option;
        do {
            System.out.println("\n--- SUBMENU: GESTION DE EVENTOS ---");
            System.out.println("1. Registrar Nuevo Evento");
            System.out.println("2. Ver Todos los Eventos");
            System.out.println("3. Volver al Menu Principal");
            System.out.print("Seleccione una opcion: ");
            
            option = readIntInput();
            
            switch (option) {
                case 1: registerNewEvent(); break;
                case 2: viewAllEvents(); break;
                case 3: System.out.println("Volviendo al Menu Principal..."); break;
                default: System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (option != 3);
    }
    
    private static void registerNewEvent() {
        System.out.println("\n--- REGISTRO DE NUEVO EVENTO ---");
        
        int customerId;
        Customer associatedCustomer = null;

        // **ASOCIACION CLAVE**: Bucle de validacion para ID de Cliente
        do {
            System.out.print("Ingrese el ID del Cliente asociado: ");
            customerId = readIntInput();
            associatedCustomer = customerController.findCustomerById(customerId);
            
            if (associatedCustomer == null) {
                System.out.println("ERROR: Cliente ID " + customerId + " no existe. Debe registrar un cliente valido.");
                // Opcional: mostrar clientes disponibles
                // viewAllCustomers(); 
            }
        } while (associatedCustomer == null);

        // Si llega aqui, el cliente es valido. Procedemos con los datos del evento.
        
        String eventName = readStringInput("Nombre del Evento (Ej. Boda, XV): ");
        String eventDate = readStringInput("Fecha del Evento (AAAA-MM-DD): ");
        String eventDetails = readStringInput("Detalles (Ej. 4 horas, 2 Locaciones): ");
        
        
        Event newEvent = new Event(
            nextEventId, 
            customerId, // Usamos el ID de cliente validado
            eventName, 
            eventDate, 
            eventDetails
        );

        if (eventController.registerEvent(newEvent)) {
            System.out.println("Evento registrado exitosamente con ID: " + nextEventId + " (Asociado a Cliente ID " + customerId + ").");
            nextEventId++;
        } else {
            // Este error solo deberia ocurrir si el DAO falla o si el ID ya existia
            System.out.println("Error al registrar el evento. Verifique el ID.");
        }
    }
    
    private static void viewAllEvents() {
        if (eventController.getAllEvents().isEmpty()) {
            System.out.println("\n--- No hay eventos registrados. ---");
            return;
        }
        
        System.out.println("\n--- LISTA DE EVENTOS REGISTRADOS ---");
        System.out.println("\nEventos: ");
        eventController.getAllEvents().forEach(System.out::println);
    }
}