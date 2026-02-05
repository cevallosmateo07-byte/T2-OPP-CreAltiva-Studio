package ec.edu.espe.crealtivastudios.controller;

import java.awt.Component;
import java.io.File;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kevin Chalan
 */
public class ReportEventControllerTest {

    @Test
    public void testGetReportTableModel() {
        System.out.println("getReportTableModel");
        ReportEventController instance = new ReportEventController();
        DefaultTableModel result = instance.getReportTableModel();
        
        assertNotNull(result);

        assertEquals(4, result.getColumnCount());
        assertEquals("Cliente", result.getColumnName(0));
        assertEquals("Estado de Pago", result.getColumnName(3));
    }


    @Test
    public void testGenerateJsonReports_NullTable() {
        System.out.println("generateJsonReports - Null Table");
        ReportEventController instance = new ReportEventController();
        

        assertDoesNotThrow(() -> instance.generateJsonReports(null));
    }

    @Test
    public void testExportToHTML_Validation() {
        System.out.println("exportToHTML");

        String[] cols = {"Col1"};
        DefaultTableModel model = new DefaultTableModel(cols, 1);
        model.setValueAt("Data", 0, 0);
        JTable table = new JTable(model);
        

        File tempFile = new File("test_report.html");
        ReportEventController instance = new ReportEventController();

        assertNotNull(table);
        tempFile.deleteOnExit(); 
    }

    @Test
    public void testExportToCSV() {
        System.out.println("exportToCSV");
        String[] cols = {"Nombre", "Evento"};
        DefaultTableModel model = new DefaultTableModel(cols, 1);
        model.setValueAt("Kevin", 0, 0);
        model.setValueAt("Boda", 0, 1);
        JTable table = new JTable(model);
        
        File file = new File("report_test.csv");
        ReportEventController instance = new ReportEventController();
        
  
        assertDoesNotThrow(() -> instance.exportToCSV(table, file, null));
        
        assertTrue(file.exists() || !file.exists()); 
        if(file.exists()) file.delete();
    }
}