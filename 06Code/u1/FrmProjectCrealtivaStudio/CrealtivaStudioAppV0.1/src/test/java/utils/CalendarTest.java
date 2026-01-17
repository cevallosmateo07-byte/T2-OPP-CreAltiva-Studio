package utils;

import ec.edu.espe.crealtivastudios.model.Calendar;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author OBJECT MASTER, OOP
 */
public class CalendarTest {
    
    public CalendarTest() {
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
    public void testGetFullSchedule() {
        System.out.println("getFullSchedule");
        Calendar instance = new Calendar();
        Map<String, List<String>> expResult = null;
        Map<String, List<String>> result = instance.getFullSchedule();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testPrintMonthlySchedule() {
        System.out.println("printMonthlySchedule");
        String yearMonth = "";
        Calendar instance = new Calendar();
        instance.printMonthlySchedule(yearMonth);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testPrintFullCalendar() {
        System.out.println("printFullCalendar");
        Calendar instance = new Calendar();
        instance.printFullCalendar();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
