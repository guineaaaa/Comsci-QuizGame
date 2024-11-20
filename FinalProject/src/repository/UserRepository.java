/**
 * repository 패키지
 * DB와 직접 통신하는 코드가 위치한다.
 */
package repository;

import config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
	// 회원가입 로직
    public boolean saveUser(String username, String password) {
        Connection conn = DatabaseConfig.getConnection();
        String query = "INSERT INTO user (id, password) VALUES (?, ?)";

        try {
            // 중복 체크
            if (isUserExists(username)) {
                return false;
            }

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 회원가입 시 중복 회원가입 방지
    private boolean isUserExists(String username) {
        Connection conn = DatabaseConfig.getConnection();
        String query = "SELECT 1 FROM user WHERE id = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
		}catch(SQLException e) {
			e.printStackTrace();
			return true;
		}
	}
    
    // 로그인 로직
    public boolean loginUser(String username, String password) {
        Connection conn = DatabaseConfig.getConnection();
        String query="SELECT 1 FROM user WHERE id=? AND password=?";
        
        try {
        	PreparedStatement pstmt=conn.prepareStatement(query);
        	pstmt.setString(1, username); //아이디
        	pstmt.setString(2, password); //비밀번호
        	
        	ResultSet rs=pstmt.executeQuery();
        	return rs.next(); //일치하는 레코드 존재 시 로그인 성공
        }catch(SQLException e) {
        	e.printStackTrace();
        	return false; //
        }
        

        
    }

}
