package ec.edu.espe.crealtivastudios.model;

/**
 *
 * @author Kevin Chalan, OBJECT MASTER, OOP
 */
public class User {
     private String userName;
    private String pasword;
    private String Type;

    public User(String userName, String pasword, String Type) {
        this.userName = userName;
        this.pasword = pasword;
        this.Type = Type;
    }

    
    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the pasword
     */
    public String getPasword() {
        return pasword;
    }

    /**
     * @param pasword the pasword to set
     */
    public void setPasword(String pasword) {
        this.pasword = pasword;
    }

    /**
     * @return the Type
     */
    public String getType() {
        return Type;
    }

    /**
     * @param Type the Type to set
     */
    public void setType(String Type) {
        this.Type = Type;
    }

}
