package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class IntroView extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JButton loginBtn, signUpBtn;
    
    private LoginView loginView;
    private SignupView signupView;

    public IntroView() {
        setTitle("Code&Conquer");
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 메인 패널 설정
        mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // 버튼을 세로로 정렬하기 위한 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // 로그인 버튼 이미지 설정
        loginBtn = new JButton(new ImageIcon("src/images/loginBtn.png"));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorderPainted(false);
        loginBtn.addActionListener(this);
        
        // 회원가입 버튼 이미지 설정
        signUpBtn = new JButton(new ImageIcon("src/images/signupBtn.png"));
        signUpBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpBtn.setContentAreaFilled(false); 
        signUpBtn.setBorderPainted(false); 
        signUpBtn.addActionListener(this);

        // 버튼을 버튼 패널에 추가
        buttonPanel.add(loginBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        buttonPanel.add(signUpBtn);

        // 버튼 패널을 메인 패널의 아래쪽에 추가
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // 메인 패널을 프레임에 추가
        add(mainPanel);

        // 로그인 뷰와 회원가입 뷰를 생성 (초기화)
        loginView = new LoginView();
        signupView = new SignupView();
    }
    
    // 배경 이미지를 위한 커스텀 JPanel 클래스
    class BackgroundPanel extends JPanel{
    	private Image backgroundImage;
    	public BackgroundPanel() {
    		backgroundImage = new ImageIcon("src/images/welcome.png").getImage();
    	}
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 패널 크기에 맞게 이미지 그리기
        }
    }

    // 로그인 화면 전환 메서드 (패널 전환)
    private void showLoginView() {
        mainPanel.removeAll(); // 기존 패널을 제거
        mainPanel.add(loginView); // 로그인 패널 추가
        mainPanel.revalidate(); // 레이아웃 갱신
        mainPanel.repaint();
    }

    // 회원가입 화면 전환 메서드 (패널 전환)
    private void showSignUpView() {
        mainPanel.removeAll(); // 기존 패널을 제거
        mainPanel.add(signupView); // 회원가입 패널 추가
        mainPanel.revalidate(); // 레이아웃 갱신
        mainPanel.repaint();
    }

    // 버튼 클릭 이벤트 처리
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            showLoginView(); 
        } else if (e.getSource() == signUpBtn) {
            showSignUpView();
        }
    }
}

