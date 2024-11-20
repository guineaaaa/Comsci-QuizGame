package model;

// DB와 매핑되는 User 객체
public class User {
	private String username;
	private String password;
	
	//생성자
	public User(String username, String password) {
		this.username=username;
		this.password=password;
	}
	
	// Getter, Setter
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username=username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password=password;
	}
}
