/**
 * config 패키지
 * - DB 연결 코드를 관리하는 곳
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/comsci_quiz";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "newpw";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

}
