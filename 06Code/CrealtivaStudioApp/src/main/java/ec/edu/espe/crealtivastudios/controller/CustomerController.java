package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Customer;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import utils.CrudOperations;

/**
 *
 * @author Daniel
 */

public class CustomerController {

    public static List<Customer> getAllCustomers() {
        return Customer.getAllCustomers();
    }

    public static String registerCustomer(String name,String phone,String email,String address) {

        if (!name.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            return "El nombre solo debe contener letras";
        }

        if (phone == null || phone.trim().isEmpty()) {
            return "El telefono es obligatorio";
        }

        if (email == null || email.trim().isEmpty()) {
            return "El correo es obligatorio";
        }

        Customer customer = new Customer(name.trim(),phone.trim(),email.trim(),address != null ? address.trim() : "");

        if (!customer.isValidCustomer()) {
            return "Datos invalidos. Verifique correo y telefono";
        }

        boolean saved = customer.save();

        if (!saved) {
            return "Error al guardar el cliente";
        }
        
        Document doc = new Document()
                
           .append("id", customer.getId())
           .append("name", customer.getName())
           .append("phone", customer.getPhone())
           .append("email", customer.getEmail())
           .append("address", customer.getAddress());

           CrudOperations.insert("customers", doc); 
            
           return "OK";
    }
    
    public static List<Customer> getAllCustomersUnified() {

        List<Customer> result = new ArrayList<>();

        List<Customer> jsonCustomers = Customer.getAllCustomers();
        result.addAll(jsonCustomers);

        List<Document> mongoCustomers = CrudOperations.findAll("customers");

        for (Document doc : mongoCustomers) {
            int id = doc.getInteger("id");

            boolean exists = result.stream()
                    .anyMatch(c -> c.getId() == id);

            if (!exists) {
                Customer c = new Customer(
                        id,
                        doc.getString("name"),
                        doc.getString("phone"),
                        doc.getString("email"),
                        doc.getString("address")
                );
                result.add(c);
            }
        }

        return result;
    }
    
    public static String updateCustomer(
        int id,
        String name,
        String phone,
        String email,
        String address) {

        if (!name.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            return "El nombre solo debe contener letras";
        }

        Customer customer = new Customer(id, name, phone, email, address);

        if (!customer.isValidCustomer()) {
            return "Datos invalidos. Verifique correo y teléfono";
        }

        // Actualizar en JSON
        boolean updatedInJson = customer.save();  // La lógica de "save" debe actualizar en JSON.

        if (!updatedInJson) {
            return "Error al actualizar el cliente en JSON";
        }

        // Actualizar en MongoDB
        Document doc = new Document()
                .append("id", customer.getId())
                .append("name", customer.getName())
                .append("phone", customer.getPhone())
                .append("email", customer.getEmail())
                .append("address", customer.getAddress());

        CrudOperations.upddate("customers", new Document("id", customer.getId()), doc);

        return "OK";
    }
    

    public static String deleteCustomer(int id) {

        if (id <= 0) {
            return "Cliente invalido";
        }

        /* Validacion futura: cliente con eventos
        if (hasAssociatedEvents(id)) {
            return "No se puede eliminar el cliente porque tiene eventos asignados";
        }*/

        // Eliminar del JSON (usando el model existente)
        boolean removedFromJson = Customer.deleteCustomer(id);

        if (!removedFromJson) {
            return "No se pudo eliminar el cliente";
        }

        // Eliminar de MongoDB
        CrudOperations.delete(
                "customers",
                new Document("id", id)
        );

        return "OK";
    }
    
    private static boolean hasAssociatedEvents(int customerId) {

        // FUTURO:
        // Buscar eventos por customerId en Mongo o JSON

        return false;
    }
}
