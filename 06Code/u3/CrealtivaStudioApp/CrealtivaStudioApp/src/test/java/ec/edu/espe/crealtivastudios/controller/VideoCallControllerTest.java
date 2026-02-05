package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.VideoCall;
import java.awt.Component;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kevin Chalan
 */
public class VideoCallControllerTest {

    @Test
    public void testGetVideoCallsTableModel() {
        System.out.println("getVideoCallsTableModel");
        VideoCallController instance = new VideoCallController();
        
        DefaultTableModel result = instance.getVideoCallsTableModel();
        
        assertNotNull(result, "El modelo de tabla no debe ser nulo");
        assertEquals(5, result.getColumnCount(), "Debe tener 5 columnas: ID, Cliente, Plataforma, Fecha, Hora");
        assertFalse(result.isCellEditable(0, 0), "Las celdas no deben ser editables");
    }

    @Test
    public void testGetAllVideoCalls() {
        System.out.println("getAllVideoCalls");
        VideoCallController instance = new VideoCallController();
        
        List<VideoCall> result = instance.getAllVideoCalls();
        
        assertNotNull(result, "La lista de videollamadas no debe ser nula");

    }

    @Test
    public void testSaveVideoCall_Validation() {
        System.out.println("saveVideoCall - Validation");
        VideoCallController instance = new VideoCallController();
        
        final boolean[] success = {false};
        Runnable onSuccess = () -> success[0] = true;

        instance.saveVideoCall("", "Zoom", new Date(), "10:00", false, onSuccess, null);
        assertFalse(success[0], "No debe guardar si el cliente está vacío");

        instance.saveVideoCall("1 - Kevin", "Meet", null, "11:00", false, onSuccess, null);
        assertFalse(success[0], "No debe guardar si la fecha es nula");
    }

    @Test
    public void testParseCustomerId_ValidFormat() {
        System.out.println("parseCustomerId - Valid");
        String comboValue = "105 - Juan Perez";
        VideoCallController instance = new VideoCallController();
        
        int expResult = 105;
        int result = instance.parseCustomerId(comboValue);
        
        assertEquals(expResult, result, "Debe extraer correctamente el ID antes del guion");
    }

    @Test
    public void testParseCustomerId_InvalidFormat() {
        System.out.println("parseCustomerId - Invalid");
        String comboValue = "Texto Sin Formato";
        VideoCallController instance = new VideoCallController();
        
        int result = instance.parseCustomerId(comboValue);
        
        assertEquals(0, result, "Debe retornar 0 si el formato del String es incorrecto");
    }

    @Test
    public void testGetVideoCallById() {
        System.out.println("getVideoCallById");
        int id = 9999; // ID inexistente
        VideoCallController instance = new VideoCallController();
        
        VideoCall result = instance.getVideoCallById(id);

        assertNull(result, "Debe retornar null si el ID no existe en la base de datos");
    }

    @Test
    public void testDeleteVideoCall() {
        System.out.println("deleteVideoCall");
        int id = -1; // ID inválido
        final boolean[] success = {false};
        Runnable onSuccess = () -> success[0] = true;
        
        VideoCallController instance = new VideoCallController();

        assertDoesNotThrow(() -> instance.deleteVideoCall(id, onSuccess, null));
    }
}