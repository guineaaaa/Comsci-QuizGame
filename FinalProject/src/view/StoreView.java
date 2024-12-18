package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import model.Item;
import model.User;
import repository.StoreRepository;
import repository.UserRepository;

public class StoreView extends JPanel {
    private static StoreRepository storeRepository;
    private static User currentUser;
    private static JPanel mainPanel;
    
    private JLabel characterImageLabel;
    private JLabel pointsLabel;
    private JLabel livesItemLabel, timeItemLabel;

    private int livesItemCount; // 목숨 +1 아이템 개수
    private int timeItemCount;  // 30초 추가 아이템 개수

    public StoreView(JPanel mainPanel, User currentUser) {
        this.mainPanel = mainPanel;
        this.currentUser = currentUser;
        this.storeRepository = new StoreRepository();

        // 최신 상태로 아이템 개수 갱신
        updateItemCounts();

        setLayout(new BorderLayout());

        // 상단 패널: 캐릭터 이미지 및 포인트 정보
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // 캐릭터 이미지 라벨
        characterImageLabel = new JLabel();
        characterImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateCharacterImage(); // 캐릭터 이미지 로드
        topPanel.add(characterImageLabel, BorderLayout.CENTER);

        // 포인트 정보 라벨, 아이템 정보 라벨 등을 새로운 패널에 추가
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // 포인트 정보 라벨
        pointsLabel = new JLabel("포인트: " + currentUser.getPoints());
        infoPanel.add(pointsLabel, BorderLayout.NORTH);

        livesItemLabel = new JLabel("목숨 추가 아이템 개수: " + livesItemCount+ "개");
        timeItemLabel = new JLabel("시간 추가 아이템 개수: " + timeItemCount+ "개");
        infoPanel.add(livesItemLabel);
        infoPanel.add(timeItemLabel);

        topPanel.add(infoPanel, BorderLayout.NORTH);

        // 아이템 목록 패널
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new GridLayout(0, 1, 10, 10));
        List<Item> items = storeRepository.getItems();
        for (Item item : items) {
            JPanel itemRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel itemNameLabel = new JLabel(item.getName() + " (" + item.getPrice() + "원)");
            JButton purchaseButton = new JButton("구매하기");
            JButton wearButton = new JButton("착용하기");

            wearButton.setEnabled(false); // 기본적으로 비활성화
            
            if (storeRepository.isItemOwned(currentUser.getUsername(), item.getItemId())) {
                purchaseButton.setEnabled(false); // 이미 구매한 아이템은 비활성화
                if ("accessory".equals(item.getType())) {
                    wearButton.setEnabled(true); // 착용 버튼 활성화
                }
            }

            // 구매 버튼 클릭
            purchaseButton.addActionListener(e -> {
                boolean success = storeRepository.purchaseItem(currentUser.getUsername(), item.getItemId());
                if (success) {
                    JOptionPane.showMessageDialog(StoreView.this, "구매 성공!");
                    currentUser.setPoints(currentUser.getPoints() - item.getPrice());
                    refreshPointsLabel(); // 포인트 갱신

                    // 아이템 종류에 따라 livesItemCount 또는 timeItemCount 갱신
                    if ("life".equals(item.getType())) {
                        livesItemCount++;
                        livesItemLabel.setText("목숨 추가 아이템 개수: " + livesItemCount);
                    } else if ("time_boost".equals(item.getType())) {
                        timeItemCount++;
                        timeItemLabel.setText("시간 추가 아이템 개수: " + timeItemCount);
                    }
                    if ("accessory".equals(item.getType())) {
                        wearButton.setEnabled(true); // 악세사리 착용 버튼 활성화
                    }
                } else {
                    JOptionPane.showMessageDialog(StoreView.this, "구매 실패: 포인트 부족 또는 이미 구매함.");
                }
            });

            // 착용 버튼 클릭
            wearButton.addActionListener(e -> {
                boolean success = storeRepository.updateCharacterImage(currentUser.getUsername(), item.getItemId());
                if (success) {
                    currentUser.setCharacterImage(storeRepository.getCharacterImagePath(currentUser.getUsername()));
                    JOptionPane.showMessageDialog(StoreView.this, "착용 성공!");
                    updateCharacterImage(); // 캐릭터 이미지 갱신
                } else {
                    JOptionPane.showMessageDialog(StoreView.this, "착용 실패.");
                }
            });

            itemRow.add(itemNameLabel);
            itemRow.add(purchaseButton);
            if ("accessory".equals(item.getType())) {
                itemRow.add(wearButton); // 악세사리에만 착용 버튼 추가
            }
            itemPanel.add(itemRow);
        }

        System.out.println("상점에서의 점수: " + currentUser.getPoints());

        // 하단 패널: 돌아가기 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton backButton = new JButton("돌아가기");
        backButton.addActionListener(e -> showMyPageView());
        bottomPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(itemPanel), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateCharacterImage() {
        String imagePath = storeRepository.getCharacterImagePath(currentUser.getUsername());
        if (imagePath != null) {
            characterImageLabel.setIcon(new ImageIcon(getClass().getResource(imagePath)));
        } else {
            characterImageLabel.setIcon(new ImageIcon(getClass().getResource("/images/character.png")));
        }
    }

    private void updateItemCounts() {
        this.livesItemCount = currentUser.getLifeItem();
        this.timeItemCount = currentUser.getTimeBoostItem();
    }

    public void refreshPointsLabel() {
        // DB에서 최신 포인트 정보를 가져오고 갱신한다...
        currentUser.setPoints(new UserRepository().getUserPoints(currentUser.getUsername()));
        pointsLabel.setText("포인트: " + currentUser.getPoints());
    }

    private void showMyPageView() {
        mainPanel.removeAll();
        mainPanel.add(new MyPageView(currentUser, mainPanel));
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
