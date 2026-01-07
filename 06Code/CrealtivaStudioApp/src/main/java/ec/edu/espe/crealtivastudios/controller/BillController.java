
package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Bill;
import ec.edu.espe.crealtivastudios.model.Customer;
import ec.edu.espe.crealtivastudios.model.Event;

public class BillController {

    /**
     
     */
    public static String registerBill(int customerId, int eventId, String notes) {

        Customer customer = Customer.findCustomerById(customerId);
        if (customer == null) {
            return "Cliente no encontrado";
        }

        
        Event event = customer.getEventById(eventId);
        if (event == null) {
            return "Evento no encontrado para este cliente";
        }

        Bill bill = new Bill(customerId, eventId, notes);

        boolean saved = bill.save();

        if (!saved) {
            return "Error al guardar la factura";
        }

        return "OK";
    }

    /**
     
     */
    public static String deleteBill(int billId) {

        if (billId <= 0) {
            return "ID de factura inválido";
        }

        Bill bill = Bill.findBillById(billId);

        if (bill == null) {
            return "Factura no encontrada";
        }

        if (!bill.delete()) {
            return "No se pudo eliminar la factura";
        }

        return "Factura eliminada correctamente";
    }
}
