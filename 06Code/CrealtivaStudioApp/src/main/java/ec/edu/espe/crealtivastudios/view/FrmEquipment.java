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
    
    // 🔥 1. Comportamiento de cierre (DISPOSE_ON_CLOSE)
    // Esto asegura que solo se cierre FrmEquipment, y no toda la aplicación.
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); 

    this.photographerContext = p;

    listEquipment.setModel(listEquipmentModel);

    setTitle("Asignar Equipo a: " + p.getName());

    // 🔥 2. AJUSTE DEL TEXTO DEL BOTÓN (jButton6)
    // Si hay un contexto de fotógrafo, cambiamos "Borrar" a "Retirar Equipo".
    if (this.photographerContext != null) {
        // En el modo de asignación, el botón elimina la asignación, no el equipo global.
        jButton6.setText("Retirar Equipo"); 
    } else {
        // Mantiene 'Borrar' si no hay contexto de fotógrafo (para eliminar el equipo global)
        jButton6.setText("Borrar"); 
    }

    // 3. Lógica de carga
    ensureInitialEquipmentExists(); 
    loadEquipmentAndPhotographerStatus(); 

    jTextField1.setEditable(false);
}

  private void ensureInitialEquipmentExists() {
    // Usamos el comportamiento GLOBAL de addEquipment para crear si no existe en la DB
    // Nota: Necesitas que 'Equipment.findByName()' exista, pero si no, 
    // la lógica de addEquipment() con context=null ya es suficiente 
    // para evitar duplicados en la lista temporal, pero necesitamos guardarlos.

    // Para simplificar, asumiremos que si llamamos a addEquipment con context=null, 
    // se guardará si no existe (tu lógica ya lo hace si no hay duplicados en la JList visual).
    // Sin embargo, para que sea robusto, la clase Equipment debería tener un findByName
    
    // Si no puedes modificar Equipment.java para añadir findByName, esta es la solución:
    
    // Temporalmente, usamos addEquipment con context=null para forzar la creación
    // si el equipo no está ya en la DB.
    
    // Creamos una instancia temporal para forzar el comportamiento de creación:
    Photographer tempNullPhotographer = null;
    
    // Llamar a la lógica de guardado global (donde photographerContext es null)
    // Usamos una versión simplificada de tu addEquipment para evitar el JOPtionPane repetido
    // y solo forzar la persistencia en el inicio, usando la lógica actual:
    
    createOrUpdateEquipment("Camera EOS R5", "Cámara Profesional Canon");
    createOrUpdateEquipment("Tripod PRO X", "Trípode de Aluminio");
    createOrUpdateEquipment("LED Light Panel", "Iluminación Suave");
    createOrUpdateEquipment("Microphone Shotgun", "Micrófono Direccional");
    createOrUpdateEquipment("Softbox 90cm", "Iluminación Suave");
    
    // No necesitamos recargar la lista aquí, ya lo hace loadEquipmentAndPhotographerStatus()
}
  private void createOrUpdateEquipment(String name, String description) {
    String nameNorm = name.trim();
    
    // Revisar si ya existe
    MongoConnection.connect();
    var collection = MongoConnection.getEquipmentCollection();
    var existingDoc = collection.find(new Document("name", nameNorm)).first();
    
    if (existingDoc == null) {
        // Crear y guardar solo si no existe
        Equipment eq = new Equipment(nameNorm, description);
        eq.save();
    }
}

    /**
     * Creates new form FrmEquipment (MODO CONTEXTUAL)
     */
   
    // --- MÉTODOS DE LÓGICA CONTEXTUAL/GLOBAL ---

    // Método principal de carga que unifica la lógica de los antiguos loadEquipmentFromDB y la nueva contextualización.
    private void loadEquipmentAndPhotographerStatus() {
      listEquipmentModel.clear();
    MongoConnection.connect();
    var collection = MongoConnection.getEquipmentCollection();

    // Conjunto normalizado de nombres asignados (trim + lowercase)
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

        // Comparación normalizada
        String eqNameNorm = (eq.getName() == null) ? "" : eq.getName().trim().toLowerCase();
        if (photographerContext != null && assignedNormalized.contains(eqNameNorm)) {
            displayString += " (ASIGNADO A ESTE FOTÓGRAFO)";
        }

        listEquipmentModel.addElement(displayString);
    }
    }
    
    // Función para agregar equipos (reutilizable)
    private void addEquipment(String name, String description) {
       if (name == null) return;
    String nameNorm = name.trim();

    if (photographerContext != null) {
        // Preparar conjunto normalizado para comparación
        java.util.Set<String> assignedNormalized = new java.util.HashSet<>();
        for (String n : photographerContext.getEquipment()) {
            if (n != null) assignedNormalized.add(n.trim().toLowerCase());
        }

        if (assignedNormalized.contains(nameNorm.toLowerCase())) {
            JOptionPane.showMessageDialog(this, "El equipo '" + name + "' ya está asignado a " + photographerContext.getName() + ".");
            return;
        }

        // Añadir y persistir
        photographerContext.getEquipment().add(nameNorm);
        MongoConnection.connect(); // por si acaso
        boolean ok = photographerContext.save();
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Error al guardar la asignación en la base de datos.");
            // intentar quitar el añadido en memoria si falló
            photographerContext.getEquipment().remove(nameNorm);
            return;
        }

        // Recargar la vista (usará comparación normalizada)
        loadEquipmentAndPhotographerStatus();
        JOptionPane.showMessageDialog(this, "Equipo '" + name + "' asignado a " + photographerContext.getName() + ".");

    } else {
        // Comportamiento global: evitar duplicados en la colección/tabla
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

    // Funciones auxiliares para guardar y cancelar
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
                
                loadEquipmentAndPhotographerStatus(); // Recarga para actualizar el estado
                
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
        // Asumiendo que el ID es la primera parte de la cadena
        String idPart = selectedString.split("\\|")[0]; 
        int id = Integer.parseInt(idPart.replace("ID:", "").trim());

        Equipment eq = Equipment.findById(id);
        if (eq == null) return;

        // 🔥 LÓGICA CLAVE: Diferenciar entre Retirar (asignado) y Borrar (global)

        if (photographerContext != null) {
            // --- CASO 1: RETIRAR EQUIPO DEL FOTÓGRAFO ASIGNADO (Solo la asignación) ---
            
            String equipName = eq.getName().trim();

            // Verificar si está asignado a ESTE fotógrafo
            if (!photographerContext.getEquipment().contains(equipName)) {
                 JOptionPane.showMessageDialog(this, "El equipo '" + equipName + "' no está asignado a " + photographerContext.getName() + ".");
                 return;
            }

            int opt = JOptionPane.showConfirmDialog(this, "¿Retirar asignación del equipo '" + equipName + "' de " + photographerContext.getName() + "?", "Confirmar Retiro", JOptionPane.YES_NO_OPTION);

            if (opt == JOptionPane.YES_OPTION) {
                // 1. Remover de la lista del fotógrafo en memoria y en Mongo.
                photographerContext.getEquipment().remove(equipName);
                photographerContext.save(); 
                
                // 2. Opcional: Marcar el equipo como Disponible si estaba En Uso (dejarlo como está si no hay un sistema de inventario más complejo).
                // eq.markAsAvailable(); // <-- Si necesitas cambiar el estado del equipo mismo

                // 3. Recargar la lista visual. Esto QUITARÁ la etiqueta "(ASIGNADO A ESTE FOTÓGRAFO)".
                loadEquipmentAndPhotographerStatus(); 
                jTextField1.setText("");
                JOptionPane.showMessageDialog(this, "Asignación del equipo '" + equipName + "' retirada exitosamente.");
            }
            
        } else {
            // --- CASO 2: BORRAR EQUIPO GLOBALMENTE (eliminar de la colección Equipment) ---
            int opt = JOptionPane.showConfirmDialog(this, "¿ELIMINAR PERMANENTEMENTE equipo ID " + id + "?", "Confirmar Borrado", JOptionPane.YES_NO_OPTION);

            if (opt == JOptionPane.YES_OPTION) {
                // Borrar de la colección de equipos
                eq.delete(); 
                
                // 4. Recargar la lista visual para que desaparezca el ítem completo.
                loadEquipmentAndPhotographerStatus(); 
                jTextField1.setText("");
                JOptionPane.showMessageDialog(this, "Equipo eliminado permanentemente.");
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al procesar la acción: " + e.getMessage(), "Error de Procesamiento", JOptionPane.ERROR_MESSAGE);
    }
    }
    

    // --- CÓDIGO GENERADO DEL DISEÑADOR (SIN MODIFICACIONES AQUÍ) ---

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        listEquipment = new javax.swing.JList<>();
        jButton9 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel5.setBackground(new java.awt.Color(0, 153, 255));

        jLabel2.setFont(new java.awt.Font("sansserif", 2, 14)); // NOI18N
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

        jPanel3.setBackground(new java.awt.Color(0, 153, 255));

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jButton6)
                .addGap(30, 30, 30)
                .addComponent(jButton7)
                .addGap(30, 30, 30)
                .addComponent(jButton8)
                .addContainerGap(482, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton5)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton1)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(jButton2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(104, 104, 104)
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton4)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton5)
                                    .addComponent(jLabel7))))
                        .addGap(13, 13, 13)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel1.setText("EQUIPOS");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        cancelAction(); // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        saveAction();   // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        removeEquipment();// TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        toggleEquipmentStatus();// TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        addEquipment("Softbox 90cm", "Iluminación Suave");// TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        addEquipment("Microphone Shotgun", "Micrófono Direccional"); // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        addEquipment("LED Light Panel", "Iluminación Suave");// TODO add your handling code here:

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        addEquipment("Tripod PRO X", "Trípode de Aluminio");// TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    // --- MANEJO DE EVENTOS ---
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addEquipment("Camera EOS R5", "Cámara Profesional Canon");// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JList<String> listEquipment;
    // End of variables declaration//GEN-END:variables
}