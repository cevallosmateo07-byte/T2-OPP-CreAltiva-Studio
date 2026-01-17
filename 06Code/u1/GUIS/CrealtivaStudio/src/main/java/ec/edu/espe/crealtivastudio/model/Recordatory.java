package ec.edu.espe.crealtivastudio.model;

import java.util.Date;

/**
 *
 * @author Mateo Cevallos
 */
public class Recordatory {
    private Date date;
    private String eventName;
    private boolean activate;
    
    public Recordatory(){};

    public Recordatory(Date date, String eventName, boolean activate) {
        this.date = date;
        this.eventName = eventName;
        this.activate = activate;
    }
    
    
}
