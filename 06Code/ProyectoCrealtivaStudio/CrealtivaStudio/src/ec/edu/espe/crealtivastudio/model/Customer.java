
package ec.edu.espe.crealtivastudio.model;

/**
 *
 * @author Daniel
 */
public class Customer {
    private int id; 
    private String name;
    private String phone;
    private String email;
    private String address;

   @Override
    public String toString() {
        // Se mantiene solo la línea de separación inferior y los datos
        String separator = "--------------------------------------";
        return separator + "\n" +
               "| ID:\t\t" + this.getId() + "\n" +
               "| Nombre:\t" + this.getName() + "\n" +
               "| Telefono:\t" + this.getPhone() + "\n" +
               "| Email:\t" + this.getEmail() + "\n" +
               "| Direccion:\t" + this.getAddress() + "\n" +
               separator;
    }

    public Customer(int id, String name, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
    
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


   
}