package controller;

import javax.swing.JOptionPane;

import model.User;
import repository.UserRepository;
import view.IntroView;

public class Controller {
    private static UserRepository userRepository = new UserRepository();
    private static IntroView introView; // IntroView 인스턴스 저장

    public static void setIntroView(IntroView view) {
        introView = view; // IntroView를 설정
    }

    public static void handleSignup(String username, String password, String nickname) {
        boolean success = userRepository.saveUser(username, password, nickname);
        if (success) {
            System.out.println("회원가입 성공");
            JOptionPane.showMessageDialog(null, "회원가입 성공");
            
            // 회원가입 성공 시 LoginView 페이지로 전환
            if(introView!=null) {
            	introView.showLoginView();
            }
        } else {
            System.out.println("회원가입 실패: 중복된 아이디");
            JOptionPane.showMessageDialog(null, "이미 존재하는 아이디 입니다.");
        }
    }

    public static void handleLogin(String username, String password) {
        boolean success = userRepository.loginUser(username, password);
        if (success) {
            System.out.println("로그인 성공");
            JOptionPane.showMessageDialog(null, "로그인 성공");

            String nickname = userRepository.getUserNickname(username);
            
            // 로그인한 사용자의 정보를 가져온다.
            User currentUser = userRepository.getCurrentUser(username);
            
            // 로그인 성공 시 MyPage 화면으로 전환
            if (introView != null) {
                introView.showMyPageView(currentUser);
            }
        } else {
            System.out.println("로그인 실패");
            JOptionPane.showMessageDialog(null, "로그인 실패");
        }
    }
}
