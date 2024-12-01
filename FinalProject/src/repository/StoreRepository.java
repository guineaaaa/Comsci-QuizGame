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

    // 아이템 목록 가져오기
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        String query = "SELECT * FROM item";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int itemId = rs.getInt("itemId");
                String name = rs.getString("name");
                String type = rs.getString("type");
                int price = rs.getInt("price");
                items.add(new Item(itemId, name, type, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // 구매 처리
    public boolean purchaseItem(String userId, int itemId) {
        Connection conn = DatabaseConfig.getConnection();

        try {
            // 1. 구매할 아이템 정보 가져오기
            String queryItem = "SELECT * FROM item WHERE itemId = ?";
            PreparedStatement pstmtItem = conn.prepareStatement(queryItem);
            pstmtItem.setInt(1, itemId);
            ResultSet rsItem = pstmtItem.executeQuery();

            if (!rsItem.next()) return false; // 아이템이 없으면 종료

            String type = rsItem.getString("type");
            String name = rsItem.getString("name");
            int price = rsItem.getInt("price");

            // 2. 사용자 포인트 확인
            String queryUserPoint = "SELECT point FROM user WHERE id = ?";
            PreparedStatement pstmtUserPoint = conn.prepareStatement(queryUserPoint);
            pstmtUserPoint.setString(1, userId);
            ResultSet rsUserPoint = pstmtUserPoint.executeQuery();

            if (!rsUserPoint.next()) return false; // 유저가 없으면 종료

            int userPoint = rsUserPoint.getInt("point");
            if (userPoint < price) return false; // 포인트 부족 시 종료

            // 3. 액세서리 중복 구매 체크
            if ("accessory".equals(type)) {
                String checkQuery = "SELECT 1 FROM user_item WHERE userId = ? AND itemId = ?";
                PreparedStatement pstmtCheck = conn.prepareStatement(checkQuery);
                pstmtCheck.setString(1, userId);
                pstmtCheck.setInt(2, itemId);
                ResultSet rsCheck = pstmtCheck.executeQuery();

                if (rsCheck.next()) return false; // 이미 구매한 경우
            }

            // 4. 구매 처리
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 포인트 차감
            String deductPoints = "UPDATE user SET point = point - ? WHERE id = ?";
            PreparedStatement pstmtDeduct = conn.prepareStatement(deductPoints);
            pstmtDeduct.setInt(1, price);
            pstmtDeduct.setString(2, userId);
            pstmtDeduct.executeUpdate();

            // 아이템에 따른 처리
            if ("life".equals(type)) {
                String updateUser = "UPDATE user SET life_item = life_item + 1 WHERE id = ?";
                PreparedStatement pstmtUpdate = conn.prepareStatement(updateUser);
                pstmtUpdate.setString(1, userId);
                pstmtUpdate.executeUpdate();
            } else if ("time_boost".equals(type)) {
                String updateUser = "UPDATE user SET time_boost_item = time_boost_item + 1 WHERE id = ?";
                PreparedStatement pstmtUpdate = conn.prepareStatement(updateUser);
                pstmtUpdate.setString(1, userId);
                pstmtUpdate.executeUpdate();
            } else if ("accessory".equals(type)) {
                // 캐릭터 이미지 설정
                String characterImage = "";
                if ("꽃 모자".equals(name)) {
                    characterImage = "/images/character_with_flower.png";
                } else if ("요리사 모자".equals(name)) {
                    characterImage = "/images/character_with_hat.png";
                }

                String updateUser = "UPDATE user SET character_image = ? WHERE id = ?";
                PreparedStatement pstmtUpdate = conn.prepareStatement(updateUser);
                pstmtUpdate.setString(1, characterImage);
                pstmtUpdate.setString(2, userId);
                pstmtUpdate.executeUpdate();

                // 구매 내역 추가
                String insertPurchased = "INSERT INTO user_item (userId, itemId) VALUES (?, ?)";
                PreparedStatement pstmtPurchased = conn.prepareStatement(insertPurchased);
                pstmtPurchased.setString(1, userId);
                pstmtPurchased.setInt(2, itemId);
                pstmtPurchased.executeUpdate();
            }

            conn.commit(); // 트랜잭션 커밋
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback(); // 롤백 처리
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true); // 자동 커밋 복원
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
 
    public String getUpdatedCharacterImage(String userId) {
        String query = "SELECT character_image FROM user WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("character_image");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ""; 
    }

}
