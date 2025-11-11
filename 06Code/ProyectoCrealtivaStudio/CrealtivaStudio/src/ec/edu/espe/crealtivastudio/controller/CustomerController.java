


package ec.edu.espe.crealtivastudio.controller;

import ec.edu.espe.crealtivastudio.model.Customer;
import java.util.List;
/**
 *
 * @author Daniel
 * Maneja la lógica de negocio (CRUD) para la gestión de clientes.
 */
public class CustomerController {

    // Se cambia la lista interna por la interfaz DAO (Data Access Object)
    private final CustomerDao customerDao;
    
    public CustomerController() {
        // Inicializa con la implementacion JSON
        this.customerDao = new JsonCustomerDao(); 
    }

    // Metodos CRUD: Ahora solo delegan la accion al DAO
    
    public boolean registerCustomer(Customer customer) {
        return customerDao.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }
    
    public boolean updateCustomerDetails(int customerId, Customer updatedCustomer) {
        return customerDao.update(customerId, updatedCustomer);
    }

    public boolean deleteCustomer(int customerId) {
        return customerDao.delete(customerId);
    }

    public Customer findCustomerById(int customerId) {
        // Se busca en la lista devuelta por el DAO
        return customerDao.findAll().stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .orElse(null);
    }
    
    // --- Metodos de Validacion (Se mantienen igual) ---
    
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }

    public boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^[0-9]{10}$");
    }
}