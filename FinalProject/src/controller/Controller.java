package controller;

import javax.swing.JOptionPane;

import repository.UserRepository;
import view.MyPage;

public class Controller {
	private static UserRepository userRepository=new UserRepository();
	
	public static void handleSignup(String username, String password) {
		boolean success=userRepository.saveUser(username, password);
		if(success) {
			System.out.println("회원가입 성공");
			JOptionPane.showMessageDialog(null, "회원가입 성공");
			
			// 회원 가입 성공 시 MyPage 화면으로 이동
			// new MyPage().setVisible(true);
		}else {
			System.out.println("회원가입 실패: 중복된 아이디");
			JOptionPane.showMessageDialog(null, "이미 존재하는 아이디 입니다.");
		}
	}
	
	public static void handleLogin(String username, String password) {
		boolean success=userRepository.loginUser(username, password);
		if(success) {
			System.out.println("로그인 성공");
			JOptionPane.showMessageDialog(null, "로그인 성공");
		}else {
			System.out.println("로그인 실패");
			JOptionPane.showMessageDialog(null, "로그인 실패");
		}
		
	}
}
