package utils;

import ec.edu.espe.crealtivastudios.model.Event;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Kevin Chalan, OBJECT MASTER, OOP
 */
public class EventTest {
    
    public EventTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetAllEvents() {
        System.out.println("getAllEvents");
        List<Event> expResult = null;
        List<Event> result = Event.getAllEvents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testReloadFromJson() {
        System.out.println("reloadFromJson");
        Event.reloadFromJson();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSaveAllToJson() {
        System.out.println("saveAllToJson");
        boolean expResult = false;
        boolean result = Event.saveAllToJson();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testCalculateFinalPrice() {
        System.out.println("calculateFinalPrice");
        Event instance = new Event();
        double expResult = 0.0;
        double result = instance.calculateFinalPrice();
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEventTypeString() {
        System.out.println("getEventTypeString");
        int eventTypeCode = 0;
        String expResult = "";
        String result = Event.getEventTypeString(eventTypeCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsValidEvent() {
        System.out.println("isValidEvent");
        Event instance = new Event();
        boolean expResult = false;
        boolean result = instance.isValidEvent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEventId() {
        System.out.println("getEventId");
        Event instance = new Event();
        int expResult = 0;
        int result = instance.getEventId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEventId() {
        System.out.println("setEventId");
        int eventId = 0;
        Event instance = new Event();
        instance.setEventId(eventId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEventName() {
        System.out.println("getEventName");
        Event instance = new Event();
        String expResult = "";
        String result = instance.getEventName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEventName() {
        System.out.println("setEventName");
        String eventName = "";
        Event instance = new Event();
        instance.setEventName(eventName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEventDate() {
        System.out.println("getEventDate");
        Event instance = new Event();
        String expResult = "";
        String result = instance.getEventDate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEventDate() {
        System.out.println("setEventDate");
        String eventDate = "";
        Event instance = new Event();
        instance.setEventDate(eventDate);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEventType() {
        System.out.println("getEventType");
        Event instance = new Event();
        String expResult = "";
        String result = instance.getEventType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEventType() {
        System.out.println("setEventType");
        String eventType = "";
        Event instance = new Event();
        instance.setEventType(eventType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEventTypeCode() {
        System.out.println("getEventTypeCode");
        Event instance = new Event();
        int expResult = 0;
        int result = instance.getEventTypeCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEventTypeCode() {
        System.out.println("setEventTypeCode");
        int eventTypeCode = 0;
        Event instance = new Event();
        instance.setEventTypeCode(eventTypeCode);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetBasePrice() {
        System.out.println("getBasePrice");
        Event instance = new Event();
        double expResult = 0.0;
        double result = instance.getBasePrice();
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetBasePrice() {
        System.out.println("setBasePrice");
        double basePrice = 0.0;
        Event instance = new Event();
        instance.setBasePrice(basePrice);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetCustomerId() {
        System.out.println("getCustomerId");
        Event instance = new Event();
        int expResult = 0;
        int result = instance.getCustomerId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetCustomerId() {
        System.out.println("setCustomerId");
        int customerId = 0;
        Event instance = new Event();
        instance.setCustomerId(customerId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testAddReminder() {
        System.out.println("addReminder");
        String reminder = "";
        Event instance = new Event();
        instance.addReminder(reminder);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetReminders() {
        System.out.println("getReminders");
        Event instance = new Event();
        List<String> expResult = null;
        List<String> result = instance.getReminders();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testScheduleAutomaticAppointment() {
        System.out.println("scheduleAutomaticAppointment");
        Event instance = new Event();
        String expResult = "";
        String result = instance.scheduleAutomaticAppointment();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsUpcoming() {
        System.out.println("isUpcoming");
        Event instance = new Event();
        boolean expResult = false;
        boolean result = instance.isUpcoming();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSave() {
        System.out.println("save");
        Event instance = new Event();
        boolean expResult = false;
        boolean result = instance.save();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
