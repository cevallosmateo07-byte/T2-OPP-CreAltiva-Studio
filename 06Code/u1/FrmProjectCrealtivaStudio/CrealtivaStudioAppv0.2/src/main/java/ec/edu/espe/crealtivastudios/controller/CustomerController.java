package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Customer;
import java.util.List;
/**
 *
 * @author Daniel
 */


public class CustomerController {

    public static List<Customer> getAllCustomers() {
        return Customer.getAllCustomers();
    }
    
    
    
    public static String registerCustomer(
            String name,
            String phone,
            String email,
            String address
    ) {

        if (name == null || name.trim().isEmpty()) {
            
            return "El nombre es obligatorio";
        }

        if (phone == null || phone.trim().isEmpty()) {
            
            return "El telefono es obligatorio";
        }

        if (email == null || email.trim().isEmpty()) {
            
            return "El correo es obligatorio";
        }

        
        Customer customer = new Customer(name, phone, email, address);

        
        if (!customer.isValidCustomer()) {
            
            return "Datos invalidos. Verifique correo y telefono";
        }

        boolean saved = customer.save();

        if (!saved) {
            
            return "Error al guardar el cliente";
        }

        return "OK";
    }
}
