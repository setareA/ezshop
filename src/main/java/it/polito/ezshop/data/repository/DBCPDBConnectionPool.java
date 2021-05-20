package it.polito.ezshop.data.repository;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;


/***
 * @see <a href="https://www.baeldung.com/java-connection-pooling">A Simple Guide to Connection Pooling in Java</a>
 ***/

public class DBCPDBConnectionPool {

    private static BasicDataSource ds = new BasicDataSource();
    private final static String dbURL = "jdbc:sqlite:ezshop.db";

    static {
        ds.setDriverClassName("org.sqlite.JDBC");
        ds.setUrl(dbURL);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void close() throws SQLException {
        ds.close();
    }

    private DBCPDBConnectionPool() {
    }

}