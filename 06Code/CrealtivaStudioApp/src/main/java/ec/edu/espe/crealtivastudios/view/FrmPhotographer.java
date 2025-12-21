package ec.edu.espe.crealtivastudios.view;

import utils.MongoConnection;
import ec.edu.espe.crealtivastudios.model.Photographer;
import java.awt.EventQueue;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import org.bson.Document;
import java.util.logging.Level;
import java.awt.event.WindowEvent;

/**
 *
 * @author Kevin Chalan, OBJECT MASTER, OOP
 */
public class FrmPhotographer extends javax.swing.JFrame {
    private DefaultListModel<String> listAssignedModel = new DefaultListModel<>();
private List<Photographer> photographersCache;

    
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmPhotographer.class.getName());
    private FrmAvailable frmAvailability;
    private FrmEquipment frmEquipment;

    private Photographer selectedPhotographer = null; 
  


    // Los JLabels y JButton declarados para evitar conflictos de ámbito


    /**
     * Creates new form FrmPhotographer
     */
    public FrmPhotographer() { 
     MongoConnection.connect(); 
    ensureInitialRegistration();
    initComponents();
    
    listAssigned.setModel(listAssignedModel);
    loadPhotographers();
    // ✅ Inicializar el modelo ANTES de usarlo
  

    // ✅ Ahora sí es seguro cargar desde la DB
    loadPhotographersFromDB();

    btnAssignEquipment.setText("Asignar Equipos"); 
    btnAssignEquipment.setEnabled(false); 
    btnAssignEquipment.addActionListener(this::btnAssignEquipmentActionPerformed);

    loadAllPhotographersStatus();
    listAssigned.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) { // Solo procesar cuando se detiene la selección
            Photographer selected = getSelectedPhotographer();
            if (selected != null) {
                selectPhotographerByName(selected.getName()); 
            } else {
                txtSelectedPhotographer.setText("fotógrafo");
                btnAssignEquipment.setEnabled(false);
                btnRemove.setEnabled(false);
            }
        }
    });
    btnRemove.setEnabled(false);
    btnSave.setEnabled(false);


    btnAssign1.setText("ok");
    btnAssign2.setText("ok");
    btnAssign3.setText("ok");
    btnAssign4.setText("ok");

    btnRemove.setText("Eliminar");
    btnSave.setText("Guardar");


    btnAssign1.addActionListener(evt -> selectPhotographerByName("Micaela Garcia"));
    btnAssign2.addActionListener(evt -> selectPhotographerByName("Luisa Andrade"));
    btnAssign3.addActionListener(evt -> selectPhotographerByName("Alexis Fares"));
    btnAssign4.addActionListener(evt -> selectPhotographerByName("Paola Maza"));
    btnRemove.addActionListener(evt -> btnRemoveActionPerformed(evt));

    }
    
    // --- LÓGICA DE ESTADO Y SELECCIÓN ---
    
    private Photographer findPhotographerByName(String name) {
        return Photographer.findByName(name);
    }
    

    
    private void updateStatusLabel(String name, JLabel label) {
        Photographer p = findPhotographerByName(name);
        
        if (p != null) {
            if (p.isAssigned()) {
                // Tiene evento -> Listo para equipo -> VERDE
                label.setText("LISTO PARA EQUIPO");
                label.setForeground(new java.awt.Color(0, 153, 51)); 
            } else {
                // Sin evento -> Disponible -> GRIS
                label.setText("DISPONIBLE");
                label.setForeground(java.awt.Color.GRAY);
            }
        } else {
            label.setText("no registrado");
            label.setForeground(java.awt.Color.GRAY);
        }
    }

private Photographer getSelectedPhotographer() {
    int index = listAssigned.getSelectedIndex();

    if (index == -1) {
        return null; // No mostrar JOptionPane aquí, solo devolver null.
    }

    // Retorna el fotógrafo seleccionado de la lista cargada (cache)
    // El índice de la lista visible debe coincidir con el índice del caché.
    if (photographersCache != null && index < photographersCache.size()) {
        return photographersCache.get(index);
    }
    return null;
}



