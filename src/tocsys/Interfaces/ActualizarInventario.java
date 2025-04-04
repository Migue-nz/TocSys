/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tocsys.Interfaces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import tocsys.ConexionBD;

/**
 *
 * @author migue
 */
public class ActualizarInventario extends javax.swing.JFrame {

    /**
     * Creates new form ActualizarInventario
     */
    DefaultTableModel m;

    public ActualizarInventario() {
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
        txtProductoid.addActionListener(e -> filtrarPorIdProducto());

    }
    //------------------------------------------------------------------------

    private java.sql.Date obtenerFechaDesdeCombos() {
        try {
            int anio = Integer.parseInt(cmbAnio.getSelectedItem().toString());
            int mes = Integer.parseInt(cmbMes.getSelectedItem().toString());
            int dia = Integer.parseInt(cmbDia.getSelectedItem().toString());

            return java.sql.Date.valueOf(java.time.LocalDate.of(anio, mes, dia));
        } catch (Exception e) {
            return null; // Si los combos no están bien seleccionados
        }
    }

    //---------------------------------------------------------------------------------------
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
    private void filtrarPorIdProducto() {
        String idTexto = txtProductoid.getText().trim();

        if (idTexto.isEmpty()) {
            cargarInventarioEnTabla(); // Si está vacío, cargar todo
            return;
        }

        try {
            int idBuscado = Integer.parseInt(idTexto);
            m.setRowCount(0); // Limpiar tabla

            String sql = "SELECT idProducto, nombre, cantidad, caducidad FROM vista_inventario_detallado WHERE idProducto = ?";

            try (
                    java.sql.Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idBuscado);
                ResultSet rs = ps.executeQuery();

                boolean hayResultados = false;
                while (rs.next()) {
                    hayResultados = true;
                    Object[] fila = new Object[4];
                    fila[0] = rs.getInt("idProducto");
                    fila[1] = rs.getString("nombre");
                    fila[2] = rs.getInt("cantidad");
                    fila[3] = rs.getDate("caducidad");
                    m.addRow(fila);
                }

                if (!hayResultados) {
                    lblFechaAnterior.setText("Producto no encontrado");
                } else {
                    lblFechaAnterior.setText("Selecciona una fila");
                }

            } catch (SQLException ex) {
                lblFechaAnterior.setText("Error al buscar: " + ex.getMessage());
            }

        } catch (NumberFormatException ex) {
            lblFechaAnterior.setText("ID inválido");
        }
    }
