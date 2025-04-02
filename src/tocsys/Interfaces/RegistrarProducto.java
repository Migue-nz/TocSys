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

    public void RegistrarProducto(String nombreTabla) {
        if ("".equals(txtNombre.getText()) || "".equals(txtMarca.getText()) || "".equals(txtDescripcion.getText()) ) {
             JOptionPane.showMessageDialog(this, "No dejes los campos vacios.");
        }else{
        //verificar que el nombre no exista antes del registro
        boolean existe = nombreExiste("Producto",txtNombre.getText()); 
        
        if (existe == true) {
            JOptionPane.showMessageDialog(this, "Producto duplicado.");
        
        }else{
            
            
            String sql = "INSERT INTO " + nombreTabla + "(nombre, marca, descripcion) VALUES("
                + "'" + txtNombre.getText() + "', "
                + "'" + txtMarca.getText() + "', "
                + "'" + txtDescripcion.getText() + "')";

            try (java.sql.Connection conn = ConexionBD.obtenerConexion(); java.sql.Statement stmt = conn.createStatement()) {
            
            
            
                int filas = stmt.executeUpdate(sql);

                if (filas > 0) {
                    JOptionPane.showMessageDialog(this, "Producto registrado exitosamente.");   
                    //consultas otra ves pero ahora solo trae la id y lo guardas en el codigo
                    codigoProducto = obtenerValor("Producto",txtNombre.getText(),txtMarca.getText(),txtDescripcion.getText());
                    //llamas al metodo registrar inventario
                    RegistroInventario("Inventario");
                    //consulta todos los producto, inventarios y lo guardas en la tabla.
                    actualizarTablaCombinada();
                    
                    
                    txtNombre.setText("");
                    txtMarca.setText("");
                    txtDescripcion.setText("");
                    
                } else {
                    JOptionPane.showMessageDialog(this, "No se insertó ningún producto.");                
                }

            } catch (SQLException e) {
                System.out.println("Error al insertar en la tabla: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
        
       }  
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
               + "FROM producto p "
               + "INNER JOIN inventario i ON p.idProducto = i.Producto_idProducto";
    
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
    

    
    //crear el metodo de registrar inventario con el parametro de codigo
    public void RegistroInventario(String nombreTabla) {
    try {        
        int limite = Integer.parseInt(txtLimite.getText());
        

        String sql = "INSERT INTO " + nombreTabla + " (unidades, limite, Producto_idProducto) VALUES(?, ?, ?)";

        try (java.sql.Connection conn = ConexionBD.obtenerConexion(); 
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            
            pstmt.setInt(2, limite);
            pstmt.setInt(3, codigoProducto);

            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Inventario registrado exitosamente.");
                
                txtLimite.setText("");
                
            } else {
                JOptionPane.showMessageDialog(this, "No se insertó ningún registro.");
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar en la tabla: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error al registrar inventario: " + e.getMessage());
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Por favor ingrese valores numéricos válidos en unidades, límite.");
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
      String sqlProducto = "UPDATE producto SET nombre = ?, marca = ?, descripcion = ? WHERE idProducto = ?";
      String sqlInventario = "UPDATE inventario SET unidades = ?, limite = ? WHERE Producto_idProducto = ?";
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
      String sqlInventario = "DELETE FROM inventario WHERE Producto_idProducto = ?";
        String sqlProducto = "DELETE FROM producto WHERE idProducto = ?";
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMarca = new javax.swing.JTextField();
        txtBuscador = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        txtLimite = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        btnLupa = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(951, 429));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("Registro productos");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 0, 320, 40));

        jLabel2.setText("Marca:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 62, 19));

        jLabel3.setText("Descripcion:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, -1, 19));

        jLabel6.setText("Limite:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 300, 52, 22));
        getContentPane().add(txtMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 150, -1));

        txtBuscador.setText("Buscador");
        getContentPane().add(txtBuscador, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 240, -1));

        txtDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescripcionActionPerformed(evt);
            }
        });
        getContentPane().add(txtDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 150, 60));
        getContentPane().add(txtLimite, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, 150, -1));
        getContentPane().add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 150, -1));

        btnLupa.setText("Lupa");
        btnLupa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLupaActionPerformed(evt);
            }
        });
        getContentPane().add(btnLupa, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, -1, -1));

        jLabel7.setText("Nombre:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 62, 25));

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 370, -1, -1));

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 370, -1, -1));

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 370, -1, -1));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, 510, 300));

        jButton1.setText("Registrair Inventario");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 350, 150, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescripcionActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
        RegistrarProducto("Producto");
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        int fila = Tabla.getSelectedRow();

if (fila >= 0) {
    // Obtener datos de columnas específicas
    int codigo = Integer.parseInt(Tabla.getValueAt(fila, 0).toString()) ;
   
    
        eliminarProductos(codigo);
}
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        // Obtener fila seleccionada
int fila = Tabla.getSelectedRow();

if (fila >= 0) {
    // Obtener datos de columnas específicas
    int codigo = Integer.parseInt(Tabla.getValueAt(fila, 0).toString()) ;
    String nombre = (String) Tabla.getValueAt(fila, 1);
    String marca = (String) Tabla.getValueAt(fila, 2);
    String descripcion = (String) Tabla.getValueAt(fila, 3);
    int unidad = Integer.parseInt(Tabla.getValueAt(fila, 4).toString()) ;
    int limite = Integer.parseInt(Tabla.getValueAt(fila, 5).toString()) ;
    
    modificarProductos(codigo,nombre,marca,descripcion,unidad,limite);
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtBuscador;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtLimite;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
