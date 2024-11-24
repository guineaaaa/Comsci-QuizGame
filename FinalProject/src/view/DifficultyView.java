package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Quiz;
import model.User;
import repository.QuizRepository;

public class DifficultyView extends JPanel implements ActionListener {
    private JPanel mainPanel;
    private String category;
    private JLabel difficultyLabel;
    private JButton easyButton, mediumButton, hardButton;
    private User currentUser;
    
    public DifficultyView(JPanel mainPanel, String category, User currentUser) {
        this.mainPanel = mainPanel;
        this.category = category;
        this.currentUser=currentUser;
        setLayout(new BorderLayout());

        System.out.println("난도에서 유저 객체 전달 디버깅"+currentUser.getNickname());
		
        // 난이도 라벨 패널 설정
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        difficultyLabel = new JLabel(category + " 카테고리 - 난이도 선택");
        topPanel.add(difficultyLabel);

        // 중앙 패널 설정 (난이도 선택)
        JPanel centerPanel = new JPanel();
        easyButton = new JButton("쉬움");
        mediumButton = new JButton("보통");
        hardButton = new JButton("어려움");

        easyButton.addActionListener(this);
        mediumButton.addActionListener(this);
        hardButton.addActionListener(this);

        centerPanel.add(easyButton);
        centerPanel.add(mediumButton);
        centerPanel.add(hardButton);

        // BorderLayout에 각각 추가
        add(topPanel, BorderLayout.NORTH);  // 상단 패널
        add(centerPanel, BorderLayout.CENTER);  // 중앙 패널
    }

    // 난이도 버튼 클릭 이벤트 처리
    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedDifficulty = "";

        if (e.getSource() == easyButton) {
            selectedDifficulty = "easy";
        } else if (e.getSource() == mediumButton) {
            selectedDifficulty = "medium";
        } else if (e.getSource() == hardButton) {
            selectedDifficulty = "hard";
        }

        // 난이도 선택 후 퀴즈 문제를 DB에서 가져오고 게임 시작
        fetchQuizQuestions(selectedDifficulty,currentUser);
    }

    // 선택된 난이도에 맞는 문제를 DB에서 가져옴
    private void fetchQuizQuestions(String difficulty, User currentUser) {
        QuizRepository quizRepository = new QuizRepository();
        List<Quiz> questions = quizRepository.getQuestions(category, difficulty);

        if (questions.isEmpty()) {
            System.out.println("선택한 카테고리와 난이도에 맞는 문제가 없습니다.");
            return;
        }

        // 문제 데이터를 GameView에 전달하여 게임 시작
        mainPanel.removeAll();
        mainPanel.add(new GameView(mainPanel, questions,currentUser)); // GameView로 문제 데이터 전달
        mainPanel.revalidate();
        mainPanel.repaint();
    }

}
