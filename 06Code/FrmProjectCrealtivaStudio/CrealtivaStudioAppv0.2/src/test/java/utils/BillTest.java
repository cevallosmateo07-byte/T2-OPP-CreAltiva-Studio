package utils;

import ec.edu.espe.crealtivastudios.model.Bill;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author  OBJECT MASTER, OOP
 */
public class BillTest {
    
    public BillTest() {
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
    public void testGetTotalRevenue() {
        System.out.println("getTotalRevenue");
        Object[] expResult = null;
        Object[] result = Bill.getTotalRevenue();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetTotalPending() {
        System.out.println("getTotalPending");
        double expResult = 0.0;
        double result = Bill.getTotalPending();
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testConfirmPayment() {
        System.out.println("confirmPayment");
        Bill instance = null;
        instance.confirmPayment();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testMarkAsPending() {
        System.out.println("markAsPending");
        Bill instance = null;
        instance.markAsPending();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsPaid() {
        System.out.println("isPaid");
        Bill instance = null;
        boolean expResult = false;
        boolean result = instance.isPaid();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsPending() {
        System.out.println("isPending");
        Bill instance = null;
        boolean expResult = false;
        boolean result = instance.isPending();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSave() {
        System.out.println("save");
        Bill instance = null;
        boolean expResult = false;
        boolean result = instance.save();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDelete() {
        System.out.println("delete");
        Bill instance = null;
        boolean expResult = false;
        boolean result = instance.delete();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAllBills() {
        System.out.println("getAllBills");
        List<Bill> expResult = null;
        List<Bill> result = Bill.getAllBills();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testFindBillById() {
        System.out.println("findBillById");
        int billId = 0;
        Bill expResult = null;
        Bill result = Bill.findBillById(billId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testFindBillsByCustomer() {
        System.out.println("findBillsByCustomer");
        int customerId = 0;
        List<Bill> expResult = null;
        List<Bill> result = Bill.findBillsByCustomer(customerId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testFindPendingBills() {
        System.out.println("findPendingBills");
        List<Bill> expResult = null;
        List<Bill> result = Bill.findPendingBills();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testFindPaidBills() {
        System.out.println("findPaidBills");
        List<Bill> expResult = null;
        List<Bill> result = Bill.findPaidBills();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testReloadFromJson() {
        System.out.println("reloadFromJson");
        Bill.reloadFromJson();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testConfirmPaymentStatus() {
        System.out.println("confirmPaymentStatus");
        Bill instance = null;
        String expResult = "";
        String result = instance.confirmPaymentStatus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testShowPendingPayment() {
        System.out.println("showPendingPayment");
        Bill instance = null;
        String expResult = "";
        String result = instance.showPendingPayment();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPaymentSummary() {
        System.out.println("getPaymentSummary");
        Bill instance = null;
        String expResult = "";
        String result = instance.getPaymentSummary();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetBillId() {
        System.out.println("getBillId");
        Bill instance = null;
        int expResult = 0;
        int result = instance.getBillId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetBillId() {
        System.out.println("setBillId");
        int billId = 0;
        Bill instance = null;
        instance.setBillId(billId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetCustomerId() {
        System.out.println("getCustomerId");
        Bill instance = null;
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
        Bill instance = null;
        instance.setCustomerId(customerId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEventId() {
        System.out.println("getEventId");
        Bill instance = null;
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
        Bill instance = null;
        instance.setEventId(eventId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsIsPaid() {
        System.out.println("isIsPaid");
        Bill instance = null;
        boolean expResult = false;
        boolean result = instance.isIsPaid();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetIsPaid() {
        System.out.println("setIsPaid");
        boolean isPaid = false;
        Bill instance = null;
        instance.setIsPaid(isPaid);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetNotes() {
        System.out.println("getNotes");
        Bill instance = null;
        String expResult = "";
        String result = instance.getNotes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetNotes() {
        System.out.println("setNotes");
        String notes = "";
        Bill instance = null;
        instance.setNotes(notes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAmount() {
        System.out.println("getAmount");
        Bill instance = null;
        double expResult = 0.0;
        double result = instance.getAmount();
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetAmount() {
        System.out.println("setAmount");
        double amount = 0.0;
        Bill instance = null;
        instance.setAmount(amount);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testToString() {
        System.out.println("toString");
        Bill instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDisplayBillInfo() {
        System.out.println("displayBillInfo");
        Bill instance = null;
        instance.displayBillInfo();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testCheckImportantDateAlert() {
        System.out.println("checkImportantDateAlert");
        Bill instance = null;
        instance.checkImportantDateAlert();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
