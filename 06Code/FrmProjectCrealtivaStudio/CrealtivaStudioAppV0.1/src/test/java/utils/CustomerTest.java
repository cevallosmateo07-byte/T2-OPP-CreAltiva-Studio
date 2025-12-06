package utils;

import ec.edu.espe.crealtivastudios.model.Customer;
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
public class CustomerTest {
    
    public CustomerTest() {
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
    public void testSave() {
        System.out.println("save");
        Customer instance = new Customer();
        boolean expResult = true;
        boolean result = instance.save();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDelete() {
        System.out.println("delete");
        Customer instance = new Customer();
        boolean expResult = false;
        boolean result = instance.delete();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testAddEvent() {
        System.out.println("addEvent");
        String eventName = "hola";
        String eventDate = "jiji";
        int eventTypeCode = 0;
        Customer instance = new Customer();
        Event expResult = null;
        Event result = instance.addEvent(eventName, eventDate, eventTypeCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testRemoveEvent() {
        System.out.println("removeEvent");
        int eventId = 0;
        Customer instance = new Customer();
        boolean expResult = false;
        boolean result = instance.removeEvent(eventId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAllCustomers() {
        System.out.println("getAllCustomers");
        List<Customer> expResult = null;
        List<Customer> result = Customer.getAllCustomers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testFindCustomerById() {
        System.out.println("findCustomerById");
        int customerId = 0;
        Customer expResult = null;
        Customer result = Customer.findCustomerById(customerId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testFindCustomersByName() {
        System.out.println("findCustomersByName");
        String name = "";
        List<Customer> expResult = null;
        List<Customer> result = Customer.findCustomersByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDeleteCustomer() {
        System.out.println("deleteCustomer");
        int customerId = 0;
        boolean expResult = false;
        boolean result = Customer.deleteCustomer(customerId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testReloadFromJson() {
        System.out.println("reloadFromJson");
        Customer.reloadFromJson();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsValidEmail() {
        System.out.println("isValidEmail");
        Customer instance = new Customer();
        boolean expResult = false;
        boolean result = instance.isValidEmail();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsValidPhone() {
        System.out.println("isValidPhone");
        Customer instance = new Customer();
        boolean expResult = false;
        boolean result = instance.isValidPhone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsValidCustomer() {
        System.out.println("isValidCustomer");
        Customer instance = new Customer();
        boolean expResult = false;
        boolean result = instance.isValidCustomer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetId() {
        System.out.println("getId");
        Customer instance = new Customer();
        int expResult = 0;
        int result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 0;
        Customer instance = new Customer();
        instance.setId(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetName() {
        System.out.println("getName");
        Customer instance = new Customer();
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "";
        Customer instance = new Customer();
        instance.setName(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPhone() {
        System.out.println("getPhone");
        Customer instance = new Customer();
        String expResult = "";
        String result = instance.getPhone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetPhone() {
        System.out.println("setPhone");
        String phone = "";
        Customer instance = new Customer();
        instance.setPhone(phone);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEmail() {
        System.out.println("getEmail");
        Customer instance = new Customer();
        String expResult = "";
        String result = instance.getEmail();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEmail() {
        System.out.println("setEmail");
        String email = "";
        Customer instance = new Customer();
        instance.setEmail(email);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAddress() {
        System.out.println("getAddress");
        Customer instance = new Customer();
        String expResult = "";
        String result = instance.getAddress();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetAddress() {
        System.out.println("setAddress");
        String address = "";
        Customer instance = new Customer();
        instance.setAddress(address);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEvents() {
        System.out.println("getEvents");
        Customer instance = new Customer();
        List<Event> expResult = null;
        List<Event> result = instance.getEvents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEvents() {
        System.out.println("setEvents");
        List<Event> events = null;
        Customer instance = new Customer();
        instance.setEvents(events);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEventById() {
        System.out.println("getEventById");
        int eventId = 0;
        Customer instance = new Customer();
        Event expResult = null;
        Event result = instance.getEventById(eventId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testToSimpleString() {
        System.out.println("toSimpleString");
        Customer instance = new Customer();
        String expResult = "";
        String result = instance.toSimpleString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDisplayCustomerInfo() {
        System.out.println("displayCustomerInfo");
        Customer instance = new Customer();
        instance.displayCustomerInfo();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetTotalEventCost() {
        System.out.println("getTotalEventCost");
        Customer instance = new Customer();
        double expResult = 0.0;
        double result = instance.getTotalEventCost();
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEventCount() {
        System.out.println("getEventCount");
        Customer instance = new Customer();
        int expResult = 0;
        int result = instance.getEventCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEventsByType() {
        System.out.println("getEventsByType");
        int eventTypeCode = 0;
        Customer instance = new Customer();
        List<Event> expResult = null;
        List<Event> result = instance.getEventsByType(eventTypeCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testShowUpcomingAppointments() {
        System.out.println("showUpcomingAppointments");
        Customer instance = new Customer();
        instance.showUpcomingAppointments();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testFindEventById() {
        System.out.println("findEventById");
        int eventId = 0;
        Event expResult = null;
        Event result = Customer.findEventById(eventId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testAddManualReminderToEvent() {
        System.out.println("addManualReminderToEvent");
        int eventId = 0;
        String reminderDetails = "";
        Customer instance = new Customer();
        boolean expResult = false;
        boolean result = instance.addManualReminderToEvent(eventId, reminderDetails);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDisplayEventReminders() {
        System.out.println("displayEventReminders");
        int eventId = 0;
        Customer instance = new Customer();
        instance.displayEventReminders(eventId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