// FrmPhotographer.java (Mantener y verificar esta función)
private void loadPhotographers() {
   listAssignedModel.clear();

    for (Photographer p : Photographer.findAll()) {
        listAssignedModel.addElement(
            p.getName() + " | " +
            (p.isAssigned() ? "OCUPADO" : "DISPONIBLE")
        );
    }
}

private void loadPhotographersFromDB() {
    if (listAssignedModel == null) {
        listAssignedModel = new DefaultListModel<>();
        listAssigned.setModel(listAssignedModel);
    }
    
    // 1. Obtener la lista de todos los fotógrafos
    // Es CRUCIAL que photographersCache se mantenga actualizado para findSelectedPhotographer
    photographersCache = Photographer.findAll(); 

    // 2. Limpiar el modelo visual
    listAssignedModel.clear(); 
    
    // 3. Añadir cada fotógrafo a la lista visual
   for (Photographer p : photographersCache) {
        String estadoTexto;
        // Si tiene evento, está listo para recibir equipo
        if (p.isAssigned()) {
            estadoTexto = "LISTO PARA ASIGNAR EQUIPO (" + p.getAssignedEvent() + ")";
        } else {
            estadoTexto = "DISPONIBLE (Sin evento)";
        }
        
        // Creamos el texto manualmente
        String display = "ID:" + p.getId() + " | " + p.getName() + " [" + estadoTexto + "]";
        listAssignedModel.addElement(display); 
    }
}

private void addPhotographerToList(Photographer p) {
    if (p == null) {
        return;
    }

    // Asegura que el modelo está inicializado
    if (listAssignedModel == null) {
        listAssignedModel = new DefaultListModel<>();
        listAssigned.setModel(listAssignedModel);
    }

    // Intentar detectar por ID para evitar duplicados y permitir actualización
    int pid = p.getId();
    for (int i = 0; i < listAssignedModel.size(); i++) {
        String elem = listAssignedModel.get(i);
        try {
            // Se espera formato "ID:123 | Nombre ...", igual que en toSimpleString()
            String idPart = elem.split("\\|")[0].replace("ID:", "").trim();
            int existingId = Integer.parseInt(idPart);
            if (existingId == pid) {
                // Si ya existe, *actualizamos* la línea (no duplicamos)
                listAssignedModel.set(i, p.toSimpleString());
                return;
            }
        } catch (Exception ex) {
            // Si no podemos parsear, seguimos buscando por nombre como fallback
            if (elem.contains(p.getName())) {
                listAssignedModel.set(i, p.toSimpleString());
                return;
            }
        }
    }

    // Lógica visual personalizada (Misma que arriba)
    String estadoTexto = p.isAssigned() ? "LISTO PARA ASIGNAR EQUIPO" : "DISPONIBLE";
    String display = "ID:" + p.getId() + " | " + p.getName() + " [" + estadoTexto + "]";
    
    // No encontrado -> añadimos
    listAssignedModel.addElement(display);
}


