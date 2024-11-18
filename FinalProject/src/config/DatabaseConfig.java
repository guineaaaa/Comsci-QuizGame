/**
 * config 패키지
 * - DB 연결 코드를 관리하는 곳
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
	private static Connection conn=null; //DB 커넥션 연결 객체
    private static final String DB_URL = "jdbc:mysql://localhost:3306/comsci_quiz"; //DBMS 접속할 db 명
    private static final String DB_USER = "root"; //DBMS 접속 시 아이디
    private static final String DB_PASSWORD = "newpw"; // DBMS 접속 시 비밀번호

    
    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("DB에 연결되었습니다: 연결됨");
            } catch (SQLException e) {
                System.err.println("DB 연결에 실패했습니다: " + e.getMessage());
            }
        }
        return conn;
    }
    
    public static void main(String[] args) {
        // 테스트 용도로 DB 연결 시도를 실행
        getConnection();
    }
 

}
