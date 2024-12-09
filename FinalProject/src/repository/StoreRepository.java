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
	
	public List<Item> getItems(){
		List<Item> items=new ArrayList<>();
		String query="SELECT * FROM item";
		
		try(Connection conn=DatabaseConfig.getConnection();
			PreparedStatement pstmt=conn.prepareStatement(query);
			ResultSet rs=pstmt.executeQuery()){
			
			while(rs.next()) {
				int itemId=rs.getInt("itemId");
				String name=rs.getString("name");
				String type=rs.getString("type");
				int price=rs.getInt("price");
				items.add(new Item(itemId,name,type,price));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	
    public boolean purchaseItem(String userId, int itemId) {
        String itemQuery = "SELECT * FROM item WHERE itemId = ?";
        String userQuery = "SELECT point FROM user WHERE id = ?";
        String updatePointQuery = "UPDATE user SET point = point - ? WHERE id = ?";
        String updateTimeBoostQuery = "UPDATE user SET time_boost_item = time_boost_item + 1 WHERE id = ?";
        String updateLifeItemQuery = "UPDATE user SET life_item = life_item + 1 WHERE id = ?";
        String insertUserItemQuery = "INSERT INTO user_item (userId, itemId) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            
            PreparedStatement itemStmt = conn.prepareStatement(itemQuery);
            itemStmt.setInt(1, itemId);
            ResultSet itemRs = itemStmt.executeQuery();
            if (!itemRs.next()) return false;

            int price = itemRs.getInt("price");
            String type = itemRs.getString("type");

           
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setString(1, userId);
            ResultSet userRs = userStmt.executeQuery();
            if (!userRs.next()) return false;

            int userPoint = userRs.getInt("point");
            if (userPoint < price) return false;

            
            PreparedStatement updatePointStmt = conn.prepareStatement(updatePointQuery);
            updatePointStmt.setInt(1, price);
            updatePointStmt.setString(2, userId);
            updatePointStmt.executeUpdate();

            
            if ("time_boost".equals(type)) {
                PreparedStatement timeBoostStmt = conn.prepareStatement(updateTimeBoostQuery);
                timeBoostStmt.setString(1, userId);
                timeBoostStmt.executeUpdate();
            } else if ("life".equals(type)) {
                PreparedStatement lifeItemStmt = conn.prepareStatement(updateLifeItemQuery);
                lifeItemStmt.setString(1, userId);
                lifeItemStmt.executeUpdate();
            } else {
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

    public boolean updateCharacterImage(String userId, int itemId) {
        String imagePath;
        switch (itemId) {
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

        String updateQuery = "UPDATE user SET character_image = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setString(1, imagePath);
            updateStmt.setString(2, userId);
            updateStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public String getCharacterImagePath(String userId) {
        String query = "SELECT character_image FROM user WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("character_image");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "/images/character.png"; 
    }


}
