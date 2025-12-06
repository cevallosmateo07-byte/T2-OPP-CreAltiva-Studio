package ec.edu.espe.crealtivastudio.controller;

import com.sun.jdi.IntegerType;
import com.toedter.calendar.JDateChooser;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

/**
 *
 * @author Mateo Cevallos
 */
public class FrameValidations {
  public static boolean validateClientSearcher(String txtId){
      if(txtId == null || txtId.trim().isEmpty()){
          return false;
      }
      
      try{
          Integer.parseInt(txtId);
          return true;          
      }catch(NumberFormatException number){
          return false;
      }
      
  }
  
  public static boolean validateDateChooser(JDateChooser dateChooser){
      if(dateChooser.getDate() == null){
          JOptionPane.showMessageDialog(null, "Debe seleccionar fecha", "Error", JOptionPane.ERROR_MESSAGE);
          return false;
      }
      
      Date selectionedDate = dateChooser.getDate();
      Date today = new Date();
      
      if(selectionedDate.after(today)){
          JOptionPane.showMessageDialog(null, "La fecha no puede ser futura", "Error", JOptionPane.ERROR_MESSAGE);
          return false;
      }
      
      if(selectionedDate.before(today)){
          JOptionPane.showMessageDialog(null, "La fecha no puede ser anterior a hoy", "Error",  JOptionPane.ERROR_MESSAGE);
          return false;
      }
      
      return true;
  }
  public static boolean validateDisccount(JSpinner spnDisccount){
    try{
      int disccount = (int) spnDisccount.getValue();
      return disccount >= 0 && disccount <= 100;
    }catch(Exception exception){
        return false;
    }
  }
  
  public static boolean validateEventName(String txtEventName){
    return txtEventName != null && txtEventName.trim().length() >= 3;
  }
  
  public static boolean validateAllForm(){
      String id = null;
      JDateChooser dateChooserDate = null;
      JSpinner spnDisccount = null;
      String txtEventName = null;

      return validateClientSearcher(id) && 
             validateDateChooser(dateChooserDate) &&
             validateDisccount(spnDisccount) &&
             validateEventName(txtEventName);
  }
}

