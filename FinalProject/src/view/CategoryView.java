package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.User;
import model.Quiz;
import repository.QuizRepository;

public class CategoryView extends JPanel implements ActionListener {
    private JPanel mainPanel;
    private JLabel categoryNameLabel;
    private JButton cat1, cat2, cat3, cat4;
    private User currentUser;
    private QuizRepository quizRepository;

    public CategoryView(JPanel mainPanel, User currentUser) {
        this.mainPanel = mainPanel;
        this.currentUser = currentUser;
        this.quizRepository = new QuizRepository(); // QuizRepository 초기화
        setLayout(new BorderLayout());

        System.out.println("카테고리 유저 객체 전달 디버깅: " + currentUser.getNickname());

        // 상단 라벨 패널 설정 (카테고리 이름 표시)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        categoryNameLabel = new JLabel("카테고리 선택");
        topPanel.add(categoryNameLabel);

        // 중앙 패널 설정 (카테고리 선택)
        JPanel centerPanel = new JPanel();
        cat1 = new JButton("프론트엔드");
        cat2 = new JButton("백엔드");
        cat3 = new JButton("자료구조");
        cat4 = new JButton("운영체제");

        centerPanel.add(cat1);
        centerPanel.add(cat2);
        centerPanel.add(cat3);
        centerPanel.add(cat4);
        cat1.addActionListener(this);
        cat2.addActionListener(this);
        cat3.addActionListener(this);
        cat4.addActionListener(this);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
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

        showItemView(selectedCategory);
    }



    
    private void showItemView(String category) {
        mainPanel.removeAll();
        mainPanel.add(new ItemView(mainPanel, currentUser, category));
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
