package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Quiz;
import config.DatabaseConfig; // DB 연결 유틸리티 클래스

public class QuizRepository {

    public List<Quiz> getQuestions(String categoryName, String difficulty) {
        List<Quiz> questions = new ArrayList<>();

        // 랜덤으로 문제를 가져오기 위해 RAND() 함수 사용
        String query = """
            SELECT q.quizId, q.title, q.option1, q.option2, q.option3, q.correct_option,  q.difficulty
            FROM quiz q
            INNER JOIN category c ON q.categoryId = c.categoryId
            WHERE c.name = ? AND q.difficulty = ?
            ORDER BY RAND()
            LIMIT 10
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // 쿼리 파라미터 설정
            stmt.setString(1, categoryName);
            stmt.setString(2, difficulty);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Quiz question = new Quiz();
                question.setQuizId(rs.getInt("quizId"));
                question.setQuestionText(rs.getString("title"));
                question.setOption1(rs.getString("option1"));
                question.setOption2(rs.getString("option2"));
                question.setOption3(rs.getString("option3"));
                question.setCorrectOption(rs.getInt("correct_option")); 
                question.setDifficulty(rs.getString("difficulty"));
                questions.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }
}
