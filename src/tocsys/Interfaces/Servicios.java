/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tocsys.Interfaces;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import javax.swing.table.DefaultTableModel;
import tocsys.ConexionBD;


public class Servicios extends javax.swing.JFrame {
     DefaultTableModel modelo;
     

    public Servicios() {
        initComponents();
        modelo = (DefaultTableModel)tblServicios.getModel();
        cargarServiciosEnTabla();
    }

    public void cargarServiciosEnTabla() {
       
       

        try (
                Connection conn = ConexionBD.obtenerConexion(); 
                Statement stmt = conn.createStatement(); 
                ResultSet rs = 
                            stmt.executeQuery(
                                    "SELECT nombre, descripcion, duracion FROM servicios")) {

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getString("duracion").substring(0,5),
                });
            }

            tblServicios.setModel(modelo);

        } catch (Exception e) {
            System.out.println("Error al cargar servicios: " + e.getMessage());
        }
    }
    
    public void cargarServicios(String busqueda) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Duracion");
               String sql = "{call BuscarServicio(?)}";


        try (
                java.sql.Connection conn = ConexionBD.obtenerConexion();
                CallableStatement stmt = (CallableStatement) conn.prepareCall(sql)) 
        {
                stmt.setString(1, busqueda);        // Primer parámetro (String)
               

            ResultSet rs=stmt.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getString("duracion").substring(0,5)
                });
            }

            tblServicios.setModel(modelo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtBuscar = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        BTNBUSCAR = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblServicios = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        BTNACTUALIZAR = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel5.setText("SERVICIOS");

        BTNBUSCAR.setText("BUSCAR");
        BTNBUSCAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNBUSCARActionPerformed(evt);
            }
        });

        tblServicios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Descripcion", "Duracion (HH:MM)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblServicios);
        if (tblServicios.getColumnModel().getColumnCount() > 0) {
            tblServicios.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblServicios.getColumnModel().getColumn(1).setMinWidth(170);
            tblServicios.getColumnModel().getColumn(2).setPreferredWidth(10);
        }

        btnAgregar.setText("AGREGAR SERVICIO");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnModificar.setText("MODIFICAR SERVICIO");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setText("ELIMINAR SERVICIO");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        BTNACTUALIZAR.setText("ACTUALIZAR");
        BTNACTUALIZAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNACTUALIZARActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BTNACTUALIZAR, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BTNBUSCAR, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(436, 436, 436))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BTNBUSCAR, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addComponent(BTNACTUALIZAR)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(148, 148, 148))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(150, 150, 150))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
        RegistrarServicio rc = new RegistrarServicio();
        rc.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed

        int filaSeleccionada = tblServicios.getSelectedRow();

        if (filaSeleccionada >= 0) {
            // Obtenemos los datos de la fila seleccionada
            int idCliente = Integer.parseInt(tblServicios.getValueAt(filaSeleccionada, 0).toString());
            String nombre = tblServicios.getValueAt(filaSeleccionada, 1).toString();
            String apellidos = tblServicios.getValueAt(filaSeleccionada, 2).toString();
            String telefono = tblServicios.getValueAt(filaSeleccionada, 3).toString();
            String correo = tblServicios.getValueAt(filaSeleccionada, 4).toString();

            // Abrimos la ventana ModificarCliente con los datos
            ModificarCliente ventana = new ModificarCliente(idCliente, nombre, apellidos, telefono, correo);
            ventana.setVisible(true);
            this.dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un cliente primero.");
        }

    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int filaSeleccionada = tblServicios.getSelectedRow();

        if (filaSeleccionada >= 0) {
            int idCliente = Integer.parseInt(tblServicios.getValueAt(filaSeleccionada, 0).toString());

            int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de eliminar este cliente?",
                    "Confirmar eliminación",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
                try (Connection conn = ConexionBD.obtenerConexion(); Statement stmt = conn.createStatement()) {

                    String sql = "DELETE FROM Cliente WHERE idCliente = " + idCliente;
                    int filas = stmt.executeUpdate(sql);

                    if (filas > 0) {
                        javax.swing.JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
                        cargarClientesEnTabla();
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(this, "No se pudo eliminar el cliente.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
                }
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un cliente primero.");
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void BTNBUSCARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNBUSCARActionPerformed
        cargarServicios(txtBuscar.getText());
    }//GEN-LAST:event_BTNBUSCARActionPerformed

    private void BTNACTUALIZARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNACTUALIZARActionPerformed
        cargarClientesEnTabla();
        // TODO add your handling code here:
    }//GEN-LAST:event_BTNACTUALIZARActionPerformed

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
            java.util.logging.Logger.getLogger(Servicios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Servicios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Servicios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Servicios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Servicios().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTNACTUALIZAR;
    private javax.swing.JButton BTNBUSCAR;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblServicios;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
/*
public class RegistrarProducto extends javax.swing.JFrame {
private int codigoProducto;
   
    public RegistrarProducto() {
        initComponents();
        
        
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
 *//*
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
    

    
    //crear el metodo de registrar inventario con el parametro de codigo
    public void RegistroInventario(String nombreTabla) {
    try {
        int unidades = Integer.parseInt(txtUnidades.getText());
        int limite = Integer.parseInt(txtLimite.getText());
        

        String sql = "INSERT INTO " + nombreTabla + " (unidades, limite, Producto_idProducto) VALUES(?, ?, ?)";

        try (java.sql.Connection conn = ConexionBD.obtenerConexion(); 
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, unidades);
            pstmt.setInt(2, limite);
            pstmt.setInt(3, codigoProducto);

            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Inventario registrado exitosamente.");
                txtUnidades.setText("");
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMarca = new javax.swing.JTextField();
        txtBuscador = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        txtUnidades = new javax.swing.JTextField();
        txtLimite = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        btnLupa = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1004, 444));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("Productos");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 0, 170, 40));

        jLabel2.setText("Marca:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 62, 19));

        jLabel3.setText("Descripcion:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, -1, 19));

        jLabel5.setText("Unidades:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 62, -1));

        jLabel6.setText("Limite:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 350, 52, 22));
        getContentPane().add(txtMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 150, -1));

        txtBuscador.setText("Buscador");
        getContentPane().add(txtBuscador, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 240, -1));

        txtDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescripcionActionPerformed(evt);
            }
        });
        getContentPane().add(txtDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 150, 60));
        getContentPane().add(txtUnidades, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 290, 150, -1));
        getContentPane().add(txtLimite, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 350, 150, -1));
        getContentPane().add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 150, -1));

        btnLupa.setText("Lupa");
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

        pack();
    }// </editor-fold>                        

    private void txtDescripcionActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
    }                                              

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        RegistrarProducto("Producto");
    }                                          

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
        actualizarTablaCombinada();
    }                                           

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }*/
