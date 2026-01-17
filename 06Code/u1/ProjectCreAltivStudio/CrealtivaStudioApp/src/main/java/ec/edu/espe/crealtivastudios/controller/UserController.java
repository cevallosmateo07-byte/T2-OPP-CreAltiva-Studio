package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.User;

/**
 *
 * @author Kevin Chalan, OBJECT MASTER, OOP
 */
public class UserController {
      public static  boolean validateLogin(User user) {
      // read information from the database with the user and pasword
       return user.getUserName().equals("kevin")&& user.getPasword().equals("chalan");
    }
    
}
