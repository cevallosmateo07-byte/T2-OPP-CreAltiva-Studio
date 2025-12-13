package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.Customer;
import ec.edu.espe.crealtivastudios.model.Event;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Daniel
 */

public class EventController {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String registerEvent(
            int customerId,
            String eventName,
            Date eventDate,
            int eventTypeCode
    ) {

        if (eventName == null || eventName.trim().isEmpty()) {
            return "El nombre del evento es obligatorio";
        }

        if (eventDate == null) {
            return "Debe seleccionar una fecha";
        }

        Date today = new Date();
        if (eventDate.before(today)) {
            return "La fecha del evento no puede ser anterior a hoy";
        }

        if (eventTypeCode < 1 || eventTypeCode > 4) {
            return "Tipo de evento invalido";
        }

        Customer customer = Customer.findCustomerById(customerId);
        if (customer == null) {
            return "Cliente no encontrado";
        }

        String formattedDate = FORMAT.format(eventDate);

        Event event = customer.addEvent(
                eventName,
                formattedDate,
                eventTypeCode
        );

        if (event == null) {
            return "No se pudo registrar el evento";
        }

        return "OK";
    }
}
