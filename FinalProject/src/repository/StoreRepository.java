package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConfig;
import model.Item;

public class StoreRepository {
	
	// 모든 아이템을 조회하는 메서드
	public List<Item> getItems(){
		List<Item> items=new ArrayList<>();
		String query="SELECT * FROM item";
		
		try(Connection conn=DatabaseConfig.getConnection(); // DB 연결 준지
			PreparedStatement pstmt=conn.prepareStatement(query); // SQL 쿼리 준비
			ResultSet rs=pstmt.executeQuery()){ 
			// ResultSet을 통해 쿼리를 실행하면 ResultSet 타입으로 반환을 해주어서 결과값을 저장할 수 있다.
			// 쿼리 실행 후의 결과 집합 Result Set
			
			while(rs.next()) { // Result set의 다음 행이 존재하는 동안 반복 한다.
				// next() 메소드를 통해서 선택되는 행을 바꿀 수 있다.
				int itemId=rs.getInt("itemId");
				String name=rs.getString("name");
				String type=rs.getString("type");
				int price=rs.getInt("price");
				items.add(new Item(itemId,name,type,price));
				// 리스트에 아이템 객체를 추가한다.
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return items; // 결과 리스트 반환
	}
	
	// 특정 유저가 아이템을 구매하는 메서드
    public boolean purchaseItem(String userId, int itemId) {
        String itemQuery = "SELECT * FROM item WHERE itemId = ?"; //구매하려는 아이템 정보를 조회하는 쿼리
        String userQuery = "SELECT point FROM user WHERE id = ?"; // 유저의 포인트를 조회하는 쿼리
        String updatePointQuery = "UPDATE user SET point = point - ? WHERE id = ?"; // 유저의 포인트를 차감하는 쿼리
        String updateTimeBoostQuery = "UPDATE user SET time_boost_item = time_boost_item + 1 WHERE id = ?"; // time_boost 아이템 수를 증가
        String updateLifeItemQuery = "UPDATE user SET life_item = life_item + 1 WHERE id = ?"; // life item 수를 증가하는 쿼리
        String insertUserItemQuery = "INSERT INTO user_item (userId, itemId) VALUES (?, ?)"; // 유저 아이템 테이블에 유저와 아이템 관계를 추가하는 쿼리

        try (Connection conn = DatabaseConfig.getConnection()) { // DB 연결 생성
            conn.setAutoCommit(false);
            
            // 아이템 정보를 조회
            PreparedStatement itemStmt = conn.prepareStatement(itemQuery);
            itemStmt.setInt(1, itemId);
            ResultSet itemRs = itemStmt.executeQuery();
            if (!itemRs.next()) return false; 

            int price = itemRs.getInt("price");
            String type = itemRs.getString("type");

            // 유저 포인트 조회
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setString(1, userId);
            ResultSet userRs = userStmt.executeQuery();
            if (!userRs.next()) return false;

            int userPoint = userRs.getInt("point");
            if (userPoint < price) return false;

            // 포인트를 차감한다.
            PreparedStatement updatePointStmt = conn.prepareStatement(updatePointQuery);
            updatePointStmt.setInt(1, price);
            updatePointStmt.setString(2, userId);
            updatePointStmt.executeUpdate();

            // 아이템 타입에 따른 업데이트를 수행한다.
            if ("time_boost".equals(type)) { //시간 증가 아이템 처리
                PreparedStatement timeBoostStmt = conn.prepareStatement(updateTimeBoostQuery);
                timeBoostStmt.setString(1, userId);
                timeBoostStmt.executeUpdate();
            } else if ("life".equals(type)) { // 생명 아이템 처리
                PreparedStatement lifeItemStmt = conn.prepareStatement(updateLifeItemQuery);
                lifeItemStmt.setString(1, userId);
                lifeItemStmt.executeUpdate();
            } else { //일반 아이템 처리
                PreparedStatement insertUserItemStmt = conn.prepareStatement(insertUserItemQuery);
                insertUserItemStmt.setString(1, userId);
                insertUserItemStmt.setInt(2, itemId);
                insertUserItemStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 유저가 특정 아이템을 소유하고 있는지 확인하는 코드
    public boolean isItemOwned(String userId, int itemId) {
        String query = "SELECT 1 FROM user_item WHERE userId = ? AND itemId = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            pstmt.setInt(2, itemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 캐릭터 이미지를 변경하는 메서드
    public boolean updateCharacterImage(String userId, int itemId) {
        String imagePath;
        switch (itemId) { // itemId에 따른 이미지 경로 설정
            case 3:
                imagePath = "/images/character_with_flower.png";
                break;
            case 4:
                imagePath = "/images/character_with_hat.png";
                break;
            case 5:
                imagePath = "/images/character.png"; // 기본 캐릭터
                break;
            default:
                return false; // 유효하지 않은 itemId
        }

        // 캐릭터 이미지 업데이트 쿼리
        String updateQuery = "UPDATE user SET character_image = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setString(1, imagePath); //이미지 경로 설정
            updateStmt.setString(2, userId); // 유저 ID 설정
            updateStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 현재 캐릭터 이미지 경로를 가져오는 메서드
    public String getCharacterImagePath(String userId) {
        String query = "SELECT character_image FROM user WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { //결과가 존재하면
                    return rs.getString("character_image");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "/images/character.png"; 
    }


}
