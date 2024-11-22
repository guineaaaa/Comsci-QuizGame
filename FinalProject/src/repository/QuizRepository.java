package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Quiz;
import config.DatabaseConfig; // DB 연결 유틸리티 클래스

public class QuizRepository {

    public List<Quiz> getQuestions(String category, String difficulty) {
        List<Quiz> questions = new ArrayList<>();

        String query = "SELECT * FROM quiz WHERE category = ? AND difficulty = ? LIMIT 10";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, category);
            stmt.setString(2, difficulty);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Quiz question = new Quiz();
                question.setQuestionText(rs.getString("question_text"));
                question.setAnswer(rs.getString("answer"));
                question.setOptions(rs.getString("options"));
                questions.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }
}
