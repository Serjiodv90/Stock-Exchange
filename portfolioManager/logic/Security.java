package portfolioManager.logic;

public class Security implements Comparable<Security> {
	private String name;
	private String status;
	private int price, amount, orderId;
	private int profit;

	public Security(int orderId, String name, String status, int price) {
		this.orderId = orderId;
		setName(name);
		setPrice(price);
		setStatus(status);
	}
	
	public Security(int orderId, String name, String status, int price, int amount) {
		this.orderId = orderId;
		setName(name);
		setPrice(price);
		setStatus(status);
		this.amount = amount;
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

	public String getStatus() {
		return status;
	}

	public int getPrice() {
		return price;
	}
	
	public int getAmount() {
		return amount;
	}

	public int getOrderId() {
		return orderId;
	}

	@Override
	public int compareTo(Security s) {
		return this.getName().compareTo(s.getName());
	}

	public void setProfit(int profit) {
		this.profit = profit;
	}
	
	public int getProfit() {
		return profit;
	}
	
	public int getTotalPrice() {
		return (this.amount*this.price);
	}
	
	
}
