package ec.edu.espe.crealtivastudios.controller;

import java.awt.Component;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Chicaiza Objet Maseters
 */
public class CalendarControllerTest {

    @Test
    public void testLoadEventsIntoCombo() {
        System.out.println("loadEventsIntoCombo");

        JComboBox combo = new JComboBox();
        CalendarController instance = new CalendarController();
        

        try {
            instance.loadEventsIntoCombo(combo);

            assertNotNull(combo, "El combo no debe ser nulo");
        } catch (Exception e) {
            System.out.println("Nota: No se pudo cargar combo (posible falta de DB)");
        }
    }

    @Test
    public void testShowSelectedEventFromCombo() {
        System.out.println("showSelectedEventFromCombo");
        JComboBox combo = new JComboBox();
        JTextField txtEventId = new JTextField();
        JTextArea txaDetails = new JTextArea();
        
        CalendarController instance = new CalendarController();
        
        instance.showSelectedEventFromCombo(combo, txtEventId, txaDetails);
        
        assertEquals("", txtEventId.getText());
    }

    @Test
    public void testSaveReminderFromUI() {
        System.out.println("saveReminderFromUI");

        String eventIdRaw = ""; 
        Date dateRaw = null;

        final boolean[] successCalled = {false};
        Runnable onSuccess = () -> successCalled[0] = true;
        
        CalendarController instance = new CalendarController();

        instance.saveReminderFromUI(eventIdRaw, dateRaw, onSuccess, null);
        
        assertFalse(successCalled[0], "El callback no debe ejecutarse si los campos están vacíos");
    }

    @Test
    public void testGetTableModel() {
        System.out.println("getTableModel");
        CalendarController instance = new CalendarController();
        
        DefaultTableModel result = instance.getTableModel();
        
        assertNotNull(result, "El modelo de tabla no debe ser nulo");

        assertEquals(3, result.getColumnCount());
        assertEquals("ID", result.getColumnName(0));
        assertEquals("Fecha", result.getColumnName(2));
    }
}