private void btnAssignEquipmentActionPerformed(java.awt.event.ActionEvent evt) {
   Photographer selected = getSelectedPhotographer(); // Debe devolver null si no hay selección

    if (selected == null) {
        JOptionPane.showMessageDialog(this, "Seleccione un fotógrafo de la lista primero.", "Error de Selección", JOptionPane.WARNING_MESSAGE);
        return;
    }

if (!selected.isAssigned()) {
        JOptionPane.showMessageDialog(
            this,
            "El fotógrafo está DISPONIBLE (Sin evento). \nPrimero debe asignarle un evento en el menú principal para poder entregarle equipos.",
            "Falta Asignación",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    try {
        // ✅ Si la ventana ya está abierta → traerla al frente
        if (frmEquipment != null && frmEquipment.isDisplayable()) {
            frmEquipment.toFront();
            frmEquipment.requestFocus();
            return;
        }

        // ✅ Crear la ventana con contexto del fotógrafo
        // ESTO ES CLAVE: Asegúrate de que FrmEquipment use el constructor (Photographer p)
        frmEquipment = new FrmEquipment(selected); 
        frmEquipment.setVisible(true);

        // ✅ Al cerrar FrmEquipment, refrescar la lista
        frmEquipment.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // No es necesario buscar el fotógrafo de nuevo si no se necesita el objeto
                loadPhotographersFromDB(); // refresca visualmente listAssigned
                loadAllPhotographersStatus(); // refresca los labels de estado

                frmEquipment = null;
            }
        });
    } catch (Exception ex) {
        // Capturar cualquier error durante la inicialización o la carga
        logger.log(Level.SEVERE, "Error al abrir FrmEquipment: ", ex);
        JOptionPane.showMessageDialog(this, "Error grave al abrir la ventana de equipos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    // --- CÓDIGO GENERADO Y MÉTODOS AUXILIARES ---

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

        jPanel3.setBackground(new java.awt.Color(0, 153, 255));

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
                        .addComponent(jLabel7)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
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
                                        .addComponent(lblLuisaStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(134, 134, 134))
                                    .addComponent(lblAlexisStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblPaolaStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(120, 120, 120))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtSelectedPhotographer, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(btnOpenAvailability)
                                .addGap(75, 75, 75)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAssignEquipment)
                        .addGap(166, 166, 166))))
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSelectedPhotographer)
                            .addComponent(jLabel8)
                            .addComponent(btnOpenAvailability))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(btnAssignEquipment)))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 153, 255));

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
                .addGap(44, 44, 44))
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
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 784, javax.swing.GroupLayout.PREFERRED_SIZE)
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
private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {                                          
    removePhotographer();
}


private void removePhotographer() {
int index = listAssigned.getSelectedIndex();

    if (index == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un fotógrafo para eliminar");
        return;
    }

    String selected = listAssignedModel.get(index);

    // Extraer ID
    int id = Integer.parseInt(selected.split("\\|")[0].replace("ID:", "").trim());

    int opt = JOptionPane.showConfirmDialog(
            this,
            "¿Eliminar fotógrafo ID " + id + "?",
            "confirme",
            JOptionPane.YES_NO_CANCEL_OPTION
    );

    if (opt == JOptionPane.YES_OPTION) {
        Photographer.deletePhotographer(id);
        listAssignedModel.remove(index);
    }
}
private void savePhotographer() {
  String selected = listAssigned.getSelectedValue();

    if (selected == null) {
        JOptionPane.showMessageDialog(this, "Seleccione un fotógrafo");
        return;
    }

    int opt = JOptionPane.showConfirmDialog(
            this,
            "quiere guardar el fotografo",
            "Confirmar",
            JOptionPane.YES_NO_CANCEL_OPTION
    );

    if (opt == JOptionPane.YES_OPTION) {
        JOptionPane.showMessageDialog(this, "Saved!");
    }
}
private void cancel() {
     int opt = JOptionPane.showConfirmDialog(
            this,
            "Quiere salir",
            "Cancelar",
            JOptionPane.YES_NO_CANCEL_OPTION
    );

    if (opt == JOptionPane.YES_OPTION) {
        this.dispose();
    }
}
   

private void btnAssign3ActionPerformed(java.awt.event.ActionEvent evt) {                                           
 selectPhotographerByName("Alexis Fares");
}     

private void btnAssign4ActionPerformed(java.awt.event.ActionEvent evt) {                                           
 selectPhotographerByName("Paola Maza");
}     

private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {                                          
    cancel(); 
}



    private void btnAssign1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssign1ActionPerformed
selectPhotographerByName("Micaela Garcia");
    }//GEN-LAST:event_btnAssign1ActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
String name = txtSelectedPhotographer.getText();

    int option = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "Quiere guardar la lista",
        "Confirme el guardado",
        javax.swing.JOptionPane.YES_NO_CANCEL_OPTION
    );

    if (option == javax.swing.JOptionPane.YES_OPTION) {
        javax.swing.JOptionPane.showMessageDialog(this, "Lista guardada!");
    }


    
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnAssign2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssign2ActionPerformed
selectPhotographerByName("Luisa Andrade");
    }//GEN-LAST:event_btnAssign2ActionPerformed

    private void btnOpenAvailabilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenAvailabilityActionPerformed
