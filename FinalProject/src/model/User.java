package model;

// DB와 매핑되는 User 객체
/**
 * 데이터를 캡슐화, 데이터와 프로그램 로직을 분리한다.
 */
public class User {
    private String username;
    private String password;
    private String nickname;
    private int points;
    private String characterImage;
    private int timeBoostItem;
    private int lifeItem;

    public User(String username, String password, String nickname, int points, String characterImage, int timeBoostItem, int lifeItem) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.points = points;
        this.characterImage = characterImage;
        this.timeBoostItem = timeBoostItem;
        this.lifeItem = lifeItem;
    }

    // Getter, Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // 포인트 getter, setter 추가
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getCharacterImage() {
        return characterImage;
    }

    public void setCharacterImage(String characterImage) {
        this.characterImage = characterImage;
    }
    
    public int getTimeBoostItem() {
        return timeBoostItem;
    }
    
    public void setTimeBoostItem(int timeBoostItem) {
    	this.timeBoostItem=timeBoostItem;
    }

    public int getLifeItem() {
        return lifeItem;
    }
    
    public void setLifeItem(int lifeItem) {
    	this.lifeItem=lifeItem;
    }
}
