package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Db {
    private static final String URL  = "jdbc:postgresql://localhost:5432/mental_health";
    private static final String USER = "postgres";
    private static final String PASS = "jt209";

    private Db() {}

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
