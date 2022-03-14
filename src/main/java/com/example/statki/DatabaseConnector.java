package com.example.statki;

import java.sql.*;
import java.sql.DriverManager;

public class DatabaseConnector {

    public Connection conn;

    public Connection getConnection () {

        String driver =  "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/ships-game";
        String username = "root";
        String password = "";

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);
            System.out.println("Success connection to database " + url );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

}
