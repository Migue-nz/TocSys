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

/**
 *
 * @author danie
 */
public class AgregarCitas extends javax.swing.JFrame {
  // private  List<String> idhor = new ArrayList<>();
    
    
    public AgregarCitas() {
        initComponents();
        cargarClientesEnTabla();
        rellenarcombobox();
    }
    
    
    public void cargarClientesEnTabla() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("IdCliente");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Telefono");
        modelo.addColumn("Correo");
        modelo.addColumn("Fecha");

        try (Connection conn = ConexionBD.obtenerConexion(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT idCliente, nombre, apellidos, telefono, correo FROM Cliente")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("idCliente"),
                    rs.getString("nombre"),
                    rs.getString("apellidos"),
                    rs.getString("telefono"),
                    rs.getString("correo")
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
    
    
    
    

/*
    public void RegistrarCitas(int idcliente,String fechaFormateada,int idhorario ) {
        
            
            String sql = "INSERT INTO Cita (idCita, idFecha,idHora, idCliente) VALUES("
                + "'" + idcliente + "', "
                + "'" + fechaFormateada + "', "
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
    */
    
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

        fecha = new com.toedter.calendar.JDateChooser();
        btnAgendarCita = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        cbxhorario = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(998, 426));

        btnAgendarCita.setText("Agendar CIta");
        btnAgendarCita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgendarCitaActionPerformed(evt);
            }
        });

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "IdCliente", "Nombre", "Apellidos", "telefono", "Correo", "Fecha"
            }
        ));
        jScrollPane1.setViewportView(tblClientes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxhorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(btnAgendarCita)))
                .addContainerGap(258, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(cbxhorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(118, 118, 118)
                .addComponent(btnAgendarCita)
                .addContainerGap(165, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgendarCitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgendarCitaActionPerformed

    obtenerFechaValidada();
        
      Date fechaCita = obtenerFechaValidada();
    
    if(fechaCita != null) {
        // Aquí tu lógica para agendar la cita
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String fechaFormateada = sdf.format(fechaCita);
        System.out.println("Fecha agendada: " + fechaFormateada);
        
         int fila = tblClientes.getSelectedRow();

        if (fila >= 0) {
        // Obtener datos de columnas específicas
        int codigo = Integer.parseInt(tblClientes.getValueAt(fila, 0).toString()) ;
        //int filahorario = cbxhorario.get ;
           // if(){
               // RegistrarCitas(codigo,fechaFormateada,idhorario); 
            //}else{
                
            //}
        
        
        
        }
    }
       

    }//GEN-LAST:event_btnAgendarCitaActionPerformed

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
    private javax.swing.JComboBox<String> cbxhorario;
    private com.toedter.calendar.JDateChooser fecha;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblClientes;
    // End of variables declaration//GEN-END:variables
}
