/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tocsys.Interfaces;

import java.sql.Connection;
import tocsys.*;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.Statement;
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
        txtBuscador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscadorActionPerformed(evt);
            }
        });
        getContentPane().add(txtBuscador, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 240, -1));

        btnLupa.setText("Actualizar Tabla");
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
