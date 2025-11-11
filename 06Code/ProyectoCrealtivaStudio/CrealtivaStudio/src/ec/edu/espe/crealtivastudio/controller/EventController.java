



package ec.edu.espe.crealtivastudio.controller;

import ec.edu.espe.crealtivastudio.model.Event;
import ec.edu.espe.crealtivastudio.model.Customer;
import java.util.List;
/**
 *
 * @author Daniel
 */

public class EventController {
    
    private final EventDao eventDao;
    private final CustomerController customerController; 

    public EventController() {
        this.eventDao = new JsonEventDao(); 
        this.customerController = new CustomerController();
    }
    
    public boolean registerEvent(Event event) {
        // Logica CLAVE: Verificar que el cliente exista
        Customer associatedCustomer = customerController.findCustomerById(event.getCustomerId());
        
        if (associatedCustomer == null) {
            // El cliente no existe, no se puede registrar el evento.
            System.out.println("ERROR: No se puede registrar el evento. El Cliente ID " + event.getCustomerId() + " no existe.");
            return false;
        }
        
        return eventDao.save(event);
    }
    
    public List<Event> getAllEvents() { return eventDao.findAll(); }
    
    public Event findEventById(int eventId) {
        return eventDao.findAll().stream()
                .filter(e -> e.getEventId() == eventId)
                .findFirst()
                .orElse(null);
    }
    
    public boolean updateEventDetails(int eventId, Event updatedEvent) { return eventDao.update(eventId, updatedEvent); }
    public boolean deleteEvent(int eventId) { return eventDao.delete(eventId); }
}
