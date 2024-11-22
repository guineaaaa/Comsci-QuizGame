package view;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StoreView extends JPanel{
	private JPanel mainPanel;
	
	public StoreView(JPanel mainPanel) {
		this.mainPanel=mainPanel;
		setLayout(new BorderLayout());
		
		// 상점 화면
		JLabel storeLabel=new JLabel("상점 화면입니다.");
		add(storeLabel, BorderLayout.CENTER);
		
	}

}
