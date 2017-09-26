package portfolioManager.storage;

public class SecurityInfo {

	private String name, status;
	private int price, orderId;
	
	public SecurityInfo(int id, String name, String status, int price) {
		this.orderId = id;
		setName(name);
		setStatus(status);
		setPrice(price);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}
	
	public int getOrderId() {
		return orderId;
	}
	
	public String getStatus() {
		return status;
	}
	
	
}
