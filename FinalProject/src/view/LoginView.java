package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controller.Controller;

// 로그인 UI를 생성하고, 사용자가 입력한 데이터를 컨트롤러로 전달
public class LoginView extends JPanel{
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	
    public LoginView() {
        setLayout(new GridLayout(3, 2));
        setSize(700, 400);

        // UI 요소
        JLabel usernameLabel = new JLabel("아이디: ");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("비밀번호: ");
        passwordField = new JPasswordField();
        loginButton = new JButton("로그인");

        // 컴포넌트 추가하기
        add(usernameLabel); 
        add(usernameField); 
        add(passwordLabel); 
        add(passwordField);
        add(new JLabel()); 
        add(loginButton);

        // 버튼 액션
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (!username.isEmpty() && !password.isEmpty()) {
                    // 컨트롤러에 데이터 전달
                    controller.Controller.handleLogin(username, password);
                } else {
                    JOptionPane.showMessageDialog(null, "모든 필드를 채워주세요");
                }
            }
        });
    }
}
