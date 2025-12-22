/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ec.edu.espe.crealtivastudios.view;


import java.awt.Component;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.bson.Document;
import utils.CrudOperations;

/**
 *
 * @author Mateo Cevallos
 */
public class FrmCustomerList extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmCustomerList.class.getName());
    private String selectedCustomerId;
    private String selectedCustomerName;
    private FrmEvent parentFrame;

    /**
     * Creates new form FrmCustomerList
     */
    public FrmCustomerList() {
        initComponents();
        loadCustomersToTable();
    }

    public FrmCustomerList(FrmEvent parent) {
        initComponents();
        this.parentFrame = parent;
        loadCustomersToTable();
    }

    private void loadCustomersToTable() {
        try {
            // Usar CrudOperations para obtener todos los clientes
            List<Document> customers = CrudOperations.findAll("Customers");

            // Crear modelo de tabla no editable
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Definir columnas (usando tu campo id numérico)
            model.addColumn("ID");
            model.addColumn("Nombre");
            model.addColumn("Teléfono");
            model.addColumn("Email");
            model.addColumn("Dirección");

            // Contador para estadísticas
            int customerCount = 0;

            // Llenar tabla con los datos específicos
            for (Document customer : customers) {
                Object[] row = new Object[5];

                // Usar tu campo id numérico
                row[0] = customer.containsKey("id") ? customer.getInteger("id")
                        : customer.containsKey("customerId") ? customer.getInteger("customerId")
                        : "N/A";

                row[1] = customer.containsKey("name") ? customer.getString("name") : "Sin nombre";
                row[2] = customer.containsKey("phone") ? customer.getString("phone") : "Sin teléfono";
                row[3] = customer.containsKey("email") ? customer.getString("email") : "Sin email";
                row[4] = customer.containsKey("address") ? customer.getString("address") : "Sin dirección";

                model.addRow(row);
                customerCount++;
            }

            tblCustomers.setModel(model);

            // Configurar apariencia de la tabla
            configureTableAppearance();

        } catch (Exception e) {
            
        }
    }
    
    private void notifyParentFrame() {
    if (parentFrame != null) {
        // Aquí podrías implementar una interfaz de callback si necesitas
        // pasar datos directamente al frame padre
        JOptionPane.showMessageDialog(parentFrame,
            "Cliente '" + selectedCustomerName + "' (ID: " + selectedCustomerId + 
            ") seleccionado.\nPuede continuar con la asignación del evento.",
            "Cliente seleccionado",
            JOptionPane.INFORMATION_MESSAGE);
    }
}

    /**
     * Configura la apariencia de la tabla
     */
    private void configureTableAppearance() {
        // Configurar anchos de columnas
        int[] columnWidths = {50, 180, 120, 200, 250};
        for (int i = 0; i < columnWidths.length; i++) {
            tblCustomers.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Configurar otras propiedades
        tblCustomers.setRowHeight(25);
        tblCustomers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblCustomers.getTableHeader().setReorderingAllowed(false);

        // Centrar contenido de la columna ID
        tblCustomers.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
    }

    public String getSelectedCustomerId() {
        return selectedCustomerId;
    }

    public String getSelectedCustomerName() {
        return selectedCustomerName;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCustomers = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnSelectCustomer = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblCustomers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblCustomers);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(java.awt.SystemColor.activeCaption);

        btnSelectCustomer.setText("Seleccionar");
        btnSelectCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectCustomerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSelectCustomer)
                .addGap(279, 279, 279))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSelectCustomer)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jMenu1.setText("Salir");

        jMenuItem1.setText("Salir y Cancelar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        FrmEvent frmEvent = new FrmEvent();
        frmEvent.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btnSelectCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectCustomerActionPerformed
       int selectedRow = tblCustomers.getSelectedRow();
    
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, 
            "Por favor, seleccione un cliente de la lista",
            "Selección requerida",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    try {
        // Obtener datos usando tu campo id numérico
        Object idValue = tblCustomers.getValueAt(selectedRow, 0);
        selectedCustomerId = (idValue != null) ? idValue.toString() : "";
        selectedCustomerName = tblCustomers.getValueAt(selectedRow, 1).toString();
        
        // Crear mensaje de confirmación detallado
        StringBuilder message = new StringBuilder();
        message.append("¿Desea asignar el evento al siguiente cliente?\n\n");
        message.append("ID: ").append(selectedCustomerId).append("\n");
        message.append("Nombre: ").append(selectedCustomerName).append("\n");
        message.append("Teléfono: ").append(tblCustomers.getValueAt(selectedRow, 2)).append("\n");
        message.append("Email: ").append(tblCustomers.getValueAt(selectedRow, 3)).append("\n");
        message.append("Dirección: ").append(tblCustomers.getValueAt(selectedRow, 4));
        
        int option = JOptionPane.showConfirmDialog(this, 
            message.toString(),
            "Confirmar selección de cliente",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (option == JOptionPane.YES_OPTION) {
            // Log de la selección
            logger.info("Cliente seleccionado - ID: " + selectedCustomerId + 
                       ", Nombre: " + selectedCustomerName);
            
            // Cerrar ventana
            this.dispose();
            
            // Notificar al frame padre si existe
            notifyParentFrame();
        }
        
    } catch (Exception e) {
        logger.log(java.util.logging.Level.SEVERE, "Error al seleccionar cliente", e);
        JOptionPane.showMessageDialog(this,
            "Error al procesar la selección: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnSelectCustomerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmCustomerList().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSelectCustomer;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCustomers;
    // End of variables declaration//GEN-END:variables
}
