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
		this.mainPanel=mainPanel; //생성자 매개변수로 받은 값을 클래스 내부 멤버 변수에 저장
		this.currentUser=currentUser;
		// View가 생성될 때, 데이터를 바로 가져와야 하기때문에 생성자에서 레포지토리를 초기화해야 한다.
		// View가 생성되자마자 실행되어야 하기 때문 이다.
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
