package ec.edu.espe.crealtivastudios.controller;

import javax.swing.JFrame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Chicaiza Objet Maseters
 */
public class MenuControllerTest {

    @Test
    public void testOpenCustomerSystem() {
        System.out.println("openCustomerSystem");
     
        JFrame menu = new JFrame();
        menu.setVisible(true);
        
        MenuController instance = new MenuController();
        instance.openCustomerSystem(menu);
        

        assertFalse(menu.isDisplayable(), "El menú principal debería haberse cerrado (disposed)");
    }

    @Test
    public void testOpenEventSystem() {
        System.out.println("openEventSystem");
        JFrame menu = new JFrame();
        MenuController instance = new MenuController();
        
        assertDoesNotThrow(() -> instance.openEventSystem(menu));
        assertFalse(menu.isDisplayable());
    }

    @Test
    public void testOpenBillingSystem() {
        System.out.println("openBillingSystem");
        JFrame menu = new JFrame();
        MenuController instance = new MenuController();
        
        instance.openBillingSystem(menu);
        assertFalse(menu.isDisplayable());
    }

    @Test
    public void testOpenVideoCalls() {
        System.out.println("openVideoCalls");
        JFrame menu = new JFrame();
        MenuController instance = new MenuController();
        
        assertDoesNotThrow(() -> instance.openVideoCalls(menu));
        assertFalse(menu.isDisplayable());
    }

    @Test
    public void testOpenCalendar() {
        System.out.println("openCalendar");
        JFrame menu = new JFrame();
        MenuController instance = new MenuController();
        
        instance.openCalendar(menu);
        assertFalse(menu.isDisplayable());
    }

    @Test
    public void testOpenReports() {
        System.out.println("openReports");
        JFrame menu = new JFrame();
        MenuController instance = new MenuController();
        
        instance.openReports(menu);
        assertFalse(menu.isDisplayable());
    }
    
}