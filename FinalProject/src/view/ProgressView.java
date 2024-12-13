package view;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ProgressView extends JPanel {
	private JPanel mainPanel;
	
	public ProgressView(JPanel mainPanel) {
		this.mainPanel=mainPanel;	
		setLayout(new BorderLayout());
		
        // 간단한 진행 현황 표시
        JLabel progressLabel = new JLabel("진행 현황 화면입니다.");
        progressLabel.setHorizontalAlignment(JLabel.CENTER);
        add(progressLabel, BorderLayout.CENTER);
	}


}
