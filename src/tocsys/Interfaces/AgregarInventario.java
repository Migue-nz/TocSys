/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tocsys.Interfaces;

import java.time.YearMonth;
import javax.swing.JOptionPane;
import tocsys.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author migue
 */
public class AgregarInventario extends javax.swing.JFrame {

    /**
     * Creates new form AgregarInventario
     */
    DefaultTableModel m;

    public AgregarInventario() {
        initComponents();
        m = (DefaultTableModel) tblInventario.getModel();
        cargarInventarioEnTabla();

        // Llenar combos al iniciar solo una vez
        cmbAnio.addItem("Año");
        for (int i = 2025; i <= 2030; i++) {
            cmbAnio.addItem(String.valueOf(i));
        }

        cmbMes.addItem("Mes");
        for (int i = 1; i <= 12; i++) {
            cmbMes.addItem(String.valueOf(i));
        }

        cmbDia.addItem("Día"); // Inicialmente solo esto

        // Eventos para actualizar días válidos
        cmbMes.addActionListener(e -> LlenarDias());
        cmbAnio.addActionListener(e -> LlenarDias());
        txtidProducto.addActionListener(e -> buscarProductoPorId());

    }

    public void LlenarDias() {
        // Validar que el usuario haya seleccionado opciones válidas
        if (cmbMes.getSelectedIndex() > 0 && cmbAnio.getSelectedIndex() > 0) {
            int mes = Integer.parseInt(cmbMes.getSelectedItem().toString());
            int anio = Integer.parseInt(cmbAnio.getSelectedItem().toString());

            int diasEnMes = YearMonth.of(anio, mes).lengthOfMonth();

            cmbDia.removeAllItems(); // Limpiar antes de llenar
            cmbDia.addItem("Día");

            for (int i = 1; i <= diasEnMes; i++) {
                cmbDia.addItem(String.valueOf(i));
            }
        } else {
            // Si no hay selección válida, solo mostrar el texto inicial
            cmbDia.removeAllItems();
            cmbDia.addItem("Día");
        }
    }

    //-----------------------------------------------------------------------------------------
    private void registrarInventario() {
        // Validación de campos vacíos o mal seleccionados
        if (txtidProducto.getText().trim().isEmpty() || txtCntidadLote.getText().trim().isEmpty()
                || cmbAnio.getSelectedIndex() == 0 || cmbMes.getSelectedIndex() == 0 || cmbDia.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor completa todos los campos correctamente.");
            return;
        }

        try {
            int idProducto = Integer.parseInt(txtidProducto.getText().trim());
            int cantidad = Integer.parseInt(txtCntidadLote.getText().trim());

            int anio = Integer.parseInt(cmbAnio.getSelectedItem().toString());
            int mes = Integer.parseInt(cmbMes.getSelectedItem().toString());
            int dia = Integer.parseInt(cmbDia.getSelectedItem().toString());

            LocalDate fechaLocal = LocalDate.of(anio, mes, dia);

            if (fechaLocal.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "La fecha de caducidad no puede ser anterior a hoy.");
                return;
            }

            java.sql.Date fechaCaducidad = java.sql.Date.valueOf(fechaLocal);

            try (Connection conn = ConexionBD.obtenerConexion()) {

                // 1. Verificar si el producto existe y obtener stockMaximo
                String consulta = "SELECT stockMaximo FROM productos WHERE idProducto = ?";
                PreparedStatement ps = conn.prepareStatement(consulta);
                ps.setInt(1, idProducto);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int stockMaximo = rs.getInt("stockMaximo");

                    // 2. Verificar si ya existe el producto con la misma fecha de caducidad
                    String buscar = "SELECT cantidad FROM inventario WHERE idProducto = ? AND caducidad = ?";
                    PreparedStatement psBuscar = conn.prepareStatement(buscar);
                    psBuscar.setInt(1, idProducto);
                    psBuscar.setDate(2, fechaCaducidad);
                    ResultSet rsBuscar = psBuscar.executeQuery();

                    int cantidadTotal;

                    if (rsBuscar.next()) {
                        int cantidadExistente = rsBuscar.getInt("cantidad");
                        cantidadTotal = cantidadExistente + cantidad;

                        // Validar contra stock máximo
                        if (cantidadTotal > stockMaximo) {
                            int opcion = JOptionPane.showConfirmDialog(this,
                                    "Estás intentando agregar una cantidad que supera el stock máximo (" + stockMaximo + ").\n"
                                    + "Cantidad actual: " + cantidadExistente + "\n"
                                    + "Cantidad a agregar: " + cantidad + "\n"
                                    + "Total: " + cantidadTotal + "\n\n¿Deseas continuar de todos modos?",
                                    "Advertencia",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE);

                            if (opcion != JOptionPane.YES_OPTION) {
                                return;
                            }
                        }

                        // Hacer UPDATE
                        String actualizar = "UPDATE inventario SET cantidad = ? WHERE idProducto = ? AND caducidad = ?";
                        PreparedStatement psUpdate = conn.prepareStatement(actualizar);
                        psUpdate.setInt(1, cantidadTotal);
                        psUpdate.setInt(2, idProducto);
                        psUpdate.setDate(3, fechaCaducidad);
                        psUpdate.executeUpdate();

                        JOptionPane.showMessageDialog(this, "Cantidad actualizada correctamente.");

                    } else {
                        // Validar si la cantidad por sí sola excede el máximo
                        if (cantidad > stockMaximo) {
                            int opcion = JOptionPane.showConfirmDialog(this,
                                    "Estás intentando ingresar una cantidad que supera el stock máximo (" + stockMaximo + ").\n"
                                    + "¿Deseas continuar de todos modos?",
                                    "Advertencia",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE);

                            if (opcion != JOptionPane.YES_OPTION) {
                                return;
                            }
                        }

                        // Hacer INSERT
                        String insertar = "INSERT INTO inventario (idProducto, cantidad, caducidad) VALUES (?, ?, ?)";
                        PreparedStatement psInsert = conn.prepareStatement(insertar);
                        psInsert.setInt(1, idProducto);
                        psInsert.setInt(2, cantidad);
                        psInsert.setDate(3, fechaCaducidad);
                        psInsert.executeUpdate();

                        JOptionPane.showMessageDialog(this, "Inventario registrado correctamente.");
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "El producto no existe en la base de datos.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage());
                ex.printStackTrace();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa solo números válidos.");
        }

        cargarInventarioEnTabla();
    }

    //-----------------------------------------------------------------------------------------
    public void cargarInventarioEnTabla() {
        m.setRowCount(0); // Limpiar la tabla antes de cargar

        String sql = "SELECT idProducto, nombre, cantidad, caducidad FROM vista_inventario_detallado";

        try (
                java.sql.Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getInt("idProducto");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getInt("cantidad");
                fila[3] = rs.getDate("caducidad");
                m.addRow(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar inventario: " + e.getMessage());
        }
    }

    //---------------------------------------------------------------------------------------------
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtidProducto = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCntidadLote = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInventario = new javax.swing.JTable();
        cmbDia = new javax.swing.JComboBox<>();
        cmbAnio = new javax.swing.JComboBox<>();
        cmbMes = new javax.swing.JComboBox<>();
        btnActualizar = new javax.swing.JButton();
        btnRegistrar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 48)); // NOI18N
        jLabel1.setText("INVENTARIO");

