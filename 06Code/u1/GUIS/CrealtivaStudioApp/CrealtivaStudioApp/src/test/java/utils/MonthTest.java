package utils;

import ec.edu.espe.crealtivastudios.model.VideoCall;
import ec.edu.espe.crealtivastudios.model.Event;
import ec.edu.espe.crealtivastudios.model.Month;
import java.time.YearMonth;
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
public class MonthTest {
    
    public MonthTest() {
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
    public void testAddEvent() {
        System.out.println("addEvent");
        Event event = null;
        Month instance = null;
        boolean expResult = false;
        boolean result = instance.addEvent(event);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testAddVideoCall() {
        System.out.println("addVideoCall");
        VideoCall videoCall = null;
        Month instance = null;
        boolean expResult = false;
        boolean result = instance.addVideoCall(videoCall);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testLoadAllFromSystem() {
        System.out.println("loadAllFromSystem");
        Month instance = null;
        instance.loadAllFromSystem();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEvents() {
        System.out.println("getEvents");
        Month instance = null;
        List<Event> expResult = null;
        List<Event> result = instance.getEvents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetVideoCalls() {
        System.out.println("getVideoCalls");
        Month instance = null;
        List<VideoCall> expResult = null;
        List<VideoCall> result = instance.getVideoCalls();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEventsByType() {
        System.out.println("getEventsByType");
        int eventTypeCode = 0;
        Month instance = null;
        List<Event> expResult = null;
        List<Event> result = instance.getEventsByType(eventTypeCode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetVideoCallsByCustomer() {
        System.out.println("getVideoCallsByCustomer");
        int customerId = 0;
        Month instance = null;
        List<VideoCall> expResult = null;
        List<VideoCall> result = instance.getVideoCallsByCustomer(customerId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetTotalEvents() {
        System.out.println("getTotalEvents");
        Month instance = null;
        int expResult = 0;
        int result = instance.getTotalEvents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetTotalVideoCalls() {
        System.out.println("getTotalVideoCalls");
        Month instance = null;
        int expResult = 0;
        int result = instance.getTotalVideoCalls();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEventsByDay() {
        System.out.println("getEventsByDay");
        int day = 0;
        Month instance = null;
        List<Event> expResult = null;
        List<Event> result = instance.getEventsByDay(day);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetVideoCallsByDay() {
        System.out.println("getVideoCallsByDay");
        int day = 0;
        Month instance = null;
        List<VideoCall> expResult = null;
        List<VideoCall> result = instance.getVideoCallsByDay(day);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetMonthSummary() {
        System.out.println("getMonthSummary");
        Month instance = null;
        String expResult = "";
        String result = instance.getMonthSummary();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetDetailedReport() {
        System.out.println("getDetailedReport");
        Month instance = null;
        String expResult = "";
        String result = instance.getDetailedReport();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetYearMonth() {
        System.out.println("getYearMonth");
        Month instance = null;
        YearMonth expResult = null;
        YearMonth result = instance.getYearMonth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetMonthName() {
        System.out.println("getMonthName");
        Month instance = null;
        String expResult = "";
        String result = instance.getMonthName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetYear() {
        System.out.println("getYear");
        Month instance = null;
        int expResult = 0;
        int result = instance.getYear();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetMonthValue() {
        System.out.println("getMonthValue");
        Month instance = null;
        int expResult = 0;
        int result = instance.getMonthValue();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetCurrentMonth() {
        System.out.println("getCurrentMonth");
        Month expResult = null;
        Month result = Month.getCurrentMonth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetMonth() {
        System.out.println("getMonth");
        int year = 0;
        int month = 0;
        Month expResult = null;
        Month result = Month.getMonth(year, month);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAllMonthsWithActivities() {
        System.out.println("getAllMonthsWithActivities");
        List<Month> expResult = null;
        List<Month> result = Month.getAllMonthsWithActivities();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testToString() {
        System.out.println("toString");
        Month instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
