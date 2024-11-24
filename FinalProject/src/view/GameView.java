package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Quiz; // Quiz 객체를 포함한 모델 클래스
import model.User;

public class GameView extends JPanel implements ActionListener {
    private JPanel mainPanel;
    private List<Quiz> questions;
    private int currentQuestionIndex = 0;
    private int lives = 5; private int points = 0;
    private int totalTime = 10 * 60; // 10분
    private User currentUser;
    
    private Timer timer;
    private JLabel questionLabel, livesLabel, pointsLabel, timerLabel;
    private JButton option1Button, option2Button, option3Button;

    public GameView(JPanel mainPanel, List<Quiz> questions,User currentUser) {
        this.mainPanel = mainPanel;
        this.questions = questions;
        System.out.println("게임 진행 화면 유저 객체 전달 디버깅"+currentUser.getNickname());
		

        setLayout(new BorderLayout());
        
        // 상단 패널: 목숨, 점수, 타이머 표시
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        livesLabel = new JLabel("목숨: " + lives);
        pointsLabel = new JLabel("점수: " + points);
        timerLabel = new JLabel("남은 시간: " + totalTime / 60 + "분 " + totalTime % 60 + "초");
        topPanel.add(livesLabel); topPanel.add(pointsLabel); topPanel.add(timerLabel);
        
        // 중앙 패널: 문제 및 선택지 표시
        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        questionLabel = new JLabel("", JLabel.CENTER);
        questionLabel.setFont(new Font("Serif", Font.BOLD, 18));
        option1Button = new JButton();
        option2Button = new JButton();
        option3Button = new JButton();

        centerPanel.add(questionLabel);
        centerPanel.add(option1Button);
        centerPanel.add(option2Button);
        centerPanel.add(option3Button);

        option1Button.addActionListener(this);
        option2Button.addActionListener(this);
        option3Button.addActionListener(this);

        // 하단 패널: 게임 종료 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton quitButton = new JButton("종료");
        quitButton.addActionListener(e -> showGameOverview());
        bottomPanel.add(quitButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // 첫 문제 로드 및 타이머 시작
        loadQuestion();
        startTimer();
    }

    private void loadQuestion() {
        if (currentQuestionIndex >= questions.size()) {
        	showGameOverview();
            return;
        }

        Quiz question = questions.get(currentQuestionIndex);
        // System.out.println("정답 확인: " + question.getCorrectOption());
        questionLabel.setText((currentQuestionIndex + 1) + ". " + question.getTitle());
        option1Button.setText(question.getOption1());
        option2Button.setText(question.getOption2());
        option3Button.setText(question.getOption3());
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                totalTime--;
                timerLabel.setText("남은 시간: " + totalTime / 60 + "분 " + totalTime % 60 + "초");

                if (totalTime <= 0) {
                    timer.cancel();
                    showGameOverview();
                }
            }
        }, 0, 1000); // 1초마다 갱신
    }

    private void checkAnswer(int selectedOptionIndex) {
        Quiz currentQuestion = questions.get(currentQuestionIndex);

        System.out.println("선택된 옵션: " + selectedOptionIndex);
        System.out.println("정답 옵션: " + currentQuestion.getCorrectOption());

        if (selectedOptionIndex == currentQuestion.getCorrectOption()) {
            System.out.println("정답 맞추었음");
            String difficulty = currentQuestion.getDifficulty();
            System.out.println("문제 난이도: " + difficulty);  
            // 점수 갱신 전 로그 추가
            System.out.println("점수 업데이트 전: " + points);

            if ("easy".equals(difficulty)) {
                points += 2;
            } else if ("medium".equals(difficulty)) {
                points += 3;
            } else if ("hard".equals(difficulty)) {
                points += 4;
            }

            // 점수 갱신 후 로그 추가
            System.out.println("점수 업데이트 후: " + points);
            pointsLabel.setText("점수: " + points);  // 점수 업데이트
        } else {
            lives--;
            livesLabel.setText("목숨: " + lives);  // 목숨 업데이트
            if (lives <= 0) {
            	showGameOverview();  // 목숨이 0 이하가 되면 게임 종료
                return;
            }
        }

        // 레이아웃 갱신과 화면 다시 그리기
        revalidate();
        repaint();
        // 문제를 풀고 나서 다음 문제로 이동
        currentQuestionIndex++;
        loadQuestion();  // 새로운 문제 로드
    }


 
    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedOptionIndex = 0; // 초기화

        // 클릭된 버튼에 따라 selectedOptionIndex 설정
        if (e.getSource() == option1Button) {
            selectedOptionIndex = 1; // 버튼 1의 인덱스는 1
        } else if (e.getSource() == option2Button) {
            selectedOptionIndex = 2; // 버튼 2의 인덱스는 2
        } else if (e.getSource() == option3Button) {
            selectedOptionIndex = 3; // 버튼 3의 인덱스는 3
        }

        checkAnswer(selectedOptionIndex);
    }

    private void showGameOverview() {
        String message = "게임 종료!\n최종 점수: " + points;
        if (lives <= 0) {
            message += "\n목숨이 모두 소진되었습니다.";
        } else if (totalTime <= 0) {
            message += "\n시간이 초과되었습니다.";
        } else {
            message += "\n모든 문제를 완료했습니다!";
        }
        System.out.println("게임오버뷰 화면 전환 전 디버깅"+currentUser.getNickname());

        // GameOverView로 화면 전환하면서 currentUser 객체도 전달
        mainPanel.removeAll();
        mainPanel.add(new GameOverView(mainPanel, points, currentUser));  // currentUser를 전달
        mainPanel.revalidate();
        mainPanel.repaint();
    }

}
