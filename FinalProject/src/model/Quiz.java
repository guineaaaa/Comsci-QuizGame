package model;

public class Quiz {
    private int quizId;
    private String questionText;
    private String option1;
    private String option2;
    private String option3;
    private int correctOption;
    private String difficulty; // 추가된 난이도 필드

    // Getters and Setters
    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }

    // 난이도에 대한 getter, setter 추가
    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    // 정답을 반환하는 메서드 수정
    public int getAnswer() {
        return correctOption; // 정답은 'correctOption'과 동일
    }

    // 제목을 반환하는 메서드 수정 (quizId 대신 제목을 반환)
    public String getTitle() {
        return questionText; // 퀴즈의 질문 텍스트를 제목으로 사용
    }
}
