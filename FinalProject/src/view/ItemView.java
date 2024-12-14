package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Quiz;
import model.User;
import repository.QuizRepository;
import repository.UserRepository;

public class ItemView extends JPanel implements ActionListener {
    private JPanel mainPanel;
    private User currentUser;
    private JLabel livesItemLabel, timeItemLabel;
    private JButton useLivesItemButton, useTimeItemButton, backButton, startGameButton;
    private int livesItemCount; // 목숨 +1 아이템 개수
    private int timeItemCount;  // 30초 추가 아이템 개수
    private String selectedCategory;

    private int useTimeItemCount = 0, useLivesItemCount = 0; // 사용된 아이템 개수

    public ItemView(JPanel mainPanel, User currentUser, String selectedCategory) {
        this.mainPanel = mainPanel;
        this.currentUser = currentUser;
        this.selectedCategory = selectedCategory;

        // 유저의 아이템 상태
        this.livesItemCount = currentUser.getLifeItem();
        this.timeItemCount = currentUser.getTimeBoostItem();

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
        
        useLivesItemButton.addActionListener(this);
        useTimeItemButton.addActionListener(this);

        centerPanel.add(useLivesItemButton);
        centerPanel.add(useTimeItemButton);

        // 하단 패널: 뒤로가기 및 게임 시작 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        backButton = new JButton("뒤로가기");
        startGameButton = new JButton("게임 시작");
        
        backButton.addActionListener(this);
        startGameButton.addActionListener(this);

        bottomPanel.add(startGameButton);
        bottomPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == useLivesItemButton) {
            useLivesItem();
        } else if (e.getSource() == useTimeItemButton) {
            useTimeItem();
        } else if (e.getSource() == backButton) {
            goToCategoryView();
        } else if (e.getSource() == startGameButton) {
            startGame();
        }
    }

    private void useLivesItem() {
        if (livesItemCount > 0) {
            livesItemCount--; // 실제 DB의 값 감소
            useLivesItemCount++; // 게임 중 사용할 아이템 증가
            currentUser.setLifeItem(livesItemCount); // 유저 객체 업데이트
            livesItemLabel.setText("목숨 +1 아이템: " + livesItemCount + "개");

            // DB 업데이트
            new UserRepository().updateItemCount(currentUser.getUsername(), livesItemCount, currentUser.getTimeBoostItem());
            System.out.println("목숨 +1 아이템 사용됨. 남은 개수: " + livesItemCount);
        } else {
            System.out.println("아이템이 부족합니다.");
        }
    }

    private void useTimeItem() {
        if (timeItemCount > 0) {
            timeItemCount--;
            useTimeItemCount++;
            currentUser.setTimeBoostItem(timeItemCount); // 유저 객체 업데이트
            timeItemLabel.setText("30초 추가 아이템: " + timeItemCount + "개");

            new UserRepository().updateItemCount(currentUser.getUsername(), currentUser.getLifeItem(), timeItemCount);
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
        System.out.println("게임 시작 카테고리: " + selectedCategory);
        System.out.println("아이템 개수DB - 목숨: " + livesItemCount + ", 시간: " + timeItemCount);
        System.out.println("사용자 선택 아이템 - 목숨: " + useLivesItemCount + ", 시간: " + useTimeItemCount);

        // QuizRepository를 사용하여 퀴즈 문제 가져오기
        List<Quiz> questions = new QuizRepository().getQuestions(currentUser.getUsername(), selectedCategory);
        mainPanel.removeAll();
        mainPanel.add(new GameView(mainPanel, questions, currentUser, useLivesItemCount, useTimeItemCount));
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
