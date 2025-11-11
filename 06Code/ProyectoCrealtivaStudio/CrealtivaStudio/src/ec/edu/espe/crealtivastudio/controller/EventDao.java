



package ec.edu.espe.crealtivastudio.controller;

import ec.edu.espe.crealtivastudio.model.Event;
import java.util.List;
/**
 *
 * @author Daniel
 */
public interface EventDao {
    boolean save(Event event);
    List<Event> findAll();
    boolean update(int id, Event event);
    boolean delete(int id);
}