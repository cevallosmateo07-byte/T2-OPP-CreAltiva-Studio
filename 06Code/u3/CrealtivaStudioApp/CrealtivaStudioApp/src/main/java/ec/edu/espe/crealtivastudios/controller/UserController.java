package ec.edu.espe.crealtivastudios.controller;

import ec.edu.espe.crealtivastudios.model.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class UserController {

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (int i = 0; i < encodedhash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean validateLogin(User user) {

        String passwordRealHash = "f4cd4e68a4f501aa8091107176d1efee410569dcd5e385109cfdd49535c849a0";
        
        String inputPasswordHash = hashPassword(user.getPasword());

        
        return user.getUserName().trim().equals("kevin") && inputPasswordHash.equals(passwordRealHash);
    }
}