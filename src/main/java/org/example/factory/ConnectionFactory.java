package org.example.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static String USER;
    private static String SENHA;

    public static void setCredentials(String user, String senha) {
        USER = user; //system
        SENHA = senha; //admin
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, SENHA);
    }
}