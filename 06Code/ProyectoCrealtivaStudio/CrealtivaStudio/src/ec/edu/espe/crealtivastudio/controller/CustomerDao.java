
package ec.edu.espe.crealtivastudio.controller;

/**
 *
 * @author Daniel
 */

import ec.edu.espe.crealtivastudio.model.Customer;
import java.util.List;

public interface CustomerDao {
    boolean save(Customer customer);
    List<Customer> findAll();
    boolean update(int id, Customer customer);
    boolean delete(int id);
}