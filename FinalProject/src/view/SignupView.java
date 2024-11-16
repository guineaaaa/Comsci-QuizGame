package view;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SignupView extends JPanel{
    public SignupView() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("SignUp View", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
