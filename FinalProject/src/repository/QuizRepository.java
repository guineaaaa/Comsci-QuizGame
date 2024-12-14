package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConfig; // DB 연결 유틸리티 클래스
import model.Quiz;

public class QuizRepository {

    // 퀴즈 목록 가져오기 (이미 푼 문제는 제외)
    public List<Quiz> getQuestions(String userId, String categoryName) {
        List<Quiz> questions = new ArrayList<>();
        
        String query = """
                SELECT q.quizId, q.title, q.option1, q.option2, q.option3, q.correct_option, q.categoryId
                FROM quiz q
                LEFT JOIN user_quiz uq ON q.quizId = uq.quizId AND uq.userId = ?
                WHERE uq.quizId IS NULL AND q.categoryId = (
                    SELECT categoryId FROM category WHERE name = ?
                )
                ORDER BY RAND()
                LIMIT 10;
            """;
        System.out.println("카테고리: " + categoryName + "에 해당하는 퀴즈 문제를 가져오는 중");
        
        System.out.println("getQuestions 호출: userId=" + userId + ", categoryName=" + categoryName);
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // 쿼리 파라미터 설정
            stmt.setString(1, userId);  // 사용자의 id
            stmt.setString(2, categoryName);  // 카테고리 이름

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Quiz question = new Quiz();
                question.setQuizId(rs.getInt("quizId"));
                question.setQuestionText(rs.getString("title"));
                question.setOption1(rs.getString("option1"));
                question.setOption2(rs.getString("option2"));
                question.setOption3(rs.getString("option3"));
                question.setCorrectOption(rs.getInt("correct_option"));
                question.setCategoryId(rs.getInt("categoryId"));
                questions.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    // 퀴즈 완료 상태 저장
    public void markQuizAsCompleted(String userId, int quizId, int categoryId) {
        String query = """
            INSERT INTO user_quiz (userId, quizId, categoryId)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE categoryId = VALUES(categoryId);
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
            stmt.setInt(2, quizId);
            stmt.setInt(3, categoryId);

            int rowsAffected = stmt.executeUpdate();
            
            System.out.println("markQuizAsCompleted 실행");
            System.out.println("markQuizAsCompleted 호출: userId=" + userId + ", quizId=" + quizId + ", categoryId=" + categoryId);
            

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("이미 기록된 퀴즈: " + quizId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // user quiz 퀴즈 갯수 세기
    public int getCountUserQuiz(String userId) {
    	Connection conn=DatabaseConfig.getConnection();
    	String query="""
    			SELECT COUNT(*) AS total_count
    			FROM user_quiz
    			WHERE userId=?;
    			""";
    	try(PreparedStatement pstmt=conn.prepareStatement(query)){
    		pstmt.setString(1, userId);
    		ResultSet rs=pstmt.executeQuery();
    		if(rs.next()) {
    			return rs.getInt("total_count");
    		}
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	return 0; // 사용자가 푼 문제가 없는 경우
    }


}