if (frmAvailability == null || !frmAvailability.isDisplayable()) {
        frmAvailability = new FrmAvailable();

        frmAvailability.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // ✅ 1. Recargar LISTA (obtiene datos de MongoDB)
                loadPhotographersFromDB(); 

                // ✅ 2. Recargar ESTADOS visuales
                loadAllPhotographersStatus();

                // ✅ 3. Limpiar selección actual
                selectedPhotographer = null;
                txtSelectedPhotographer.setText("fotógrafo");

                // ✅ 4. Bloquear acciones hasta nueva selección
                btnAssignEquipment.setEnabled(false);
                btnRemove.setEnabled(false);
                btnSave.setEnabled(false);
               
            }
        });
    }

    frmAvailability.setVisible(true);
    }//GEN-LAST:event_btnOpenAvailabilityActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
 FrmCrealtivaStudiosMenu frmCrealtivaStudiosMenu = new FrmCrealtivaStudiosMenu();
 frmCrealtivaStudiosMenu.setVisible(true);
this.setVisible(false);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void AsignEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AsignEventActionPerformed


FrmAssignEvent frmAssignEvent = new FrmAssignEvent();
    frmAssignEvent.setVisible(true);
    this.dispose();           // TODO add your handling code here:
    }//GEN-LAST:event_AsignEventActionPerformed

    /**
     * @param args the command line arguments
     */


    // FrmPhotographer.java

private void selectPhotographerByName(String name) {
 Photographer p = findPhotographerByName(name);

    if (p == null) {
        JOptionPane.showMessageDialog(this,
            "El fotógrafo " + name + " aún no está registrado. Use la función de Registro o verifique la base de datos.",
            "Error de Selección", JOptionPane.ERROR_MESSAGE);

        selectedPhotographer = null;
        txtSelectedPhotographer.setText("No seleccionado");
        btnAssignEquipment.setEnabled(false);
        btnRemove.setEnabled(false);
        btnSave.setEnabled(false);
        
        return;
    }

  selectedPhotographer = p;
    txtSelectedPhotographer.setText(p.getName());

    btnRemove.setEnabled(true);
    btnSave.setEnabled(true);
    

    boolean isAvailable = !p.isAssigned();
    btnAssignEquipment.setEnabled(isAvailable); // 🔥 Esto es lo que bloquea/habilita

  // LÓGICA INVERTIDA: Solo si TIENE evento (isAssigned = true), se activa el botón
    boolean tieneEvento = p.isAssigned(); 
    btnAssignEquipment.setEnabled(tieneEvento); 

    if (!tieneEvento) {
        // Opcional: Avisar por qué no se puede asignar
        // JOptionPane.showMessageDialog(this, "El fotógrafo no tiene evento asignado. Primero asigne un evento.");
    }

    // --- Añadir / actualizar visualmente en la lista (evita duplicados) ---
    addPhotographerToList(p);
}
    public void loadAllPhotographersStatus() {
    updateStatusLabel("Micaela Garcia", lblMicaelaStatus);
    updateStatusLabel("Luisa Andrade", lblLuisaStatus);
    updateStatusLabel("Alexis Fares", lblAlexisStatus);
    updateStatusLabel("Paola Maza", lblPaolaStatus);
    
}
    private void ensureInitialRegistration() {
    String[] names = {"Micaela Garcia", "Luisa Andrade", "Alexis Fares", "Paola Maza"};

    for (String name : names) {
        if (Photographer.findByName(name) == null) {
            // El fotógrafo no existe, lo registramos.
            int newId = Photographer.getNextId();
            // Creamos con assignedEvent = "" (disponible)
            Photographer newPhotographer = new Photographer(newId, name, "", "", true);
            
            if (newPhotographer.save()) {
                logger.log(Level.INFO, "Fotógrafo inicial registrado: " + name);
            } else {
                logger.log(Level.WARNING, "Error al registrar fotógrafo inicial: " + name);
            }
        }
    }
}
    public static void main(String args[]) {
  MongoConnection.connect();

    EventQueue.invokeLater(() -> {
        new FrmPhotographer().setVisible(true);
    });
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