package com.playernguyen.authes.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLEstablishment implements SQLEstablishment {

    private final String host;
    private final String username;
    private final String password;
    private final String database;
    private final String port;

    public MySQLEstablishment(String host, String username, String password, String database, String port) throws ClassNotFoundException {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.port = port;

        // Validation of class
        Class.forName("com.mysql.jdbc.Driver");
    }


    public Connection openConnection() throws SQLException {
        // Open new connection
        return DriverManager.getConnection(String.format(
                "jdbc:mysql://%s:%s/%s",
                this.host,
                this.port,
                this.database
        ), username, password);
    }
}
