package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static Connection conn = null;  // DB 커넥션 연결 객체
    private static final String DB_URL = "jdbc:mysql://localhost:3306/comsci_quiz";  // DBMS 접속할 db 명
    private static final String DB_USER = "root";  // DBMS 접속 시 아이디
    private static final String DB_PASSWORD = "newpw";  // DBMS 접속 시 비밀번호

    // 커넥션을 얻는 메소드
    public static Connection getConnection() {
        if (conn == null || isConnectionClosed()) {
            try {
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("DB에 연결되었습니다: 연결됨");
            } catch (SQLException e) {
                System.err.println("DB 연결에 실패했습니다: " + e.getMessage());
            }
        }
        return conn;
    }

    // 연결이 닫혔는지 확인하는 메소드
    private static boolean isConnectionClosed() {
        try {
            return conn == null || conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    // 테스트용 메인 메소드
    public static void main(String[] args) {
        // DB 연결 시도
        getConnection();
    }
}
