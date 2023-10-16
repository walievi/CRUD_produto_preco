package com.walievi.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DB {
    private Connection conn;

    public DB() throws SQLException {
        try {
            Dotenv dotenv = Dotenv.load();
            String host = dotenv.get("DB_HOST");
            String port = dotenv.get("DB_PORT");
            String dbname = dotenv.get("DB_DBNAME");
            String timezone = dotenv.get("DB_TIMEZONE");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname + "?serverTimezone=" + timezone;
            String usuario = dotenv.get("DB_USER");
            String senha = dotenv.get("DB_PASSWORD");

            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(url, usuario, senha);
        } catch (Exception e) {
            throw new SQLException("Falha na conexão com o banco de dados", e);
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query);
        return stmt.executeQuery();
    }

    public Connection getConnection() {
        return conn;
    }
    
}
