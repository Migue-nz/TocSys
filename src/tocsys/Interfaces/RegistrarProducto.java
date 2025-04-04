/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tocsys.Interfaces;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import tocsys.*;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import javax.swing.table.DefaultTableModel;
import tocsys.ConexionBD;

/**
 *
 * @author danie
 */
public class RegistrarProducto extends javax.swing.JFrame {

    private int codigoProducto;

    public RegistrarProducto() {
        initComponents();
        actualizarTablaCombinada();

    }

    public void Producto() {
        if ("".equals(txtNombre.getText()) || "".equals(txtMarca.getText()) || "".equals(txtDescripcion.getText()) 
                || Integer.parseInt(txtCodigo.getText())  < 1  
                || Integer.parseInt(txtLimiteMin.getText())  < 1 || Integer.parseInt(txtLimiteMax.getText())  < 1   ) {
            JOptionPane.showMessageDialog(this, "No dejes los campos vacios.");
        } else {
            //verificar que el nombre no exista antes del registro
            //boolean existe = nombreExiste( txtNombre.getText());

            //if (existe == true) {
                //JOptionPane.showMessageDialog(this, "Producto duplicado.");

            //} else {
                        agregarProducto();
                        actualizarTablaCombinada();
                        borrartxt();
                        
           // }

        }
    }


   
    public void actualizarTablaCombinada() {
        // Crear modelo de tabla con las columnas que necesitas
        DefaultTableModel modeloTabla = new DefaultTableModel();

        // Configurar columnas
        modeloTabla.addColumn("Codigo");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Marca");
        modeloTabla.addColumn("Descripcion");
        modeloTabla.addColumn("LimiteMin");
        modeloTabla.addColumn("LimiteMax");

       
        try (java.sql.Connection conn = ConexionBD.obtenerConexion(); 
         java.sql.CallableStatement procedimiento = conn.prepareCall("{CALL MostrarProductos()}")) {
            
            
            // Ejecutar el procedimiento almacenado y obtener resultados
        ResultSet resultado = procedimiento.executeQuery();

            
            while (resultado.next()) {
                Object[] fila = {
                    resultado.getInt("idProducto"),
                    resultado.getString("nombre"),
                    resultado.getString("marca"),
                    resultado.getString("descripcion"),
                    resultado.getInt("stockMinimo"),
                    resultado.getInt("stockMaximo")
                };
                modeloTabla.addRow(fila);
            }

            Tabla.setModel(modeloTabla);
            Tabla.revalidate();
            Tabla.repaint();

        } catch (SQLException e) {
            System.err.println("Error al cargar datos combinados: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    

    //metodo si el producto existe
    public boolean nombreExiste( String nombre) {
        String sql = "SELECT COUNT(*) FROM  productos  WHERE nombre = ?";

        try (java.sql.Connection conn = ConexionBD.obtenerConexion(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {  // Usar PreparedStatement en lugar de Statement

            stmt.setString(1, nombre);  // Asigna el valor del parámetro correctamente
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true si el nombre ya existe
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar si el nombre existe: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        return false; // Retorna false si no existe o hay un error
    }

    
    
    public void modificarProductos(int idProducto, String nombre, String marca, String descripcion, int stockMinimo, int stockMaximo) {
        String sqlProducto = "UPDATE productos SET nombre = ?, marca = ?, descripcion = ?,  stockMinimo = ?, stockMaximo = ?  WHERE idProducto = ?";
        
        try (java.sql.Connection conn = ConexionBD.obtenerConexion(); java.sql.PreparedStatement stmtProducto = conn.prepareStatement(sqlProducto);) {
            // 1. Actualizar Producto

            stmtProducto.setString(1, nombre);
            stmtProducto.setString(2, marca);
            stmtProducto.setString(3, descripcion);
            stmtProducto.setInt(4, stockMinimo);
            stmtProducto.setInt(5, stockMaximo);
            stmtProducto.setInt(6, idProducto);
            stmtProducto.executeUpdate();

            

            JOptionPane.showMessageDialog(this, "Actualización exitosa");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());

        }
    }

    public void eliminarProductos(int idProducto) {
        
        try (java.sql.Connection conn = ConexionBD.obtenerConexion(); 
         java.sql.CallableStatement procedimiento = conn.prepareCall("{CALL EliminarProducto(?,?)}")) {
            

            // 1. Luego eliminar el producto
            procedimiento.setInt(1, idProducto);
             procedimiento.registerOutParameter(2, Types.VARCHAR );
            procedimiento.executeUpdate();
           
            String mensaje = procedimiento.getString(2);
        
            // Mostrar mensaje al usuario
            JOptionPane.showMessageDialog(null, mensaje, "Resultado", JOptionPane.INFORMATION_MESSAGE);

            actualizarTablaCombinada();
            

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al Eliminacion: " + e.getMessage());

        }
    }
    
    
    public void agregarProducto(){
    
    
    try (java.sql.Connection conn = ConexionBD.obtenerConexion(); 
         java.sql.CallableStatement procedimiento = conn.prepareCall("{CALL AgregarProducto(?,?,?,?,?,?)}")) {
            
        
        
            procedimiento.setInt(1, Integer.parseInt(txtCodigo.getText()));
            procedimiento.setString(2, txtNombre.getText());
            procedimiento.setString(3, txtMarca.getText());
            procedimiento.setString(4, txtDescripcion.getText());
            procedimiento.setInt(5, Integer.parseInt(txtLimiteMin.getText()));
            procedimiento.setInt(6, Integer.parseInt(txtLimiteMax.getText()));
        
        
            // Ejecutar el procedimiento
            procedimiento.executeQuery();

           System.out.println("Producto agregado correctamente");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
    
    
    
    
    
    
    }
    
    public void borrartxt(){
    
                        txtNombre.setText("");
                        txtMarca.setText("");
                        txtDescripcion.setText("");
                        txtCodigo.setText("");
                        txtLimiteMax.setText("");
                        txtLimiteMin.setText("");
    
    }
    
    
    
    
    
    public void buscarProducto(String texto) {
        DefaultTableModel modeloTabla = new DefaultTableModel();

        // Configurar columnas
        modeloTabla.addColumn("Codigo");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Marca");
        modeloTabla.addColumn("Descripcion");
        modeloTabla.addColumn("LimiteMin");
        modeloTabla.addColumn("LimiteMax");

       String sql = "SELECT idProducto, nombre, marca, descripcion, stockMinimo, stockMaximo FROM productos "
                + "WHERE (nombre LIKE '%" + texto + "%' "
                + "OR marca LIKE '%" + texto + "%' "
                + "OR descripcion LIKE '%" + texto + "%' "
                + "OR idProducto LIKE '%" + texto + "%') "
                + "AND eliminado = 0";


        boolean hayResultados = false;

        try (Connection conn = ConexionBD.obtenerConexion(); Statement stmt = conn.createStatement(); ResultSet resultado = stmt.executeQuery(sql)) {

            while (resultado.next()) {
                hayResultados = true;
                modeloTabla.addRow(new Object[]{
                   resultado.getInt("idProducto"),
                    resultado.getString("nombre"),
                    resultado.getString("marca"),
                    resultado.getString("descripcion"),
                    resultado.getInt("stockMinimo"),
                    resultado.getInt("stockMaximo")
                });
            }

            Tabla.setModel(modeloTabla); // actualiza la tabla incluso si está vacía

            if (!hayResultados) {
                javax.swing.JOptionPane.showMessageDialog(this, "No se encontraron resultados para: " + texto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error al buscar: " + e.getMessage());
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMarca = new javax.swing.JTextField();
        txtBuscador = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        txtLimiteMin = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        btnLupa = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        txtCodigo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtLimiteMax = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(951, 429));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("Registro productos");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 10, 320, 50));

        jLabel2.setText("Marca:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 62, 19));

        jLabel3.setText("Descripcion:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, -1, 19));

        jLabel6.setText("Limite Minimo:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 90, 22));
        getContentPane().add(txtMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 150, -1));

        txtBuscador.setText("Buscador");
        txtBuscador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscadorActionPerformed(evt);
            }
        });
        getContentPane().add(txtBuscador, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 240, -1));

        txtDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescripcionActionPerformed(evt);
            }
        });
        getContentPane().add(txtDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 150, 60));
        getContentPane().add(txtLimiteMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 280, 150, -1));
        getContentPane().add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 150, -1));

        btnLupa.setText("Actualizar Tabla");
        btnLupa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLupaActionPerformed(evt);
            }
        });
        getContentPane().add(btnLupa, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, -1, -1));

        jLabel7.setText("Nombre:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 62, 25));

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 390, -1, -1));

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 390, -1, -1));

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 390, -1, -1));

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Marca", "Descripcion", "LimiteMin", "LimiteMax"
            }
        ));
        jScrollPane1.setViewportView(Tabla);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, 640, 300));

        jButton1.setText("Registrair Inventario");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 350, 150, 50));
        getContentPane().add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 150, -1));

        jLabel8.setText("Codigo:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 62, 25));

        jLabel9.setText("Limite Maximo:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 90, 22));
        getContentPane().add(txtLimiteMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, 150, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
        Producto();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        int fila = Tabla.getSelectedRow();

        if (fila >= 0) {
            // Obtener datos de columnas específicas
            int codigo = Integer.parseInt(Tabla.getValueAt(fila, 0).toString());

            eliminarProductos(codigo);
        }else{
        JOptionPane.showMessageDialog(this, "Selecciones un reglon.");
        
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        // Obtener fila seleccionada
        int fila = Tabla.getSelectedRow();

        if (fila >= 0) {
            // Obtener datos de columnas específicas
            int codigo = Integer.parseInt(Tabla.getValueAt(fila, 0).toString());
            String nombre = (String) Tabla.getValueAt(fila, 1);
            String marca = (String) Tabla.getValueAt(fila, 2);
            String descripcion = (String) Tabla.getValueAt(fila, 3);
            int stockMinimo = Integer.parseInt(Tabla.getValueAt(fila, 4).toString());
            int stockMaximo = Integer.parseInt(Tabla.getValueAt(fila, 5).toString());

            modificarProductos(codigo, nombre, marca, descripcion, stockMinimo, stockMaximo);
        }else{
        JOptionPane.showMessageDialog(this, "Selecciones un reglon.");
        
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnLupaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLupaActionPerformed
        // TODO add your handling code here:
        actualizarTablaCombinada();
    }//GEN-LAST:event_btnLupaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        AgregarInventario agregarInventario = new AgregarInventario();
        agregarInventario.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtBuscadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscadorActionPerformed
        String texto = txtBuscador.getText().trim();

        if (!texto.isEmpty()) {
            buscarProducto(texto); // busca lo que se escribió
        } else {
            actualizarTablaCombinada(); // si está vacío, recarga todo
        }
    }//GEN-LAST:event_txtBuscadorActionPerformed

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
            java.util.logging.Logger.getLogger(RegistrarProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegistrarProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegistrarProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegistrarProducto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegistrarProducto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLupa;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtBuscador;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtLimiteMax;
    private javax.swing.JTextField txtLimiteMin;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
