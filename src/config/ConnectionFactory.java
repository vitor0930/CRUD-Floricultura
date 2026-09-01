package config;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
    private static String URL = "jdbc:mysql://localhost:3306/floricultura";
    private static String USER = "root";
    private static String PASSWORD = "";

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro na conexão");
        }
    }
}
