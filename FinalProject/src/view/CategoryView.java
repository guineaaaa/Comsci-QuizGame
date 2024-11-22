package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CategoryView extends JPanel implements ActionListener{
	private JPanel mainPanel;
	private JLabel categoryNameLabel;
	private JButton cat1,cat2,cat3,cat4;
	
	public CategoryView(JPanel mainPanel) {
		this.mainPanel=mainPanel;
		setLayout(new BorderLayout());
		
		// 상단 라벨 패널 설정 (카테고리 이름 표시)
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		categoryNameLabel=new JLabel("카테고리 선택");
		topPanel.add(categoryNameLabel);
		
		// 중앙 패널 설정 (카테고리 선택)
		JPanel centerPanel=new JPanel();
		cat1=new JButton("프론트엔드");
		cat2=new JButton("백엔드");
		cat3=new JButton("자료구조");
		cat4=new JButton("운영체제");
		
		centerPanel.add(cat1); centerPanel.add(cat2); centerPanel.add(cat3); centerPanel.add(cat4);
        cat1.addActionListener(this); cat2.addActionListener(this);
        cat3.addActionListener(this); cat4.addActionListener(this);
        
        add(topPanel, BorderLayout.NORTH); add(centerPanel, BorderLayout.CENTER);
        
	}
    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedCategory = "";

        if (e.getSource() == cat1) {
            selectedCategory = "프론트엔드";
        } else if (e.getSource() == cat2) {
            selectedCategory = "백엔드";
        } else if (e.getSource() == cat3) {
            selectedCategory = "자료구조";
        } else if (e.getSource() == cat4) {
            selectedCategory = "운영체제";
        }

        // 카테고리 선택 후 DifficultyView로 전환
        showDifficultyView(selectedCategory);
    }
    
    private void showDifficultyView(String category) {
        mainPanel.removeAll(); // 기존 패널 제거
        mainPanel.add(new DifficultyView(mainPanel, category)); // 선택된 카테고리로 DifficultyView 추가
        mainPanel.revalidate(); // 레이아웃 갱신
        mainPanel.repaint();
    }
}
