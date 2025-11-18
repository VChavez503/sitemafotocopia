package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    
    private static final String URL  = "jdbc:mysql://localhost:3306/fotocopia_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = ""; // tu contraseña
    
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Conexión exitosa a MySQL");
        } catch (Exception e) {
            System.out.println("❌ Error conectando a MySQL: " + e.getMessage());
        }
        return con;
    }
}
