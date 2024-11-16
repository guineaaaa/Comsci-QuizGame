package view;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LoginView extends JPanel{
	public LoginView() {
		setLayout(new BorderLayout());
        JLabel label = new JLabel("Login View", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
	}

}
