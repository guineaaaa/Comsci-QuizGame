/**
 * repository 패키지
 * DB와 직접 통신하는 코드가 위치한다.
 */
package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.DatabaseConfig;
import model.User;

public class UserRepository {
	// 회원가입 로직
    public boolean saveUser(String username, String password, String nickname) {
        Connection conn = DatabaseConfig.getConnection();
        String query = "INSERT INTO user (id, password, nickname) VALUES (?, ?,?)";

        try {
            // 중복 체크
            if (isUserExists(username)) {
                return false;
            }

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3,  nickname);
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
    
    public String getUserNickname(String username) {
    	Connection conn = DatabaseConfig.getConnection();
        String query = "SELECT nickname FROM user WHERE id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nickname"); // 닉네임 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 닉네임이 없거나 에러가 발생한 경우
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
    
    // 현재 로그인된 사용자 
    public User getCurrentUser(String username) {
        Connection conn = DatabaseConfig.getConnection();
        String query = "SELECT id, password, nickname, point, character_image, time_boost_item, life_item FROM user WHERE id=?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);  // You are passing the username as the 'id' in your query.
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String id = rs.getString("id"); // id는 문자열로 가져옵니다.
                String password = rs.getString("password");
                String nickname = rs.getString("nickname");
                int points = rs.getInt("point");
                String characterImage = rs.getString("character_image");

                if (characterImage == null || characterImage.isEmpty()) {
                    characterImage = "/images/character.png";
                }

                // time_boost_item과 life_item을 추가로 처리합니다.
                int timeBoostItem = rs.getInt("time_boost_item");
                int lifeItem = rs.getInt("life_item");

                return new User(id, password, nickname, points, characterImage, timeBoostItem, lifeItem);  // User 객체에 추가 필드 전달
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }

    
    public void updateUserPoints(String username, int addedPoints) {
        String query = "UPDATE user SET point = point + ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, addedPoints); // 추가될 점수
            pstmt.setString(2, username); // 사용자 ID
            int rowsUpdated = pstmt.executeUpdate(); // 업데이트 성공 여부 확인
            
            if (rowsUpdated > 0) {
                System.out.println(username + "의 점수가 성공적으로 업데이트되었습니다.");
            } else {
                System.out.println("사용자를 찾을 수 없습니다: " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void updateItemCount(String userId, int livesItem, int timeBoostItem) {
        String query = "UPDATE user SET life_item = ?, time_boost_item = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, livesItem);  // 남은 목숨 아이템
            stmt.setInt(2, timeBoostItem);  // 남은 시간 아이템
            stmt.setString(3, userId);  // 사용자 ID

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getUserPoints(String username) {
        Connection conn = DatabaseConfig.getConnection();
        String query = "SELECT point FROM user WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("point"); // 포인트 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // 포인트가 없거나 에러가 발생한 경우
    }
   

}

