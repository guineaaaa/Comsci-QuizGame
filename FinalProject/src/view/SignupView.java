package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

// 회원가입 UI를 생성하고, 사용자가 입력한 데이터를 컨트롤러로 전달한다.
public class SignupView extends JFrame{
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton signupButton;
	
    public SignupView() {
    	setTitle("회원가입");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setLayout(new GridLayout(3,2));
    	setSize(700, 400);

    	
    	// UI 요소
    	JLabel usernameLabel=new JLabel("아이디: ");
    	usernameField=new JTextField();
    	JLabel passwordLabel=new JLabel("비밀번호: ");
    	passwordField=new JPasswordField();
    	signupButton =new JButton("회원가입");
    	
    	// 컴포넌트 추가하기
    	add(usernameLabel); add(usernameField); add(passwordLabel); add(passwordField);
    	add(new JLabel()); add(signupButton);
    	
    	// 버튼 액션
       signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (!username.isEmpty() && !password.isEmpty()) {
                    // 컨트롤러에 데이터 전달
                    controller.Controller.handleSignup(username, password);
                } else {
                    JOptionPane.showMessageDialog(null, "모든 필드를 채워주세요");
                }
            }
        });
    }
}
