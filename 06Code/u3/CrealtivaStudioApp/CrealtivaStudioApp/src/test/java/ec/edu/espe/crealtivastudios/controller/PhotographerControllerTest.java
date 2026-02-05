package ec.edu.espe.crealtivastudios.controller;

import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kevin Chalan
 */
public class PhotographerControllerTest {

    @Test
    public void testFillListModel() {
        System.out.println("fillListModel");
        DefaultListModel<String> model = new DefaultListModel<>();
        PhotographerController instance = new PhotographerController();
        

        assertDoesNotThrow(() -> instance.fillListModel(model));
        assertNotNull(model);
    }

    @Test
    public void testUpdateStatusLabel() {
        System.out.println("updateStatusLabel");
        JLabel label = new JLabel("Original");
        String name = "Micaela Garcia"; 
        PhotographerController instance = new PhotographerController();
        

        assertDoesNotThrow(() -> instance.updateStatusLabel(label, name));
    }

    @Test
    public void testDeletePhotographer_InvalidFormat() {
        System.out.println("deletePhotographer - Invalid Format");

        String selected = "1NombreFotografo"; 
        final boolean[] success = {false};
        Runnable onSuccess = () -> success[0] = true;
        
        PhotographerController instance = new PhotographerController();
        instance.deletePhotographer(selected, onSuccess, null);
        

        assertFalse(success[0], "No debe borrar si el formato del string no es 'ID - Nombre'");
    }

    @Test
    public void testGetAssignTableModel() {
        System.out.println("getAssignTableModel");
        PhotographerController instance = new PhotographerController();
        DefaultTableModel result = instance.getAssignTableModel();
        
        assertNotNull(result);
        assertEquals(3, result.getColumnCount(), "Debe tener 3 columnas: ID Evento, Nombre Evento, Asignado A");
    }

    @Test
    public void testGetPhotographersComboModel() {
        System.out.println("getPhotographersComboModel");
        PhotographerController instance = new PhotographerController();
        DefaultComboBoxModel<String> result = instance.getPhotographersComboModel();
        
        assertNotNull(result);
        assertEquals("- Seleccione -", result.getElementAt(0), "El primer elemento debe ser el placeholder");
    }

    @Test
    public void testAssignEvent_Validation() {
        System.out.println("assignEvent - Validation");

        String photo = "- Seleccione -";
        String event = "- Seleccione -";
        final boolean[] success = {false};
        Runnable ok = () -> success[0] = true;
        
        PhotographerController instance = new PhotographerController();
        instance.assignEvent(photo, event, ok, null);
        
        assertFalse(success[0], "No debe asignar si se seleccionan los placeholders");
    }

    @Test
    public void testGetAttendanceTableModel() {
        System.out.println("getAttendanceTableModel");
        PhotographerController instance = new PhotographerController();
        DefaultTableModel result = instance.getAttendanceTableModel();
        
        assertNotNull(result);

        assertEquals(Boolean.class, result.getColumnClass(3), "La columna de asistencia debe ser Boolean");
        assertTrue(result.isCellEditable(0, 3), "La columna de asistencia debe ser editable");
        assertFalse(result.isCellEditable(0, 0), "La columna ID no debe ser editable");
    }

    @Test
    public void testIsPhotographerAssigned() {
        System.out.println("isPhotographerAssigned");
        PhotographerController instance = new PhotographerController();
        
        assertDoesNotThrow(() -> instance.isPhotographerAssigned("NonExistent"));
    }
}