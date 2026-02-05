package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Customer;
import java.awt.Component;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Chicaiza Objet Maseters
 */
public class CustomerControllerTest {

    @Test
    public void testGetTableModel() {
        System.out.println("getTableModel");
        CustomerController instance = new CustomerController();
        
        DefaultTableModel result = instance.getTableModel();
        
        assertNotNull(result);

        assertEquals(5, result.getColumnCount());
        assertFalse(result.isCellEditable(0, 0), "Las celdas no deben ser editables");
    }

    @Test
    public void testSaveFromUI_Validation() {
        System.out.println("saveFromUI - Validation");
        CustomerController instance = new CustomerController();
        

        final boolean[] successCalled = {false};
        Runnable onSuccess = () -> successCalled[0] = true;
        

        instance.saveFromUI("", " ", " ", "", "", false, onSuccess, null);
        
        assertFalse(successCalled[0], "No debe llamar a onSuccess si los campos obligatorios están vacíos");
    }

    @Test
    public void testFindForEdit_NullId() {
        System.out.println("findForEdit - Null ID");
        CustomerController instance = new CustomerController();
        

        assertDoesNotThrow(() -> {
            instance.findForEdit(null, customer -> {}, null);
        });
    }

    @Test
    public void testDeleteFromUI_InvalidId() {
        System.out.println("deleteFromUI - Invalid ID");
        CustomerController instance = new CustomerController();
        

        final boolean[] successCalled = {false};
        Runnable onSuccess = () -> successCalled[0] = true;
        
        try {
            instance.deleteFromUI("-1", onSuccess, null);
        } catch (Exception e) {

        }
    }

    @Test
    public void testGetAllCustomers() {
        System.out.println("getAllCustomers");
        CustomerController instance = new CustomerController();
        
        List<Customer> result = instance.getAllCustomers();
        
        assertNotNull(result, "La lista no debe ser nula");
    }

    @Test
    public void testSelectCustomerForEvent_NoSelection() {
        System.out.println("selectCustomerForEvent - No selection");
        CustomerController instance = new CustomerController();

        String[] columns = {"ID", "Nombre"};
        DefaultTableModel model = new DefaultTableModel(columns, 1);
        JTable table = new JTable(model); 
        table.clearSelection(); 

        JFrame frame = new JFrame();

        assertDoesNotThrow(() -> {
            instance.selectCustomerForEvent(table, frame, null);
        });
    }
}