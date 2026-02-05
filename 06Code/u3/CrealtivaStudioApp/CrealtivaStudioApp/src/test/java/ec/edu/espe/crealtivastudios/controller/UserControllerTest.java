package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kevin Chalan
 */
public class UserControllerTest {

    @Test
    public void testHashPassword() {
        System.out.println("hashPassword");
  
        String password = "admin123";
        String result = UserController.hashPassword(password);
        

        String expResult = "f4cd4e68a4f501aa8091107176d1efee410569dcd5e385109cfdd49535c849a0";
        
        assertNotNull(result);
        assertEquals(expResult, result, "El hash generado debe coincidir con el hash estándar SHA-256");
    }

    @Test
    public void testValidateLogin_Success() {
        System.out.println("validateLogin - Success");

        User user = new User("kevin", "admin123", "Admin");
        
        boolean result = UserController.validateLogin(user);
        
        assertTrue(result, "Debería permitir el login para el usuario 'kevin' con la clave 'admin123'");
    }

    @Test
    public void testValidateLogin_WrongPassword() {
        System.out.println("validateLogin - Wrong Password");
   
        User user = new User("kevin", "claveEquivocada", "Admin");
        
        boolean result = UserController.validateLogin(user);
        
        assertFalse(result, "No debería permitir el login con una contraseña incorrecta");
    }

    @Test
    public void testValidateLogin_WrongUser() {
        System.out.println("validateLogin - Wrong User");

        User user = new User("pepe", "admin123", "Admin");
        
        boolean result = UserController.validateLogin(user);
        
        assertFalse(result, "No debería permitir el login si el nombre de usuario no es 'kevin'");
    }

    @Test
    public void testHashConsistency() {
        System.out.println("hashConsistency");
        String pass = "holamundo123";
        
        String hash1 = UserController.hashPassword(pass);
        String hash2 = UserController.hashPassword(pass);
        
        assertEquals(hash1, hash2, "El proceso de hash debe ser determinista (siempre el mismo resultado para el mismo input)");
    }
}