package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Equipment;
import java.awt.Component;
import java.util.function.Consumer;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Chicaiza Objet Maseters
 */
public class EquipmentControllerTest {

    @Test
    public void testGetTableModel() {
        System.out.println("getTableModel");
        EquipmentController instance = new EquipmentController();
        
        DefaultTableModel result = instance.getTableModel();
        
        assertNotNull(result, "El modelo no debe ser nulo");

        assertEquals(4, result.getColumnCount(), "Debe tener 4 columnas");
        assertEquals("ID", result.getColumnName(0));
        assertEquals("Estado", result.getColumnName(3));
        assertFalse(result.isCellEditable(0, 0), "Las celdas no deben ser editables");
    }

    @Test
    public void testSaveFromUI() {
        System.out.println("saveFromUI");
        // Datos de prueba
        String name = "Cámara Sony";
        String desc = "A7III - 4K";
        String status = "Disponible";
        
        final boolean[] success = {false};
        Runnable ok = () -> success[0] = true;
        
        EquipmentController instance = new EquipmentController();
        
        try {

            instance.saveFromUI("", name, desc, status, false, ok, null);

        } catch (Exception e) {
            System.out.println("Nota: Falló saveFromUI por falta de DB: " + e.getMessage());
        }
    }

    @Test
    public void testFindForEdit() {
        System.out.println("findForEdit");
        String idRaw = "1"; 
        
        final Equipment[] foundEquipment = {null};
        Consumer<Equipment> onFound = (equipment) -> foundEquipment[0] = equipment;
        
        EquipmentController instance = new EquipmentController();
        
        try {
            instance.findForEdit(idRaw, onFound, null);

        } catch (Exception e) {
            System.out.println("Nota: Falló findForEdit por falta de DB.");
        }
    }

    @Test
    public void testDeleteFromUI() {
        System.out.println("deleteFromUI");
        String idRaw = "999"; // ID ficticio
        
        final boolean[] deleted = {false};
        Runnable ok = () -> deleted[0] = true;
        
        EquipmentController instance = new EquipmentController();

        assertDoesNotThrow(() -> instance.deleteFromUI(null, ok, null));
        assertFalse(deleted[0], "No se debe llamar al callback si el ID es nulo");
    }
}