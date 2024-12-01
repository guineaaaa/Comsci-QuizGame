package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.Item;
import model.User;
import repository.StoreRepository;

public class StoreView extends JPanel {
    private JPanel mainPanel;
    private User currentUser;
    private StoreRepository storeRepository;
    private JLabel characterImageLabel;

    public StoreView(JPanel mainPanel, User currentUser) {
        this.mainPanel = mainPanel;
        this.currentUser = currentUser;
        this.storeRepository = new StoreRepository();

        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        // 상단 패널: 목숨 아이템과 시간 추가 아이템 구매 버튼
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // 가격 정보와 함께 버튼 추가
        int lifeItemPrice = getItemPriceByName("목숨 +1");
        int timeBoostItemPrice = getItemPriceByName("시간 +30초");

        JButton buyLifeButton = new JButton("목숨 +1 아이템 구매 (" + lifeItemPrice + " 포인트)");
        JButton buyTimeBoostButton = new JButton("시간 +30초 아이템 구매 (" + timeBoostItemPrice + " 포인트)");
        JLabel pointsLabel = new JLabel("포인트: " + currentUser.getPoints());

        buyLifeButton.addActionListener(e -> purchaseLifeItem(pointsLabel));
        buyTimeBoostButton.addActionListener(e -> purchaseTimeBoostItem(pointsLabel));

        topPanel.add(buyLifeButton);
        topPanel.add(buyTimeBoostButton);
        topPanel.add(pointsLabel);
        add(topPanel, BorderLayout.NORTH);

        // 중앙 패널: 현재 캐릭터 이미지
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        characterImageLabel = new JLabel();
        ImageIcon characterIcon = new ImageIcon(getClass().getResource(currentUser.getCharacterImage()));
        characterImageLabel.setIcon(characterIcon);
        centerPanel.add(characterImageLabel);
        add(centerPanel, BorderLayout.CENTER);

        // 오른쪽 패널: 액세서리 아이템 목록
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(2, 1, 10, 10));

        // Get all items from the repository
        List<Item> allItems = storeRepository.getItems();

        // Filter accessory items
        for (Item item : allItems) {
            if ("accessory".equals(item.getType())) {
                JButton itemButton = new JButton(item.getName() + " (" + item.getPrice() + " 포인트)");
                itemButton.addActionListener(e -> purchaseAccessoryItem(item, pointsLabel, characterImageLabel));
                rightPanel.add(itemButton);
            }
        }

        add(rightPanel, BorderLayout.EAST);
    }

    // 목숨 아이템 구매 처리
    private void purchaseLifeItem(JLabel pointsLabel) {
        int lifeItemPrice = getItemPriceByName("목숨 아이템");
        boolean success = storeRepository.purchaseItem(currentUser.getUsername(), getItemIdByName("목숨 아이템"));
        if (success) {
            JOptionPane.showMessageDialog(this, "목숨 아이템을 구매했습니다!");
            currentUser.setPoints(currentUser.getPoints() - lifeItemPrice);
            pointsLabel.setText("포인트: " + currentUser.getPoints());
        } else {
            JOptionPane.showMessageDialog(this, "구매에 실패했습니다. 포인트를 확인하세요.");
        }
    }

    // 시간 추가 아이템 구매 처리
    private void purchaseTimeBoostItem(JLabel pointsLabel) {
        int timeBoostItemPrice = getItemPriceByName("시간 추가 아이템");
        boolean success = storeRepository.purchaseItem(currentUser.getUsername(), getItemIdByName("시간 추가 아이템"));
        if (success) {
            JOptionPane.showMessageDialog(this, "시간 추가 아이템을 구매했습니다!");
            currentUser.setPoints(currentUser.getPoints() - timeBoostItemPrice);
            pointsLabel.setText("포인트: " + currentUser.getPoints());
        } else {
            JOptionPane.showMessageDialog(this, "구매에 실패했습니다. 포인트를 확인하세요.");
        }
    }

    // 액세서리 아이템 구매 처리
    private void purchaseAccessoryItem(Item item, JLabel pointsLabel, JLabel characterImageLabel) {
        boolean success = storeRepository.purchaseItem(currentUser.getUsername(), item.getItemId());
        if (success) {
            JOptionPane.showMessageDialog(this, item.getName() + "를 구매했습니다!");
            currentUser.setPoints(currentUser.getPoints() - item.getPrice());
            pointsLabel.setText("포인트: " + currentUser.getPoints());
            characterImageLabel.setIcon(new ImageIcon(storeRepository.getUpdatedCharacterImage(currentUser.getUsername())));
        } else {
            JOptionPane.showMessageDialog(this, "구매에 실패했습니다. 포인트를 확인하거나 이미 구매한 아이템인지 확인하세요.");
        }
    }

    // 아이템 ID 가져오기 (간단한 매핑 메서드)
    private int getItemIdByName(String itemName) {
        return storeRepository.getItems().stream()
                .filter(item -> item.getName().equals(itemName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템을 찾을 수 없습니다."))
                .getItemId();
    }

    // 아이템 가격 가져오기 (간단한 매핑 메서드)
    private int getItemPriceByName(String itemName) {
        return storeRepository.getItems().stream()
                .filter(item -> item.getName().equals(itemName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 아이템을 찾을 수 없습니다."))
                .getPrice();
    }
}
