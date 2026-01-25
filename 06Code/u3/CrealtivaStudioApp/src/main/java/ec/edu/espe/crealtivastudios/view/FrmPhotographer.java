package ec.edu.espe.crealtivastudios.view;

import ec.edu.espe.crealtivastudios.controller.MenuController;
import ec.edu.espe.crealtivastudios.controller.PhotographerController;
import javax.swing.DefaultListModel;

public class FrmPhotographer extends javax.swing.JFrame {

    
    private final PhotographerController controller = new PhotographerController();
    private final MenuController menuController = new MenuController();
    
  
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public FrmPhotographer() {
        initComponents();
        this.setLocationRelativeTo(null);
        
        
        listAssigned.setModel(listModel);
        updateUI(); 
        
       
        listAssigned.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                txtSelectedPhotographer.setText(listAssigned.getSelectedValue());
            }
        });
    }
    
    private void updateUI() {
    
        controller.fillListModel(listModel);
        

        controller.updateStatusLabel(lblMicaelaStatus, "Micaela Garcia");
        controller.updateStatusLabel(lblLuisaStatus, "Luisa Andrade");
        controller.updateStatusLabel(lblAlexisStatus, "Alexis Fares");
        controller.updateStatusLabel(lblPaolaStatus, "Paola Maza");
        
        
        txtSelectedPhotographer.setText("Ninguno");
    }
    

  /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: DO NOT MODIFY THIS CODE EXCEPT PARA LA INICIALIZACIÓN DE NUEVOS COMPONENTES.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnAssign1 = new javax.swing.JButton();
        btnAssign2 = new javax.swing.JButton();
        btnAssign3 = new javax.swing.JButton();
        btnAssign4 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtSelectedPhotographer = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listAssigned = new javax.swing.JList<>();
        btnAssignEquipment = new javax.swing.JButton();
        lblMicaelaStatus = new javax.swing.JLabel();
        lblLuisaStatus = new javax.swing.JLabel();
        lblAlexisStatus = new javax.swing.JLabel();
        lblPaolaStatus = new javax.swing.JLabel();
        btnOpenAvailability = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        AsignEvent = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(java.awt.SystemColor.activeCaption);

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel1.setText("Asignamiento de Fotografos");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 24, Short.MAX_VALUE))
        );

        jLabel2.setText("Lista de Fotografos");

        jLabel3.setText("Micaela Garcia");

        jLabel4.setText("Luisa Andrade");

        jLabel5.setText("Alexis Fares");

        jLabel6.setText("Paola Maza");

        btnAssign1.setText("ok");
        btnAssign1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssign1ActionPerformed(evt);
            }
        });

        btnAssign2.setText("ok");
        btnAssign2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssign2ActionPerformed(evt);
            }
        });

        btnAssign3.setText("ok");

        btnAssign4.setText("ok");

        jLabel7.setText("Usted selecciono a ");

        txtSelectedPhotographer.setText("fotógrafo");

        jLabel8.setText(".");

        jLabel9.setText("Accion");

        listAssigned.setModel(new DefaultListModel<String>()
        );
        jScrollPane1.setViewportView(listAssigned);

        btnAssignEquipment.setText("Asignar equipos");
        btnAssignEquipment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignEquipmentActionPerformed(evt);
            }
        });

        lblMicaelaStatus.setText(".");

        lblLuisaStatus.setText(".");

        lblAlexisStatus.setText(".");

        lblPaolaStatus.setText(".");

        btnOpenAvailability.setText("Cambiar Estado");
        btnOpenAvailability.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenAvailabilityActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btnAssign1, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnAssign2, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnAssign3, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(btnAssign4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMicaelaStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblLuisaStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                                .addGap(134, 134, 134))
                            .addComponent(lblAlexisStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPaolaStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtSelectedPhotographer, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(btnOpenAvailability)
                                .addGap(75, 75, 75)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAssignEquipment))
                .addGap(55, 55, 55))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(btnAssign1)
                            .addComponent(lblMicaelaStatus))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(btnAssign2)
                            .addComponent(lblLuisaStatus))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(btnAssign3)
                            .addComponent(lblAlexisStatus))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(btnAssign4)
                            .addComponent(lblPaolaStatus)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSelectedPhotographer)
                    .addComponent(jLabel8)
                    .addComponent(btnOpenAvailability)
                    .addComponent(btnAssignEquipment))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jPanel4.setBackground(java.awt.SystemColor.activeCaption);

        btnSave.setText("Guardar");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Regresar al Menu");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnRemove.setText("Eliminar");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnSave)
                .addGap(18, 18, 18)
                .addComponent(btnRemove)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(196, 196, 196))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnRemove)
                    .addComponent(jButton1))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jMenu1.setText("Asignar Evento");

        AsignEvent.setText("Disponibilidad");
        AsignEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AsignEventActionPerformed(evt);
            }
        });
        jMenu1.add(AsignEvent);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(36, 36, 36)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAssignEquipmentActionPerformed(java.awt.event.ActionEvent evt) {                                                   
    FrmEquipment frmEquipment = new FrmEquipment();
    frmEquipment.setVisible(true);
    this.dispose(); 
    }
    
    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {                                          
   String selected = (String) listAssigned.getSelectedValue();
    
    
    if (selected == null) {
        javax.swing.JOptionPane.showMessageDialog(this, "Por favor, seleccione un fotógrafo de la lista.");
        return;
    }

   
    controller.deletePhotographer(
            selected, 
            () -> this.updateUI(), 
            this
    );
}


private void btnAssign3ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        controller.selectInListByName(listAssigned, "Alexis Fares");
    }    

private void btnAssign4ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        controller.selectInListByName(listAssigned, "Paola Maza");
    }   



    private void btnAssign1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssign1ActionPerformed
controller.selectInListByName(listAssigned, "Micaela Garcia");
    
    }//GEN-LAST:event_btnAssign1ActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
updateUI();
        javax.swing.JOptionPane.showMessageDialog(this, "Datos actualizados.");
    
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnAssign2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssign2ActionPerformed
controller.selectInListByName(listAssigned, "Luisa Andrade");
    
    }//GEN-LAST:event_btnAssign2ActionPerformed

    private void btnOpenAvailabilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenAvailabilityActionPerformed
     FrmAvailable frmAvailable = new FrmAvailable();
    frmAvailable.setVisible(true);
    this.dispose(); 
    }//GEN-LAST:event_btnOpenAvailabilityActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      new FrmCrealtivaStudiosMenu().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void AsignEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AsignEventActionPerformed
    FrmAssignEvent frmAssignEvent = new FrmAssignEvent();
    frmAssignEvent.setVisible(true);
    this.dispose();          
    }//GEN-LAST:event_AsignEventActionPerformed


    public static void main(String args[]) {
  try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {}
        
        java.awt.EventQueue.invokeLater(() -> new FrmPhotographer().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AsignEvent;
    private javax.swing.JButton btnAssign1;
    private javax.swing.JButton btnAssign2;
    private javax.swing.JButton btnAssign3;
    private javax.swing.JButton btnAssign4;
    private javax.swing.JButton btnAssignEquipment;
    private javax.swing.JButton btnOpenAvailability;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAlexisStatus;
    private javax.swing.JLabel lblLuisaStatus;
    private javax.swing.JLabel lblMicaelaStatus;
    private javax.swing.JLabel lblPaolaStatus;
    private javax.swing.JList<String> listAssigned;
    private javax.swing.JLabel txtSelectedPhotographer;
    // End of variables declaration//GEN-END:variables
}