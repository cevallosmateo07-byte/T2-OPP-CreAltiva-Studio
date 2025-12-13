package ec.edu.espe.crealtivastudios.view;
import ec.edu.espe.crealtivastudios.model.Equipment;
import ec.edu.espe.crealtivastudios.model.Photographer;
import utils.MongoConnection;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.logging.Level;
import org.bson.Document;

/**
 *
 * @author Kevin Chalan, OBJECT MASTER, OOP
 */
public class FrmEquipment extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmEquipment.class.getName());
    private DefaultListModel<String> listEquipmentModel = new DefaultListModel<>();
 

    private Photographer photographerContext = null; // Variable de contexto

    /**
     * Creates new form FrmEquipment (MODO GLOBAL)
     */

  public FrmEquipment(Photographer p) {
 initComponents();
    
   
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); 

    this.photographerContext = p;

    listEquipment.setModel(listEquipmentModel);

    setTitle("Asignar Equipo a: " + p.getName());

   
    if (this.photographerContext != null) {
       
        jButton6.setText("Retirar Equipo"); 
    } else {
        
        jButton6.setText("Borrar"); 
    }

 
    ensureInitialEquipmentExists(); 
    loadEquipmentAndPhotographerStatus(); 

    jTextField1.setEditable(false);
}

  private void ensureInitialEquipmentExists() {


    Photographer tempNullPhotographer = null;
    

    createOrUpdateEquipment("Camera EOS R5", "Cámara Profesional Canon");
    createOrUpdateEquipment("Tripod PRO X", "Trípode de Aluminio");
    createOrUpdateEquipment("LED Light Panel", "Iluminación Suave");
    createOrUpdateEquipment("Microphone Shotgun", "Micrófono Direccional");
    createOrUpdateEquipment("Softbox 90cm", "Iluminación Suave");
    
    
}
  private void createOrUpdateEquipment(String name, String description) {
    String nameNorm = name.trim();
    
    
    MongoConnection.connect();
    var collection = MongoConnection.getEquipmentCollection();
    var existingDoc = collection.find(new Document("name", nameNorm)).first();
    
    if (existingDoc == null) {
       
        Equipment eq = new Equipment(nameNorm, description);
        eq.save();
    }
}


    private void loadEquipmentAndPhotographerStatus() {
      listEquipmentModel.clear();
    MongoConnection.connect();
    var collection = MongoConnection.getEquipmentCollection();

 
    java.util.Set<String> assignedNormalized = new java.util.HashSet<>();
    if (photographerContext != null) {
        for (String n : photographerContext.getEquipment()) {
            if (n != null) assignedNormalized.add(n.trim().toLowerCase());
        }
    }

    for (var doc : collection.find()) {
        Equipment eq = new Equipment(
            doc.getInteger("equipmentId"),
            doc.getString("name"),
            doc.getString("description"),
            doc.getString("status")
        );

        String displayString = eq.toSimpleString();

        
        String eqNameNorm = (eq.getName() == null) ? "" : eq.getName().trim().toLowerCase();
        if (photographerContext != null && assignedNormalized.contains(eqNameNorm)) {
            displayString += " (ASIGNADO A ESTE FOTÓGRAFO)";
        }

        listEquipmentModel.addElement(displayString);
    }
    }
    
   
    private void addEquipment(String name, String description) {
       if (name == null) return;
    String nameNorm = name.trim();

    if (photographerContext != null) {
       
        java.util.Set<String> assignedNormalized = new java.util.HashSet<>();
        for (String n : photographerContext.getEquipment()) {
            if (n != null) assignedNormalized.add(n.trim().toLowerCase());
        }

        if (assignedNormalized.contains(nameNorm.toLowerCase())) {
            JOptionPane.showMessageDialog(this, "El equipo '" + name + "' ya está asignado a " + photographerContext.getName() + ".");
            return;
        }

        
        photographerContext.getEquipment().add(nameNorm);
        MongoConnection.connect(); 
        boolean ok = photographerContext.save();
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Error al guardar la asignación en la base de datos.");
           
            photographerContext.getEquipment().remove(nameNorm);
            return;
        }

    
        loadEquipmentAndPhotographerStatus();
        JOptionPane.showMessageDialog(this, "Equipo '" + name + "' asignado a " + photographerContext.getName() + ".");

    } else {
        
        for (int i = 0; i < listEquipmentModel.size(); i++) {
            if (listEquipmentModel.get(i).toLowerCase().contains(nameNorm.toLowerCase())) {
                JOptionPane.showMessageDialog(this, "El equipo '" + name + "' ya ha sido asignado.");
                return;
            }
        }
      Equipment eq = new Equipment(nameNorm, description);
    MongoConnection.connect();
    eq.save();
    loadEquipmentAndPhotographerStatus(); 
    }
    jTextField1.setText(nameNorm);
    
    }

   
    private void saveAction() {
        JOptionPane.showMessageDialog(this, "Datos guardados correctamente.");
    }


    private void cancelAction() {
        if (JOptionPane.showConfirmDialog(this, "¿Salir?", "Salir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }
    
    private void toggleEquipmentStatus() {
        int index = listEquipment.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un equipo de la lista para cambiar su estado.");
            return;
        }

        String selectedString = listEquipmentModel.get(index);
        try {
            String idPart = selectedString.split("\\|")[0]; 
            int id = Integer.parseInt(idPart.replace("ID:", "").trim());

            Equipment eq = Equipment.findById(id);

            if (eq != null) {
                if (eq.isAvailable()) {
                    eq.markAsInUse(); 
                } else {
                    eq.markAsAvailable(); 
                }
                
                loadEquipmentAndPhotographerStatus(); 
                
                JOptionPane.showMessageDialog(this, "Estado actualizado a: " + eq.getStatus());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar estado: " + e.getMessage());
        }
    }
    
    private void removeEquipment() {
   int index = listEquipment.getSelectedIndex();
    if (index == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un equipo para realizar la acción.");
        return;
    }

    String selectedString = listEquipmentModel.get(index);
    
    try {
       
        String idPart = selectedString.split("\\|")[0]; 
        int id = Integer.parseInt(idPart.replace("ID:", "").trim());

        Equipment eq = Equipment.findById(id);
        if (eq == null) return;


        if (photographerContext != null) {
           
            
            String equipName = eq.getName().trim();

           
            if (!photographerContext.getEquipment().contains(equipName)) {
                 JOptionPane.showMessageDialog(this, "El equipo '" + equipName + "' no está asignado a " + photographerContext.getName() + ".");
                 return;
            }

            int opt = JOptionPane.showConfirmDialog(this, "¿Retirar asignación del equipo '" + equipName + "' de " + photographerContext.getName() + "?", "Confirmar Retiro", JOptionPane.YES_NO_OPTION);

            if (opt == JOptionPane.YES_OPTION) {
              
                photographerContext.getEquipment().remove(equipName);
                photographerContext.save(); 
                
              
                loadEquipmentAndPhotographerStatus(); 
                jTextField1.setText("");
                JOptionPane.showMessageDialog(this, "Asignación del equipo '" + equipName + "' retirada exitosamente.");
            }
            
        } else {
           
            int opt = JOptionPane.showConfirmDialog(this, "¿ELIMINAR PERMANENTEMENTE equipo ID " + id + "?", "Confirmar Borrado", JOptionPane.YES_NO_OPTION);

            if (opt == JOptionPane.YES_OPTION) {
         
                eq.delete(); 
       
                loadEquipmentAndPhotographerStatus(); 
                jTextField1.setText("");
                JOptionPane.showMessageDialog(this, "Equipo eliminado permanentemente.");
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al procesar la acción: " + e.getMessage(), "Error de Procesamiento", JOptionPane.ERROR_MESSAGE);
    }
    }
    



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listEquipment = new javax.swing.JList<>();
        jButton9 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("EQUIPOS");

        jLabel2.setText("LISTA DE EQUIPOS DE FOTOGRAFIA");

        jLabel3.setText(" Camera EOS R5");

        jLabel4.setText("Tripod PRO X ");

        jLabel5.setText("LED Light Panel");

        jLabel6.setText("Microphone Shotgun ");

        jLabel7.setText("Softbox 90cm ");

        jButton1.setText("Asignar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Asignar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Asignar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Asignar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Asignar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel8.setText("Usted selecciono");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel9.setText("(NO editable)");

        jLabel10.setText("Acciones");

        jButton6.setText("Borrar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Guardar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Salir");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        listEquipment.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listEquipment);

        jButton9.setText("Cambiar Estado");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 178, Short.MAX_VALUE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(46, 46, 46)
                                .addComponent(jButton9))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton6)
                                .addGap(18, 18, 18)
                                .addComponent(jButton7)
                                .addGap(18, 18, 18)
                                .addComponent(jButton8))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton4)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton5)
                                .addComponent(jButton3)
                                .addComponent(jButton2)
                                .addComponent(jButton1)))
                        .addGap(46, 46, 46))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton4)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton5)
                                    .addComponent(jLabel7)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addGap(0, 124, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addEquipment("Camera EOS R5", "Cámara Profesional Canon");// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        addEquipment("Tripod PRO X", "Trípode de Aluminio");// TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       addEquipment("Microphone Shotgun", "Micrófono Direccional"); // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        addEquipment("Softbox 90cm", "Iluminación Suave");// TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        removeEquipment();// TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
     saveAction();   // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
       cancelAction(); // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
      addEquipment("LED Light Panel", "Iluminación Suave");// TODO add your handling code here:
   
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        toggleEquipmentStatus();// TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(() -> {
        new FrmPhotographer().setVisible(true);
    });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JList<String> listEquipment;
    // End of variables declaration//GEN-END:variables
}