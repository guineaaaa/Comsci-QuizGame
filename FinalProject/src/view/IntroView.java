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
import javax.swing.SwingUtilities;

public class IntroView extends JFrame implements ActionListener {
    private JPanel panel;
    private JButton loginBtn, signUpBtn;
    
    private LoginView loginView;
    private SignupView signupView;

    public IntroView() {
        setTitle("Code&Conquer");
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 커스텀 배경 패널 설정
        panel = new BackgroundPanel();
        panel.setLayout(new BorderLayout());
        
        // 버튼을 세로로 정렬하기 위한 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false); // 버튼 패널을 투명하게 설정하여 배경이 보이게 함

        // 로그인 버튼 이미지 설정
        loginBtn = new JButton(new ImageIcon("src/images/loginBtn.png"));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setContentAreaFilled(false); // 배경 제거
        loginBtn.setBorderPainted(false); // 테두리 제거
        loginBtn.addActionListener(this);
        
        // 회원가입 버튼 이미지 설정
        signUpBtn = new JButton(new ImageIcon("src/images/signupBtn.png"));
        signUpBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpBtn.setContentAreaFilled(false); 
        signUpBtn.setBorderPainted(false); 
        signUpBtn.addActionListener(this);

        // 버튼을 버튼 패널에 추가
        buttonPanel.add(loginBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // 버튼 사이 간격
        buttonPanel.add(signUpBtn);

        // 버튼 패널을 메인 패널의 아래쪽에 추가
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // 메인 패널을 프레임에 추가
        add(panel);
        
        // 로그인 뷰와 회원가입 뷰를 생성
        loginView=new LoginView();
        signupView=new SignupView();
        
    }

    // 배경 이미지를 위한 커스텀 JPanel 클래스
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        public BackgroundPanel() {
            // 배경 이미지 로드
            backgroundImage = new ImageIcon("src/images/welcome.png").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 패널 크기에 맞게 이미지 그리기
        }
    }
    // 로그인 화면 전환 메서드
    private void showLoginView() {
        getContentPane().removeAll();  // 기존의 모든 컴포넌트 제거
        getContentPane().add(loginView);  // 로그인 화면 추가
        revalidate();  // 레이아웃 갱신
        repaint();  // 화면 갱신
    }

    // 회원가입 화면 전환 메서드
    private void showSignUpView() {
        getContentPane().removeAll();  // 기존의 모든 컴포넌트 제거
        getContentPane().add(signupView);  // 회원가입 화면 추가
        revalidate();  // 레이아웃 갱신
        repaint();  // 화면 갱신
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 버튼 클릭 처리
        if (e.getSource() == loginBtn) {
            System.out.println("로그인 버튼 클릭");
            showLoginView(); 
        } else if (e.getSource() == signUpBtn) {
            System.out.println("회원가입 버튼 클릭");
            showSignUpView();
        }
    }
    /*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new IntroView().setVisible(true);
        });
    }*/

}
