/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tocsys;
import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {
    public static void main(String[] args)  {
        /*
        String url = "jdbc:mysql://tangamandapio.software:3306/IngenieriaSoftware"; // Cambia "tu_base_de_datos" por el nombre real
        String usuario = "daniel";  // Cambia si usas otro usuario
        String contraseña = "daniel123"; // Ingresa tu contraseña de MySQL

        try {
            
            Connection conexion = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("✅ Conexión exitosa a MySQL.");
            conexion.close();
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
        }
        */
       
        String url = "jdbc:mysql://tangamandapio.software:3306/IngenieriaSoftware"; // Cambia "tu_base_de_datos" por el nombre real
        String usuario = "admin";  // Cambia si usas otro usuario
        String contraseña = "admin"; // Ingresa tu contraseña de MySQL
        
        try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña)) {
    
    // Procedimiento con parámetros de entrada
    CallableStatement stmt = (CallableStatement) conexion.prepareCall("{call sp_insertar_usuario(?, ?, ?)}");
        stmt.setString(1,"1");  // Primer parámetro (1 admin o 2 recepcionmista)
        stmt.setString(2,"Rosa");  // Primer parámetro ()
        stmt.setString(3,"12345678"); // Segundo parámetro (email)
       
        stmt.execute();
        System.out.println("Cliente registrado correctamente");
} catch (SQLException e) {
            System.out.println("error al llamar al procedimiento: " + e.getMessage());
            e.printStackTrace();
}
        
        
        
        
        
    }
}