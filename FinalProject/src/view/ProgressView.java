package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.User;
import repository.QuizRepository;
import repository.StoreRepository;
import repository.UserRepository;

public class ProgressView extends JPanel implements ActionListener{
	private QuizRepository quizRepository;
	private StoreRepository storeRepository;
	private JPanel mainPanel;
	private JLabel progressLabel, characterImageLabel;
	private User currentUser;
	private JButton backButton;
	
	public ProgressView(JPanel mainPanel, User currentUser) {
		this.mainPanel=mainPanel;
		this.currentUser=currentUser;
		quizRepository=new QuizRepository();
		storeRepository=new StoreRepository();
		
		setLayout(new BorderLayout());
		
		int userSolvedQuiz=quizRepository.getCountUserQuiz(currentUser.getUsername());
		progressLabel=new JLabel("현재 내가 푼 문제 개수는: "+userSolvedQuiz+" 개 입니다.");
        progressLabel.setHorizontalAlignment(JLabel.CENTER);
        add(progressLabel, BorderLayout.CENTER);
        
		characterImageLabel=new JLabel();
		updateCharacterImage();
		add(characterImageLabel, BorderLayout.NORTH);
		
		backButton=new JButton("돌아가기");
		backButton.addActionListener(this);
		add(backButton, BorderLayout.SOUTH);
		
		
	}
	
	private void updateCharacterImage() {
		String imagePath=storeRepository.getCharacterImagePath(currentUser.getUsername());
        if (imagePath != null) {
            characterImageLabel.setIcon(new ImageIcon(getClass().getResource(imagePath)));
        } else {
            characterImageLabel.setIcon(new ImageIcon(getClass().getResource("/images/character.png")));
        }
	}
	
	private void showMyPageView() {
        mainPanel.removeAll();
        mainPanel.add(new MyPageView(currentUser, mainPanel));
        mainPanel.revalidate();
        mainPanel.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==backButton) {
			showMyPageView();
		}
	}

}
