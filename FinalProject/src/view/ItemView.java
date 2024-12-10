package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Quiz;
import model.User;
import repository.QuizRepository;

public class ItemView extends JPanel {
    private JPanel mainPanel;
    private User currentUser;
    private JLabel livesItemLabel, timeItemLabel;
    private JButton useLivesItemButton, useTimeItemButton, backButton;
    private int livesItemCount; // 목숨 +1 아이템 개수
    private int timeItemCount;  // 30초 추가 아이템 개수
    private String selectedCategory;

    public ItemView(JPanel mainPanel, User currentUser, String selectedCategory) {
        this.mainPanel = mainPanel;
        this.currentUser = currentUser;

        // 유저의 아이템 상태 (예: 데이터베이스나 User 객체에서 가져올 수 있음)
        this.livesItemCount = currentUser.getLifeItem(); // 예: User 모델에서 가져오기
        this.timeItemCount = currentUser.getTimeBoostItem();   // 예: User 모델에서 가져오기

        setLayout(new BorderLayout());

        // 상단 패널: 현재 아이템 개수 표시
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        livesItemLabel = new JLabel("목숨 +1 아이템: " + livesItemCount + "개");
        timeItemLabel = new JLabel("30초 추가 아이템: " + timeItemCount + "개");
        topPanel.add(livesItemLabel);
        topPanel.add(timeItemLabel);

        // 중앙 패널: 아이템 사용 버튼
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        useLivesItemButton = new JButton("목숨 +1 아이템 사용");
        useTimeItemButton = new JButton("30초 추가 아이템 사용");

        useLivesItemButton.addActionListener(e -> useLivesItem());
        useTimeItemButton.addActionListener(e -> useTimeItem());

        centerPanel.add(useLivesItemButton);
        centerPanel.add(useTimeItemButton);

        // 하단 패널: 뒤로가기 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        backButton = new JButton("뒤로가기");
        backButton.addActionListener(e -> goToCategoryView());
        
        JButton startGameButton = new JButton("게임 시작");
        startGameButton.addActionListener(e -> startGame());
        bottomPanel.add(startGameButton);
        bottomPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void useLivesItem() {
        if (livesItemCount > 0) {
            livesItemCount--;
            currentUser.setLifeItem(currentUser.getLifeItem() + 1); // 유저 목숨 +1
            livesItemLabel.setText("목숨 +1 아이템: " + livesItemCount + "개");
            System.out.println("목숨 +1 아이템 사용됨. 남은 개수: " + livesItemCount);
        } else {
            System.out.println("아이템이 부족합니다.");
        }
    }

    private void useTimeItem() {
        if (timeItemCount > 0) {
            timeItemCount--;
            currentUser.setTimeBoostItem(currentUser.getTimeBoostItem() + 30); // 게임 시간 +30초
            timeItemLabel.setText("30초 추가 아이템: " + timeItemCount + "개");
            System.out.println("30초 추가 아이템 사용됨. 남은 개수: " + timeItemCount);
        } else {
            System.out.println("아이템이 부족합니다.");
        }
    }

    private void goToCategoryView() {
        mainPanel.removeAll();
        mainPanel.add(new CategoryView(mainPanel, currentUser));
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private void startGame() {
        List<Quiz> questions = new QuizRepository().getQuestions(currentUser.getUsername(), selectedCategory);
        mainPanel.removeAll();
        mainPanel.add(new GameView(mainPanel, questions, currentUser, livesItemCount, timeItemCount));
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
