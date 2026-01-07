package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.User;

/**
 *
 * @author Kevin Chalan, OBJECT MASTER, OOP
 */
public class UserController {
      public static  boolean validateLogin(User user) {
      
       return user.getUserName().equals("kevin")&& user.getPasword().equals("chalan");
    }
    
}
