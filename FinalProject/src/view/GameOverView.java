package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.User;
import repository.UserRepository;

public class GameOverView extends JPanel implements ActionListener {
    private JPanel mainPanel;
    private int totalScore;
    private JLabel scoreLabel;
    private JButton mainMenuButton, restartButton;
    private User currentUser; // 현재 사용자 정보

    public GameOverView(JPanel mainPanel, int totalScore, User currentUser) {
        this.mainPanel = mainPanel;
        this.totalScore = totalScore;
        this.currentUser = currentUser;
        System.out.println(currentUser.getUsername());
        
        setLayout(new BorderLayout());

        // 상단 패널: 점수 표시
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        scoreLabel = new JLabel("총 점수: " + totalScore);
        topPanel.add(scoreLabel);

        // 중앙 패널: 메인 화면, 다시 시작 버튼
        JPanel centerPanel = new JPanel();
        mainMenuButton = new JButton("메인 화면");
        restartButton = new JButton("다시 시작");

        mainMenuButton.addActionListener(this);
        restartButton.addActionListener(this);

        centerPanel.add(mainMenuButton);
        centerPanel.add(restartButton);

        // BorderLayout에 각 패널 추가
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mainMenuButton) {
            showMyPageView();
        } else if (e.getSource() == restartButton) {
            restartGame();
        }
    }

    private void showMyPageView() {
        mainPanel.removeAll();
        mainPanel.add(new MyPageView(currentUser, mainPanel));
        mainPanel.revalidate();
        mainPanel.repaint();

        updateUserPoints();
    }

    private void restartGame() {
        mainPanel.removeAll();
        mainPanel.add(new CategoryView(mainPanel, currentUser));
        mainPanel.revalidate();
        mainPanel.repaint();

        updateUserPoints();
    }

    private void updateUserPoints() {
        if (currentUser != null) {
            UserRepository userRepository = new UserRepository();
            int newScore = currentUser.getPoints() + totalScore;
            userRepository.updateUserPoints(currentUser.getUsername(), newScore);

            // 사용자 정보 업데이트
            currentUser.setPoints(newScore);
        } else {
            System.out.println("사용자 정보를 찾을 수 없습니다.");
        }
    }
}
