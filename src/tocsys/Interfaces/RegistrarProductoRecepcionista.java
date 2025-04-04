/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tocsys.Interfaces;

import tocsys.*;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author danie
 */
public class RegistrarProductoRecepcionista extends javax.swing.JFrame {
private int codigoProducto;
   
    public RegistrarProductoRecepcionista() {
        initComponents();
        actualizarTablaCombinada();
        
    }

  
    
    //crear 2 metodo de consulta producto uno total y otro que traiga el id del producto recien agregado
    public int obtenerValor(String nombreTabla, String nombre, String marca, String descripcion) {
        String sql = "SELECT idproducto FROM " + nombreTabla + " WHERE nombre = ? AND marca = ? AND descripcion = ? LIMIT 1";
        int resultado = -1; // Valor por defecto si no encuentra nada

        try (java.sql.Connection conn = ConexionBD.obtenerConexion();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {  

            stmt.setString(1, nombre);
            stmt.setString(2, marca);
            stmt.setString(3, descripcion);

            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                resultado = rs.getInt("idproducto"); // Devuelve el ID o valor entero encontrado
            }
        } catch (SQLException e) {
            System.out.println("Error al encontrar el id: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        return resultado; // Retorna -1 si no encuentra ningún resultado
    }
    
    /**
 * Actualiza la tabla con datos combinados de Producto e Inventario
 */
public void actualizarTablaCombinada() {
    // Crear modelo de tabla con las columnas que necesitas
    DefaultTableModel modeloTabla = new DefaultTableModel();
    
    // Configurar columnas (según tu diseño)
    modeloTabla.addColumn("Codigo");
    modeloTabla.addColumn("Nombre");
    modeloTabla.addColumn("Marca");
    modeloTabla.addColumn("Descripcion");
    modeloTabla.addColumn("Unidad");
    modeloTabla.addColumn("Limite");
    
    // Consulta SQL para unir las tablas
    String sql = "SELECT p.idProducto, p.nombre, p.marca, p.descripcion, "
               + "i.unidades, i.limite "
               + "FROM Producto p "
               + "INNER JOIN Inventario i ON p.idProducto = i.Producto_idProducto";
    
    try (java.sql.Connection conn = ConexionBD.obtenerConexion();
         java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        // Llenar la tabla con los resultados
        while (rs.next()) {
            Object[] fila = {
                rs.getInt("idProducto"),   // Código
                rs.getString("nombre"),    // Nombre
                rs.getString("marca"),     // Marca
                rs.getString("descripcion"), // Descripción
                rs.getInt("unidades"),    // Unidad
                rs.getInt("limite")       // Límite
            };
            modeloTabla.addRow(fila);
        }
        
        // Asignar el modelo a la tabla existente
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
    public boolean nombreExiste(String nombreTabla, String nombre) {
        String sql = "SELECT COUNT(*) FROM " + nombreTabla + " WHERE nombre = ?";
        
        try (java.sql.Connection conn = ConexionBD.obtenerConexion(); 
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {  // Usar PreparedStatement en lugar de Statement

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
    
    
 public void modificarProductos(int idProducto, String nombre, String marca, String descripcion, 
                                int unidades, int limite) {
      String sqlProducto = "UPDATE Producto SET nombre = ?, marca = ?, descripcion = ? WHERE idProducto = ?";
      String sqlInventario = "UPDATE Inventario SET unidades = ?, limite = ? WHERE Producto_idProducto = ?";
    try (java.sql.Connection conn = ConexionBD.obtenerConexion();
            java.sql.PreparedStatement stmtProducto = conn.prepareStatement(sqlProducto);
            java.sql.PreparedStatement stmtInventario = conn.prepareStatement(sqlInventario)) {
        // 1. Actualizar Producto
        
       
       
            stmtProducto.setString(1, nombre);
            stmtProducto.setString(2, marca);
            stmtProducto.setString(3, descripcion);
            stmtProducto.setInt(4, idProducto);
            stmtProducto.executeUpdate();
        
        
        // 2. Actualizar Inventario
        
        
            stmtInventario.setInt(1, unidades);
            stmtInventario.setInt(2, limite);
            stmtInventario.setInt(3, idProducto);
            stmtInventario.executeUpdate();
        

        JOptionPane.showMessageDialog(this, "Actualización exitosa");
        

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        
    }
}
    
 
 
 public void eliminarProductos(int idProducto) {
      String sqlInventario = "DELETE FROM Inventario WHERE Producto_idProducto = ?";
        String sqlProducto = "DELETE FROM Producto WHERE idProducto = ?";
    try (java.sql.Connection conn = ConexionBD.obtenerConexion();
            java.sql.PreparedStatement stmtProducto = conn.prepareStatement(sqlProducto);
            java.sql.PreparedStatement stmtInventario = conn.prepareStatement(sqlInventario)) {
        
        // 1. Primero eliminar el inventario (por la restricción de clave foránea)
            stmtInventario.setInt(1, idProducto);
            stmtInventario.executeUpdate();
            
            // 2. Luego eliminar el producto
            stmtProducto.setInt(1, idProducto);
            stmtProducto.executeUpdate();
        

        JOptionPane.showMessageDialog(this, "Eliminacion exitosa");
        

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al Eliminacion: " + e.getMessage());
        
    }
}
 
 
 
 
 
 
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtBuscador = new javax.swing.JTextField();
        btnLupa = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(951, 420));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("Productos");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 0, 170, 40));

        txtBuscador.setText("Buscador");
        getContentPane().add(txtBuscador, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 240, -1));

        btnLupa.setText("Lupa");
        btnLupa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLupaActionPerformed(evt);
            }
        });
        getContentPane().add(btnLupa, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, -1, -1));

        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Marca", "Descripcion", "Unidad", "Limite"
            }
        ));
        jScrollPane1.setViewportView(Tabla);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 600, 260));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLupaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLupaActionPerformed
        // TODO add your handling code here:
        actualizarTablaCombinada();
    }//GEN-LAST:event_btnLupaActionPerformed

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
            java.util.logging.Logger.getLogger(RegistrarProductoRecepcionista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegistrarProductoRecepcionista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegistrarProductoRecepcionista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegistrarProductoRecepcionista.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegistrarProductoRecepcionista().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla;
    private javax.swing.JButton btnLupa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtBuscador;
    // End of variables declaration//GEN-END:variables
}
