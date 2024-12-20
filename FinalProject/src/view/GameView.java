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
import javax.swing.SwingUtilities;

import model.Quiz; // Quiz 객체를 포함한 모델 클래스
import model.User;
import repository.QuizRepository;
import repository.UserRepository;  

public class GameView extends JPanel implements ActionListener {
    private JPanel mainPanel; // 메인 패널
    private List<Quiz> questions; // 퀴즈 목록
    private int currentQuestionIndex = 0; //현재 퀴즈 인덱스
    
    private int lives = 5; 
    private int points = 0;
    private int totalTime = 10 * 60; // 10분
    private User currentUser; // 현재 사용자 정보
    
    private Timer timer; // 타이머 객체
    private JLabel questionLabel, livesLabel, pointsLabel, timerLabel; //화면 표시용 라벨
    private JButton option1Button, option2Button, option3Button; //문제 선택지 버튼

    public GameView(JPanel mainPanel, List<Quiz> questions, User currentUser, int livesItem, int timeBoostItem) {
        this.mainPanel = mainPanel;
        this.questions = questions;
        this.currentUser = currentUser;
        
        this.lives = 5 + livesItem; // 기본 목숨 + 아이템
        this.totalTime = 10 * 60 + timeBoostItem * 30; // 기본 10분 + 아이템 시간 추가
        System.out.println("게임 진행 화면 유저 객체 전달 디버깅: " + currentUser.getNickname());

        setLayout(new BorderLayout());

        // 상단 패널: 목숨, 점수, 타이머 표시
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        livesLabel = new JLabel("목숨: " + lives);
        pointsLabel = new JLabel("점수: " + points);
        timerLabel = new JLabel("남은 시간: " + totalTime / 60 + "분 " + totalTime % 60 + "초");
        topPanel.add(livesLabel); 
        topPanel.add(pointsLabel); 
        topPanel.add(timerLabel);

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

    // 문제 로드 메서드
    private void loadQuestion() {
    	System.out.println("문제 받아와짐");
        if (currentQuestionIndex >= questions.size()) { // 모든 문제를 완료한 경우
            showGameOverview(); // 게임 종료 화면으로 이동
            return;
        }

        Quiz question = questions.get(currentQuestionIndex); //현재 문제 가져오기
        questionLabel.setText((currentQuestionIndex + 1) + ". " + question.getTitle()); // 문제 제목 설정
        option1Button.setText(question.getOption1()); // 선택지1 설정
        option2Button.setText(question.getOption2()); // 선택지2 설정
        option3Button.setText(question.getOption3()); // 선택지3 설정
    }

    // 타이머 시작 메서드
    private void startTimer() {
        timer = new Timer(); // 타이머 초기화
        timer.scheduleAtFixedRate(new TimerTask() { //일정 간격으로 작업을 수행한다.
            @Override
            public void run() {
                totalTime--; //남은 시간 감소
                timerLabel.setText("남은 시간: " + totalTime / 60 + "분 " + totalTime % 60 + "초");

                if (totalTime <= 0) { //시간이 다 된 경우
                    timer.cancel(); //타이머를 중지하고
                    showGameOverview(); //게임 종료 화면으로 이동한다.
                }
            }
        }, 0, 1000); // 1초마다 run()이 실행 된다.
    }

    // 정답 체크 메서드
    private void checkAnswer(int selectedOptionIndex) {
    	System.out.println("checkAnswer 호출됨. 선택된 옵션: " + selectedOptionIndex);
        Quiz currentQuestion = questions.get(currentQuestionIndex);

        if (selectedOptionIndex == currentQuestion.getCorrectOption()) {
            points += 40;
            SwingUtilities.invokeLater(() -> pointsLabel.setText("점수: " + points)); // UI 업데이트
            
            QuizRepository quizRepository = new QuizRepository();
            quizRepository.markQuizAsCompleted(
                    currentUser.getUsername(),
                    currentQuestion.getQuizId(),
                    currentQuestion.getCategoryId()
                );
            System.out.println("markQuizAsCompleted 호출 완료.");
        } else {
            lives--;
            SwingUtilities.invokeLater(() -> livesLabel.setText("목숨: " + lives));
            if (lives <= 0) {
                showGameOverview();
                return;
            }
        }

        revalidate();
        repaint();           
        currentQuestionIndex++;
        loadQuestion();      
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

    // 게임 종료 화면 표시 메서드
    private void showGameOverview() {
        String message = "게임 종료!\n최종 점수: " + points;
        if (lives <= 0) {
            message += "\n목숨이 모두 소진되었습니다.";
        } else if (totalTime <= 0) {
            message += "\n시간이 초과되었습니다.";
        } else {
            message += "\n모든 문제를 완료했습니다!";
        }
        System.out.println("게임오버뷰 화면 전환 전 디버깅: " + currentUser.getNickname());

        JOptionPane.showMessageDialog(this, message, "게임종료",JOptionPane.INFORMATION_MESSAGE);
        
        // 유저 점수 업데이트
        UserRepository userRepository = new UserRepository();
        userRepository.updateUserPoints(currentUser.getUsername(), points);

        mainPanel.removeAll();
        mainPanel.add(new GameOverView(mainPanel, points, currentUser));  // currentUser를 전달
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
