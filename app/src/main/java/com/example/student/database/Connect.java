package com.example.student.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    private static Connection conn;

    public static Connection getConn() throws Exception {

        String REMOTE_IP = "192.168.0.5";
        String URL = "jdbc:mysql://" + REMOTE_IP + "/mydb";
        String USER = "root";
        String PASSWORD = "123456";
        //DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        return conn;

    }

    public void DBclose() throws SQLException {
        conn.close();
    }
}