//-----------------------------------------------------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cmbDia = new javax.swing.JComboBox<>();
        cmbAnio = new javax.swing.JComboBox<>();
        cmbMes = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        lblFechaAnterior = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        txtCantidad = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInventario = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtProductoid = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 48)); // NOI18N
        jLabel1.setText("ACTUALIZA INVENTARIO ");

        jLabel3.setText("Caducidad nueva:");

        lblFechaAnterior.setText("AAAA-MM-DD");

        jLabel4.setText("Caducidad actual:");

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

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
        tblInventario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInventarioMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblInventario);

        jLabel2.setText("Cantidad a actualizar o eliminar:");

        jLabel5.setText("Producto:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblFechaAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProductoid, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(btnActualizar)
                            .addGap(89, 89, 89)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(txtCantidad, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(cmbAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addComponent(cmbDia, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(40, 40, 40)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFechaAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtProductoid, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbMes, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbDia, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed

        int fila = tblInventario.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Primero selecciona un producto en la tabla.");
            return;
        }

        String idProducto = tblInventario.getValueAt(fila, 0).toString();
        String fechaActual = tblInventario.getValueAt(fila, 3).toString();
        java.sql.Date fechaActualSQL = java.sql.Date.valueOf(fechaActual);

        java.sql.Date fechaNuevaSQL = obtenerFechaDesdeCombos();
        if (fechaNuevaSQL == null) {
            JOptionPane.showMessageDialog(this, "Selecciona una fecha nueva válida.");
            return;
        }

        if (fechaNuevaSQL.equals(fechaActualSQL)) {
            JOptionPane.showMessageDialog(this, "La nueva fecha no puede ser igual a la actual.");
            return;
        }

        int cantidadMover;
        try {
            cantidadMover = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidadMover <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Deseas mover " + cantidadMover + " unidades del lote con fecha " + fechaActual + " a la nueva fecha " + fechaNuevaSQL + "?",
                "Confirmar actualización",
                JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try (java.sql.Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Verificar cantidad actual del producto en la fecha actual
            String consultaCantidad = "SELECT cantidad FROM inventario WHERE idProducto = ? AND caducidad = ?";
            PreparedStatement psConsulta = conn.prepareStatement(consultaCantidad);
            psConsulta.setInt(1, Integer.parseInt(idProducto));
            psConsulta.setDate(2, fechaActualSQL);
            ResultSet rs = psConsulta.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "No se encontró el lote original con esa fecha.");
                conn.rollback();
                return;
            }

            int cantidadExistente = rs.getInt("cantidad");
            if (cantidadMover > cantidadExistente) {
                JOptionPane.showMessageDialog(this, "No hay suficientes unidades para mover.");
                conn.rollback();
                return;
            }

            // 2. Restar del lote actual
            String actualizar = "UPDATE inventario SET cantidad = cantidad - ? WHERE idProducto = ? AND caducidad = ?";
            PreparedStatement psActualizar = conn.prepareStatement(actualizar);
            psActualizar.setInt(1, cantidadMover);
            psActualizar.setInt(2, Integer.parseInt(idProducto));
            psActualizar.setDate(3, fechaActualSQL);
            psActualizar.executeUpdate();

            // 3. Eliminar el lote si quedó en 0
            String verificarCantidad = "SELECT cantidad FROM inventario WHERE idProducto = ? AND caducidad = ?";
            PreparedStatement psVerificar = conn.prepareStatement(verificarCantidad);
            psVerificar.setInt(1, Integer.parseInt(idProducto));
            psVerificar.setDate(2, fechaActualSQL);
            ResultSet rsVerificar = psVerificar.executeQuery();

            if (rsVerificar.next() && rsVerificar.getInt("cantidad") == 0) {
                String eliminarCero = "DELETE FROM inventario WHERE idProducto = ? AND caducidad = ?";
                PreparedStatement psEliminarCero = conn.prepareStatement(eliminarCero);
                psEliminarCero.setInt(1, Integer.parseInt(idProducto));
                psEliminarCero.setDate(2, fechaActualSQL);
                psEliminarCero.executeUpdate();
            }

            // 4. Verificar si ya existe con la nueva fecha
            String buscarNuevo = "SELECT cantidad FROM inventario WHERE idProducto = ? AND caducidad = ?";
            PreparedStatement psBuscarNuevo = conn.prepareStatement(buscarNuevo);
            psBuscarNuevo.setInt(1, Integer.parseInt(idProducto));
            psBuscarNuevo.setDate(2, fechaNuevaSQL);
            ResultSet rsNuevo = psBuscarNuevo.executeQuery();

            if (rsNuevo.next()) {
                // Ya existe → sumamos la cantidad
                String sumar = "UPDATE inventario SET cantidad = cantidad + ? WHERE idProducto = ? AND caducidad = ?";
                PreparedStatement psSumar = conn.prepareStatement(sumar);
                psSumar.setInt(1, cantidadMover);
                psSumar.setInt(2, Integer.parseInt(idProducto));
                psSumar.setDate(3, fechaNuevaSQL);
                psSumar.executeUpdate();
            } else {
                // No existe → insertamos nuevo lote
                String insertar = "INSERT INTO inventario (idProducto, cantidad, caducidad) VALUES (?, ?, ?)";
                PreparedStatement psInsertar = conn.prepareStatement(insertar);
                psInsertar.setInt(1, Integer.parseInt(idProducto));
                psInsertar.setInt(2, cantidadMover);
                psInsertar.setDate(3, fechaNuevaSQL);
                psInsertar.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Inventario actualizado correctamente.");
            cargarInventarioEnTabla();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void tblInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInventarioMouseClicked
        int fila = tblInventario.getSelectedRow();

        if (fila >= 0) {
            // Obtener ID y fecha de la fila seleccionada
            String idProducto = tblInventario.getValueAt(fila, 0).toString();
            String fecha = tblInventario.getValueAt(fila, 3).toString(); // formato yyyy-MM-dd

            // Mostrar la fecha en la etiqueta
            lblFechaAnterior.setText(fecha);

            // Llenar el campo del ID de producto
            txtProductoid.setText(idProducto);
        }
    }//GEN-LAST:event_tblInventarioMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int fila = tblInventario.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto de la tabla para eliminar.");
            return;
        }

        String idProducto = tblInventario.getValueAt(fila, 0).toString();
        String fecha = tblInventario.getValueAt(fila, 3).toString();
        java.sql.Date fechaSQL = java.sql.Date.valueOf(fecha);

        int cantidadAEliminar;
        try {
            cantidadAEliminar = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidadAEliminar <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingresa una cantidad válida para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de eliminar " + cantidadAEliminar + " unidades del producto ID " + idProducto + " con fecha " + fecha + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try (java.sql.Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false); // Iniciar transacción

            // Verificar la cantidad actual
            String consulta = "SELECT cantidad FROM inventario WHERE idProducto = ? AND caducidad = ?";
            PreparedStatement psConsulta = conn.prepareStatement(consulta);
            psConsulta.setInt(1, Integer.parseInt(idProducto));
            psConsulta.setDate(2, fechaSQL);
            ResultSet rs = psConsulta.executeQuery();

            if (rs.next()) {
                int cantidadExistente = rs.getInt("cantidad");

                if (cantidadAEliminar < cantidadExistente) {
                    // Solo restar
                    String actualizar = "UPDATE inventario SET cantidad = cantidad - ? WHERE idProducto = ? AND caducidad = ?";
                    PreparedStatement psUpdate = conn.prepareStatement(actualizar);
                    psUpdate.setInt(1, cantidadAEliminar);
                    psUpdate.setInt(2, Integer.parseInt(idProducto));
                    psUpdate.setDate(3, fechaSQL);
                    psUpdate.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Se eliminaron " + cantidadAEliminar + " unidades.");
                } else {
                    // Eliminar el registro completo
                    String eliminar = "DELETE FROM inventario WHERE idProducto = ? AND caducidad = ?";
                    PreparedStatement psDelete = conn.prepareStatement(eliminar);
                    psDelete.setInt(1, Integer.parseInt(idProducto));
                    psDelete.setDate(2, fechaSQL);
                    psDelete.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Se eliminó completamente el lote.");
                }

                conn.commit();
                cargarInventarioEnTabla();

            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el lote seleccionado.");
                conn.rollback();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

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
            java.util.logging.Logger.getLogger(ActualizarInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ActualizarInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ActualizarInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ActualizarInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ActualizarInventario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JComboBox<String> cmbAnio;
    private javax.swing.JComboBox<String> cmbDia;
    private javax.swing.JComboBox<String> cmbMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFechaAnterior;
    private javax.swing.JTable tblInventario;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtProductoid;
    // End of variables declaration//GEN-END:variables
}
