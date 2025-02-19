package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.User;

public class MyPageView extends JPanel implements ActionListener {
    private JLabel userIdLabel, nicknameLabel,characterImageLabel; // 사용자 ID와 닉네임 라벨
    private JButton progressButton;    // 진행 현황 버튼
    private JButton storeButton;       // 상점 버튼
    private JButton startGameButton;   // 게임 시작 버튼

    private JPanel mainPanel; // 전환 대상이 되는 부모 패널 (MainView에서 전달)
    private User currentUser; // User 객체 추가
    
    public MyPageView(User currentUser, JPanel mainPanel) {
        this.currentUser = currentUser; // User 객체 저장
        this.mainPanel = mainPanel; // 부모 패널을 저장

        setLayout(new BorderLayout()); // BorderLayout 사용

        // 상단 버튼 패널 설정 (진행 현황 버튼과 상점 버튼)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 중앙 정렬
        progressButton = new JButton("진행 현황");
        storeButton = new JButton("상점");
        progressButton.addActionListener(this);
        storeButton.addActionListener(this);
        topPanel.add(progressButton);
        topPanel.add(storeButton);

        // 중앙 패널 설정 (사용자 정보 표시)
        JPanel centerPanel = new JPanel();
        userIdLabel = new JLabel("사용자 ID: " + currentUser.getUsername()); // User 객체 사용
        nicknameLabel = new JLabel("사용자 별명: " + currentUser.getNickname()); // User 객체 사용
        centerPanel.add(userIdLabel);
        centerPanel.add(nicknameLabel);
        
        // 캐릭터 이미지
        characterImageLabel = new JLabel();
        ImageIcon characterIcon=new ImageIcon(getClass().getResource(currentUser.getCharacterImage()));
        characterImageLabel.setIcon(characterIcon);
       
        centerPanel.add(characterImageLabel);
        
        // 하단 버튼 패널 설정 (게임 시작 버튼)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        startGameButton = new JButton("게임 시작");
        startGameButton.addActionListener(this);
        bottomPanel.add(startGameButton);

        
        System.out.println("마이페이지 에서의 포인트: "+currentUser.getPoints());
        // BorderLayout에 각각 추가
        add(topPanel, BorderLayout.NORTH);  // 상단 패널
        add(centerPanel, BorderLayout.CENTER);  // 중앙 패널
        add(bottomPanel, BorderLayout.SOUTH);  // 하단 패널
    }

    // 진행 현황 화면 전환 메서드
    private void showProgressView() {
        mainPanel.removeAll(); // 기존 패널 제거
        mainPanel.add(new ProgressView(mainPanel,currentUser)); // 진행 현황 패널 추가
        mainPanel.revalidate(); // 레이아웃 갱신
        mainPanel.repaint();
    }

    // 상점 화면 전환 메서드
    private void showStoreView() {
        mainPanel.removeAll(); // 기존 패널 제거
        StoreView storeView=new StoreView(mainPanel, currentUser);
        storeView.refreshPointsLabel();
        mainPanel.add(storeView);
        mainPanel.revalidate(); // 레이아웃 갱신
        mainPanel.repaint();
    }

    // 게임 시작 화면 전환 메서드
    private void startGame(User currentUser) {
        mainPanel.removeAll(); // 기존 패널 제거
        mainPanel.add(new CategoryView(mainPanel, currentUser)); // 게임 카테고리 화면 추가
        mainPanel.revalidate(); // 레이아웃 갱신
        mainPanel.repaint();
    }

    // 버튼 클릭 이벤트 처리
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == progressButton) {
            showProgressView();
        } else if (e.getSource() == storeButton) {
            showStoreView();
        } else if (e.getSource() == startGameButton) {
            startGame(currentUser);
        }
    }
    
    /*
    public void updateCurrentUser(User user) {
    	this.currentUser=user;
    }
    */
}
