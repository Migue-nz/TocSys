/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tocsys.Interfaces;

import java.awt.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import tocsys.ConexionBD;


//actualizar podría hacer una transacción entre eliminar (actualiza horas disponibles) y agregar
/**
 *
 * @author danie
 */
public class AgregarCitas extends javax.swing.JFrame {
  // private  List<String> idhor = new ArrayList<>();
    String JSON = "";
    DefaultTableModel modelo = new DefaultTableModel();

    
    //Para eliminar cita, capturo sus productos en un archivo JSON y por cada uno, uso el metodo de gestionarInventario
    
    
    public AgregarCitas() {
        initComponents();
        setSize(800, 440);
        Cliente c = new Cliente();
        modelo = (DefaultTableModel)tblClientes.getModel();
        cargarClientes();
        
    }
    
    public void cargarClientes(){
        modelo.setRowCount(0);
        try (Connection conn = ConexionBD.obtenerConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT idCliente, nombre, apellidos, telefono, correo FROM clientes")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    (rs.getString("nombre")+rs.getString("apellidos")),
                    rs.getString("telefono"),
                });
            }

            tblClientes.setModel(modelo);

        } catch (Exception e) {
            System.out.println("Error al cargar clientes: " + e.getMessage());
        }
    }
    private Date obtenerFechaValidada() {
    Date fechaSeleccionada = fecha.getDate();
    
    if(fechaSeleccionada == null) {
        JOptionPane.showMessageDialog(this, 
            "Por favor seleccione una fecha", 
            "Fecha requerida", 
            JOptionPane.WARNING_MESSAGE);
        fecha.requestFocusInWindow();
    }
    return fechaSeleccionada;
}
    
    
    
    public void rellenarcombobox(){
        cbxhorario.removeAllItems();
    
    try (Connection conn = ConexionBD.obtenerConexion();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT idHora, hora FROM horario ORDER BY hora")) {

        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        
        while (rs.next()) {
            // Formato: "Hora: HH:mm (ID: XX)"
            String item = "Hora: " + rs.getString("hora") + " (ID: " + rs.getInt("idHora") + ")";
            
            modelo.addElement(item);
        }
        
        cbxhorario.setModel(modelo);
        
    } catch (Exception e) {
        System.err.println("Error al cargar horarios: " + e.getMessage());
        JOptionPane.showMessageDialog(null, 
            "Error al cargar los horarios disponibles", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    
    
    
    }
    
    
    
    
    
    
    
    
    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAgendarCita = new javax.swing.JButton();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(998, 426));
        getContentPane().setLayout(null);

        btnAgendarCita.setText("Agendar CIta");
        btnAgendarCita.setEnabled(false);
        btnAgendarCita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgendarCitaActionPerformed(evt);
            }
        });
        getContentPane().add(btnAgendarCita);
        btnAgendarCita.setBounds(430, 360, 99, 23);
        getContentPane().add(jCalendar1);
        jCalendar1.setBounds(430, 50, 333, 230);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(jComboBox1);
        jComboBox1.setBounds(100, 350, 72, 22);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(jComboBox2);
        jComboBox2.setBounds(570, 320, 72, 22);

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Telefono"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblClientes);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(20, 70, 390, 210);

        jTextField1.setText("Seleccionar cliente");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField1);
        jTextField1.setBounds(20, 40, 230, 22);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel5.setText("AGENDAR CITAS");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(300, 0, 269, 32);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar servicio" }));
        getContentPane().add(jComboBox3);
        jComboBox3.setBounds(20, 290, 250, 22);

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(jComboBox4);
        jComboBox4.setBounds(430, 320, 72, 22);

        jLabel1.setText("Minutos");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(100, 380, 60, 16);

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(jComboBox5);
        jComboBox5.setBounds(20, 350, 72, 22);

        jLabel2.setText("Seleccionar hora");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(430, 290, 110, 16);

        jButton1.setText("Seleccionar productos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(190, 350, 150, 23);

        jLabel3.setText("Tiempo adicional");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 320, 200, 16);

        jLabel4.setText("Horas");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(20, 380, 60, 16);

        jLabel6.setText("Minutos");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(650, 320, 60, 20);

        jLabel7.setText("Horas");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(510, 320, 60, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgendarCitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgendarCitaActionPerformed

    }//GEN-LAST:event_btnAgendarCitaActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        SeleccionarProductos c = new SeleccionarProductos();
        c.setVisible(true);
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
            java.util.logging.Logger.getLogger(AgregarCitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AgregarCitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AgregarCitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AgregarCitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AgregarCitas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgendarCita;
    private javax.swing.JButton jButton1;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tblClientes;
    // End of variables declaration//GEN-END:variables
}
