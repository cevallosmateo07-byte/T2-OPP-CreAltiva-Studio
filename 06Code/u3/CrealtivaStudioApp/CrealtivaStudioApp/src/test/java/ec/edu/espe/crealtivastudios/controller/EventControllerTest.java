package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Event;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Chicaiza
 */
public class EventControllerTest {

    @Test
    public void testGetEventsListText() {
        System.out.println("getEventsListText");
        EventController instance = new EventController();
        String result = instance.getEventsListText();
        
        assertNotNull(result);

        assertTrue(result.contains("ID:") || result.equals("No hay eventos registrados."));
    }

    @Test
    public void testCalculateEventPrice() {
        System.out.println("calculateEventPrice");
        EventController instance = new EventController();
        

        Event wedding = new Event(1, "Boda Test", "2026-05-10", EventController.TYPE_BODAS, 1);
        assertEquals(135.00, instance.calculateEventPrice(wedding), 0.001);
        

        Event baptism = new Event(2, "Bautizo Test", "2026-06-12", EventController.TYPE_BAUTIZOS, 2);
        assertEquals(20.00, instance.calculateEventPrice(baptism), 0.001);
        

        Event unknown = new Event(3, "Otro", "2026-07-20", 99, 3);
        assertEquals(50.00, instance.calculateEventPrice(unknown), 0.001);
    }

    @Test
    public void testGetEventTypeString() {
        System.out.println("getEventTypeString");
        EventController instance = new EventController();
        
        assertEquals("Bodas", instance.getEventTypeString(EventController.TYPE_BODAS));
        assertEquals("Graduaciones", instance.getEventTypeString(EventController.TYPE_GRADUACIONES));
        assertEquals("Otro", instance.getEventTypeString(0));
    }

    @Test
    public void testSaveEventFromUI_Validation() {
        System.out.println("saveEventFromUI - Validation");
        EventController instance = new EventController();
        
        final boolean[] success = {false};
        Runnable onSuccess = () -> success[0] = true;


        instance.saveEventFromUI("Ab", new Date(), 0, 1, onSuccess, null);
        assertFalse(success[0], "No debe guardar eventos con nombres de menos de 3 letras");


        instance.saveEventFromUI("Evento Test", null, 0, 1, onSuccess, null);
        assertFalse(success[0], "No debe guardar si la fecha es nula");
    }

    @Test
    public void testLoadEventsIntoComboBox_Null() {
        System.out.println("loadEventsIntoComboBox - Null Safety");
        EventController instance = new EventController();
        
 
        assertThrows(NullPointerException.class, () -> {
            instance.loadEventsIntoComboBox(null);
        });
    }

    @Test
    public void testGetAllEvents() {
        System.out.println("getAllEvents");
        EventController instance = new EventController();
        List<Event> result = instance.getAllEvents();
        
        assertNotNull(result, "La lista no debe ser nula, incluso si está vacía");
    }
}