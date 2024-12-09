package view;

import model.Item;
import model.User;
import repository.StoreRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StoreView extends JPanel {
    private final StoreRepository storeRepository;
    private final User currentUser;
    private final JPanel mainPanel;
    private JLabel characterImageLabel;
    private JLabel pointsLabel; // 포인트를 표시할 라벨 추가

    public StoreView(JPanel mainPanel, User currentUser) {
        this.mainPanel = mainPanel;
        this.currentUser = currentUser;
        this.storeRepository = new StoreRepository();

        setLayout(new BorderLayout());

        // 상단 패널: 캐릭터 이미지 및 포인트 정보
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // 캐릭터 이미지 라벨
        characterImageLabel = new JLabel();
        characterImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateCharacterImage(); // 캐릭터 이미지 로드
        topPanel.add(characterImageLabel, BorderLayout.CENTER);

        // 포인트 정보 라벨
        pointsLabel = new JLabel("포인트: " + currentUser.getPoints());
        // pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(pointsLabel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("상점");
        // titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(titleLabel, BorderLayout.SOUTH);

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
                    currentUser.setPoints(currentUser.getPoints() - item.getPrice()); // 포인트 차감
                    updatePointsLabel(); // 포인트 갱신
                    purchaseButton.setEnabled(false);
                    if ("accessory".equals(item.getType())) {
                        wearButton.setEnabled(true);
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

    private void updatePointsLabel() {
        pointsLabel.setText("포인트: " + currentUser.getPoints());
    }

    private void showMyPageView() {
        mainPanel.removeAll();
        mainPanel.add(new MyPageView(currentUser, mainPanel));
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
