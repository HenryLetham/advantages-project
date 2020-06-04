package sample.models;


import java.sql.*;

public class DB {
    public static DB instance;
    private Connection conn;

    public DB() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/target?serverTimezone=Europe/Moscow&useSSL=false", "root", "root");
    }

    public static DB getInstance() throws SQLException {
        if (instance == null) {
            instance = new DB();
        }
        return instance;
    }

    public Connection getConn() {
        return conn;
    }
}
