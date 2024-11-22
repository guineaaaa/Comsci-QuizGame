package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import repository.QuizRepository;

public class DifficultyView extends JPanel implements ActionListener {
    private JPanel mainPanel;
    private String category;
    private JLabel difficultyLabel;
    private JButton easyButton, mediumButton, hardButton;

    public DifficultyView(JPanel mainPanel, String category) {
        this.mainPanel = mainPanel;
        this.category = category;
        setLayout(new BorderLayout());

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
            selectedDifficulty = "쉬움";
        } else if (e.getSource() == mediumButton) {
            selectedDifficulty = "보통";
        } else if (e.getSource() == hardButton) {
            selectedDifficulty = "어려움";
        }

        // 난이도 선택 후 퀴즈 문제를 DB에서 가져오고 게임 시작
        fetchQuizQuestions(selectedDifficulty);
    }

    // 선택된 난이도에 맞는 문제를 DB에서 가져옴
    private void fetchQuizQuestions(String difficulty) {
        // QuizRepository를 사용하여 DB에서 문제를 가져옵니다
        QuizRepository quizRepository = new QuizRepository();
        quizRepository.getQuestions(category, difficulty);

        // 이후 문제를 게임 화면에 보여주는 로직을 추가
    }
}
