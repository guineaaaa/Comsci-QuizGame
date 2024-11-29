package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Quiz;
import config.DatabaseConfig; // DB 연결 유틸리티 클래스

public class QuizRepository {

	public List<Quiz> getQuestions(String userId, String categoryName, String difficulty) {
	    List<Quiz> questions = new ArrayList<>();
	    
	    String query = """
	        SELECT q.quizId, q.title, q.option1, q.option2, q.option3, q.correct_option
	        FROM quiz q
	        
	        INNER JOIN category c ON q.categoryId = c.categoryId
	        LEFT JOIN user_quiz uq ON q.quizId = uq.quizId AND uq.id = ?
	        
	        WHERE c.name = ? AND q.difficulty = ? AND uq.quizId IS NULL
	        
	        ORDER BY RAND()
	        LIMIT 10
	    """;

	    try (Connection conn = DatabaseConfig.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(query)) {

	        // 쿼리 파라미터 설정
	        stmt.setString(1, userId);  // 사용자의 id
	        stmt.setString(2, categoryName);
	        stmt.setString(3, difficulty);

	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            Quiz question = new Quiz();
	            question.setQuizId(rs.getInt("quizId"));
	            question.setQuestionText(rs.getString("title"));
	            question.setOption1(rs.getString("option1"));
	            question.setOption2(rs.getString("option2"));
	            question.setOption3(rs.getString("option3"));
	            question.setCorrectOption(rs.getInt("correct_option"));
	            questions.add(question);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return questions;
	}

}