        jLabel2.setText("Codigo:");

        jLabel3.setText("Caducidad:");

        txtCntidadLote.setText("1");
        txtCntidadLote.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCntidadLoteKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCntidadLoteKeyTyped(evt);
            }
        });

        jLabel4.setText("Cantidad del lote:");

        tblInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Cantidad", "Caducidad"
            }
        ));
        tblInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInventarioMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblInventario);

        btnActualizar.setText("REGISTRAR");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnRegistrar1.setText("ACTUALIZAR");
        btnRegistrar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtidProducto)
                            .addComponent(txtCntidadLote))
                        .addGap(29, 29, 29))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnRegistrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cmbAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(cmbDia, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(598, 598, 598))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtidProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbDia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCntidadLote, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRegistrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCntidadLoteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCntidadLoteKeyPressed
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) && !Character.isISOControl(c)) {
            evt.consume(); // Cancela la entrada si no es un número
        }
    }//GEN-LAST:event_txtCntidadLoteKeyPressed

    private void txtCntidadLoteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCntidadLoteKeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) && !Character.isISOControl(c)) {
            evt.consume(); // Bloquea letras, símbolos, etc.
        }
    }//GEN-LAST:event_txtCntidadLoteKeyTyped

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        registrarInventario();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnRegistrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrar1ActionPerformed
        ActualizarInventario actualizarInventario = new ActualizarInventario();
        actualizarInventario.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegistrar1ActionPerformed

    private void tblInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInventarioMouseClicked
        int fila = tblInventario.getSelectedRow();

        if (fila >= 0) {
            String idProducto = tblInventario.getValueAt(fila, 0).toString();
            String cantidad = tblInventario.getValueAt(fila, 2).toString();
            String fecha = tblInventario.getValueAt(fila, 3).toString(); // formato yyyy-MM-dd

            // Separar la fecha
            String[] partesFecha = fecha.split("-");
            String anio = partesFecha[0];
            String mes = String.valueOf(Integer.parseInt(partesFecha[1])); // quitar ceros a la izquierda
            String dia = String.valueOf(Integer.parseInt(partesFecha[2]));

            // Llenar los campos del formulario
            txtidProducto.setText(idProducto);
            txtCntidadLote.setText(cantidad);
            cmbAnio.setSelectedItem(anio);
            cmbMes.setSelectedItem(mes);
            cmbDia.setSelectedItem(dia);
        }

    }//GEN-LAST:event_tblInventarioMouseClicked
//---------------------------------------------------------------------------------------------------

    private void buscarProductoPorId() {
        String idTexto = txtidProducto.getText().trim();

        if (idTexto.isEmpty()) {
            cargarInventarioEnTabla(); // Cargar todo si no hay nada
            return;
        }

        try {
            int idBuscado = Integer.parseInt(idTexto);
            m.setRowCount(0); // Limpiar tabla

            String sql = "SELECT idProducto, nombre, cantidad, caducidad FROM vista_inventario_detallado WHERE idProducto = ?";

            try (
                    Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idBuscado);
                ResultSet rs = ps.executeQuery();

                boolean encontrado = false;
                while (rs.next()) {
                    encontrado = true;
                    Object[] fila = new Object[4];
                    fila[0] = rs.getInt("idProducto");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getInt("cantidad");
                    fila[3] = rs.getDate("caducidad");
                    m.addRow(fila);
                }

                if (!encontrado) {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún producto con ese ID.");
                    txtidProducto.setText("");
                    cargarInventarioEnTabla(); // Opcional: volver a cargar todo
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al buscar: " + ex.getMessage());
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido. Ingresa solo números.");
            txtidProducto.setText("");
        }
    }

//----------------------------------------------------------------------------------------------------
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AgregarInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AgregarInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AgregarInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AgregarInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AgregarInventario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnRegistrar1;
    private javax.swing.JComboBox<String> cmbAnio;
    private javax.swing.JComboBox<String> cmbDia;
    private javax.swing.JComboBox<String> cmbMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblInventario;
    private javax.swing.JTextField txtCntidadLote;
    private javax.swing.JTextField txtidProducto;
    // End of variables declaration//GEN-END:variables
}
