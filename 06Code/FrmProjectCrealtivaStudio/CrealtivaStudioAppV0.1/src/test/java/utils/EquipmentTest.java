package utils;

import ec.edu.espe.crealtivastudios.model.Equipment;
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
public class EquipmentTest {
    
    public EquipmentTest() {
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
        Equipment instance = new Equipment();
        boolean expResult = false;
        boolean result = instance.save();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testDelete() {
        System.out.println("delete");
        Equipment instance = new Equipment();
        boolean expResult = false;
        boolean result = instance.delete();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testMarkAsInUse() {
        System.out.println("markAsInUse");
        Equipment instance = new Equipment();
        instance.markAsInUse();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testMarkAsAvailable() {
        System.out.println("markAsAvailable");
        Equipment instance = new Equipment();
        instance.markAsAvailable();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testMarkAsMaintenance() {
        System.out.println("markAsMaintenance");
        Equipment instance = new Equipment();
        instance.markAsMaintenance();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testReloadFromJson() {
        System.out.println("reloadFromJson");
        Equipment.reloadFromJson();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAllEquipment() {
        System.out.println("getAllEquipment");
        List<Equipment> expResult = null;
        List<Equipment> result = Equipment.getAllEquipment();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testFindById() {
        System.out.println("findById");
        int equipmentId = 0;
        Equipment expResult = null;
        Equipment result = Equipment.findById(equipmentId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testFindByStatus() {
        System.out.println("findByStatus");
        String status = "";
        List<Equipment> expResult = null;
        List<Equipment> result = Equipment.findByStatus(status);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEquipmentId() {
        System.out.println("getEquipmentId");
        Equipment instance = new Equipment();
        int expResult = 0;
        int result = instance.getEquipmentId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEquipmentId() {
        System.out.println("setEquipmentId");
        int equipmentId = 0;
        Equipment instance = new Equipment();
        instance.setEquipmentId(equipmentId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetName() {
        System.out.println("getName");
        Equipment instance = new Equipment();
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
        Equipment instance = new Equipment();
        instance.setName(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        Equipment instance = new Equipment();
        String expResult = "";
        String result = instance.getDescription();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetDescription() {
        System.out.println("setDescription");
        String description = "";
        Equipment instance = new Equipment();
        instance.setDescription(description);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        Equipment instance = new Equipment();
        String expResult = "";
        String result = instance.getStatus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetStatus() {
        System.out.println("setStatus");
        String status = "";
        Equipment instance = new Equipment();
        instance.setStatus(status);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsAvailable() {
        System.out.println("isAvailable");
        Equipment instance = new Equipment();
        boolean expResult = false;
        boolean result = instance.isAvailable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testToSimpleString() {
        System.out.println("toSimpleString");
        Equipment instance = new Equipment();
        String expResult = "";
        String result = instance.toSimpleString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testToString() {
        System.out.println("toString");
        Equipment instance = new Equipment();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
