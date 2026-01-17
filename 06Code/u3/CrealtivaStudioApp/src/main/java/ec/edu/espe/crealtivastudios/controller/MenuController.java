package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.view.*; // Importamos todas las vistas
import javax.swing.JFrame;

public class MenuController {

    // =========================================================================
    //  SISTEMA DE NAVEGACIÓN CENTRALIZADO
    // =========================================================================

    public void openCustomerSystem(JFrame menu) {
        new FrmCustomer().setVisible(true);
        menu.dispose(); 
    }

    public void openEventSystem(JFrame menu) {
        new FrmEvent().setVisible(true);
        menu.dispose();
    }

    public void openBillingSystem(JFrame menu) {
        new FrmBillp().setVisible(true);
        menu.dispose();
    }

    public void openPhotographerSystem(JFrame menu) {
        new FrmPhotographer().setVisible(true);
        menu.dispose();
    }

    public void openEquipmentSystem(JFrame menu) {
        new FrmEquipmentAvailable().setVisible(true);
        menu.dispose();
    }

    public void openAttendanceSystem(JFrame menu) {
        new FrmAtendencePhotographers().setVisible(true);
        menu.dispose();
    }

    public void openVideoCalls(JFrame menu) {
        FrmVideoCalls v = new FrmVideoCalls();
        v.setLocationRelativeTo(null); // Centrar
        v.setVisible(true);
        menu.dispose();
    }

    public void openCalendar(JFrame menu) {
        new FrmCalendar().setVisible(true);
        menu.dispose();
    }

    public void openReports(JFrame menu) {
        new FrmReportEvent().setVisible(true);
        menu.dispose();
    }
}