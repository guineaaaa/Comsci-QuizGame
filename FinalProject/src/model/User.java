package model;

// DB와 매핑되는 User 객체
public class User {
    private String username;
    private String password;
    private int points; // 포인트 추가
    private String nickname;

    // 생성자
    public User(String username, String password,String nickname, int points) {
        this.username = username; //id
        this.nickname=nickname;
        this.password = password;
        this.points = points; // 포인트 초기화
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
}
