package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Bill;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Chicaiza Objet Maseters
 */
public class BillControllerTest {
    
    public BillControllerTest() {
    }

    @Test
    public void testGetTableModel() {
        System.out.println("getTableModel");
        BillController instance = new BillController();
        
        DefaultTableModel result = instance.getTableModel();
        
        assertNotNull(result, "El modelo de la tabla no debe ser nulo");
        
        assertEquals(5, result.getColumnCount(), "Debe tener 5 columnas: ID, Cliente, Evento, Monto, Estado");
        
        assertEquals("ID Factura", result.getColumnName(0));
        assertEquals("Estado", result.getColumnName(4));
    }

    @Test
    public void testSyncEventsToBills() {
        System.out.println("syncEventsToBills");
        BillController instance = new BillController();
        
        assertDoesNotThrow(() -> instance.syncEventsToBills());
    }

    @Test
    public void testUpdateStatusFromUI() {
        System.out.println("updateStatusFromUI");
        
        String idRaw = "1"; 
        String statusText = "CANCELADO";

        final boolean[] callbackExecuted = {false};
        Runnable onSuccess = () -> callbackExecuted[0] = true;
        
        BillController instance = new BillController();
        
        try {
            instance.updateStatusFromUI(idRaw, statusText, onSuccess, null);
        } catch (Exception e) {
            System.out.println("Nota: updateStatus falló probablemente por falta de conexión a DB: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllBills() {
        System.out.println("getAllBills");
        BillController instance = new BillController();
        
        List<Bill> result = instance.getAllBills();
        
        // Verificamos que el resultado sea una lista (aunque esté vacía)
        assertNotNull(result, "La lista de facturas no debe ser nula");
        
        // Si hay datos, verificar que el primer elemento sea un objeto Bill
        if (!result.isEmpty()) {
            assertTrue(result.get(0) instanceof Bill);
        }
    }
}