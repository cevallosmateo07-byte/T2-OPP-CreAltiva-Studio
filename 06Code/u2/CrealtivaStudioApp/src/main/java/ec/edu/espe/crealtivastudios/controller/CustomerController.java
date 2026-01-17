package ec.edu.espe.crealtivastudios.controller;

import com.google.gson.reflect.TypeToken;
import ec.edu.espe.crealtivastudios.model.Customer;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import org.bson.Document;
import utils.CrudOperations;
import utils.JsonOperations;

/**
 *
 * @author Daniel
 */

public class CustomerController {
    
    private static final String JSON_FILE = "customers";
    private static final Type CUSTOMER_LIST_TYPE =new TypeToken<List<Customer>>() {}.getType();
    
    public static List<Customer> getAllCustomers() {
        return JsonOperations.loadListFromFile(JSON_FILE, CUSTOMER_LIST_TYPE);
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
        
        List <Customer> customers= JsonOperations.loadListFromFile(JSON_FILE, CUSTOMER_LIST_TYPE);
        customers.add(customer);
        
        boolean savedJson = JsonOperations.saveListToFile(customers,JSON_FILE);

        if (!savedJson) {
            return "Error al guardar el cliente";
        }
        
        Document doc = new Document()
                
           .append("id", customer.getId())
           .append("name", customer.getName())
           .append("phone", customer.getPhone())
           .append("email", customer.getEmail())
           .append("address", customer.getAddress());

           CrudOperations.insert("Customers", doc); 
            
           return "OK";
    }
    
    public static List<Customer> getAllCustomersUnified() {

        List<Customer> result = JsonOperations.loadListFromFile(JSON_FILE, CUSTOMER_LIST_TYPE);

        List<Document> mongoCustomers = CrudOperations.findAll("Customers");

        for (Document doc : mongoCustomers){
            
            int id = doc.getInteger("id");

            boolean exists = result.stream().anyMatch(c -> c.getId() == id);

            if (!exists) {
                Customer customer = new Customer(
                        id,
                        doc.getString("name"),
                        doc.getString("phone"),
                        doc.getString("email"),
                        doc.getString("address")
                );
                result.add(customer);
            }
        }

        return result;
    }
    
    public static String updateCustomer(int id,String name,String phone,String email,String address) {
        
        if (id <= 0) {
            return "Cliente invalido";
        }
        
        if (!name.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            return "El nombre solo debe contener letras";
        }

        Customer updated = new Customer(id, name, phone, email, address);

        if (!updated.isValidCustomer()) {
            return "Datos invalidos. Verifique correo y teléfono";
        }
        
        List<Customer> customers = JsonOperations.loadListFromFile(JSON_FILE, CUSTOMER_LIST_TYPE);
      
        boolean found = false;
        

        for (Customer c : customers){
            
                if (c.getId() == id) {
                c.setName(updated.getName());
                c.setPhone(updated.getPhone());
                c.setEmail(updated.getEmail());
                c.setAddress(updated.getAddress());
                found = true;
                break;
            }
        }
        if(!found){
            return "Cliente no encontrado";
        }
        boolean savedJson= JsonOperations.saveListToFile(customers, JSON_FILE);
        
        if(!savedJson){
            return "Error al atualizar cliente en JSON";
        }
        
        Document doc = new Document()
                .append("id", updated.getId())
                .append("name", updated.getName())
                .append("phone", updated.getPhone())
                .append("email", updated.getEmail())
                .append("address", updated.getAddress());

        CrudOperations.upddate("Customers", new Document("id", id), doc);

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

            List <Customer> customers = JsonOperations.loadListFromFile(JSON_FILE, CUSTOMER_LIST_TYPE);

            boolean removed = false;

            Iterator<Customer> iterator = customers.iterator();

            while (iterator.hasNext()){
                if (iterator.next().getId()==id){
                    iterator.remove();
                    removed = true;
                    break;
                }
            }

            if (!removed) {
                return "No se pudo econtrar el cliente";
            }
            boolean savedJson = JsonOperations.saveListToFile(customers, JSON_FILE);

            if(!savedJson){
                return "Error al eliminar cliente";
            }

            CrudOperations.delete("Customers",new Document("id", id));
            
            return "OK";
        }
    
    private static boolean hasAssociatedEvents(int customerId) {

        return false;
    }
}
