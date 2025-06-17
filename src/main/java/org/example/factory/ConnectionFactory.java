package org.example.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL ="jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER ="system";
    private static final String SENHA ="admin";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, SENHA);
    }
}
