package MainApp;

public class Account {
	private int id;
	private String secret;

	public Account(String secret, int id) {
		this.secret=secret;
		this.id=id;
	}

	public int getId() {
		return id;
	}

	public String getSecret() {
		return secret;
	}
}
