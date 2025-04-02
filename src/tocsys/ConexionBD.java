/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tocsys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author danie
 */
public class ConexionBD {
     private static final String URL = "jdbc:mysql://tangamandapio.software:3306/IngenieriaSoftware";
    private static final String USUARIO = "admin";
    private static final String CONTRASENA = "admin";

    // Método reutilizable para obtener la conexión
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }

    
    // Método main solo para probar la conexión
    public static void main(String[] args) {
        try (Connection conexion = obtenerConexion()) {
            System.out.println("Conectado");
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }
}
