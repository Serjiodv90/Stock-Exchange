package securityManager.logic;

import java.util.Date;

public class Order implements Comparable<Order> {
	
	private enum securityState {
		SOLD,
		WAITINGFORSELLINGMATCH,
		BOUGHT,
		WAITINGFORBUYINGMATCH
	}
	
	private final char ASK = 'A';
	private final char BID = 'B';
	
	private Date actionDate;
	private Security securityType;
	private int destination;
	private int secState;
	private int orderID;
	private char orderType;
	
	public Order () {}
	
	public Order (int destination, Security secType, int orderID, char orderType) {
		
		this.setActionDate();
		this.setSecurityType(secType);
//		this.setPrice(secType.getPaidPrice());
//		this.setSecurityAmount(secType.getAmount());
//		this.setSecurityName(secType.getSecurityName());
		this.setDestination(destination);
		this.setOrderID(orderID);
		this.setType(orderType);
		
		
		if(this.orderType == ASK)
			setState("WAITINGFORBUYINGMATCH");
		else setState("WAITINGFORBUYINGMATCH");
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

//	public String getSecurityName() {
//		return securityType.getSecurityName();
//	}
//
//	public void setSecurityName(String securityName) {
//		this.securityType.setName(securityName);
//	}
//
//	public int getSecurityAmount() {
//		return securityType.getAmount();
//	}
//
//	public void setSecurityAmount(int securityAmount) {
//		this.securityType.setAmount(securityAmount);
//	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate() {
		this.actionDate = new Date();
	}

	public int getPrice() {
		return this.securityType.getPaidPrice();
	}

	public void setPrice(int price) {
		this.securityType.setPaidPrice(price);
		
	}
	
	public void setSecurityType(Security secType) {
		this.securityType = secType;
	}
	
	public Security getSecurityType () {
		return this.securityType;
	}
	
	public void setDestination (int dest) {
		this.destination = dest;
	}
	
	public int getDestination () {
		return this.destination;
	}
	
	public void setState (String state) {
		this.secState = securityState.valueOf(state).ordinal();
	}
	
	public String getState () {
		return securityState.values()[this.secState].name();
	}
	
	public void setType (char orderType) {
		if(Character.toLowerCase(orderType) == 'a')
		this.orderType = ASK;
		else this.orderType = BID;
	}
	
	public char getType() {
		return this.orderType;
	}
	
	@Override
	public String toString () {
	 String str = "Order details:"
				+ "\nID: " +getOrderID()
				+ "\nDestination acount: " +getDestination()
				+ "\nType: " +(getType() == ASK ? "ask" : "bid") 
				+ "\naction date: " +getActionDate()
				+ "\nState: ";
	 			
	 switch (this.secState) {
	case 0:
		str += "sold\n";		
		break;
	case 1:
		str += "waiting for sell match\n";
		break;
	case 2:
		str += "bought\n";
		break;
	case 3:
		str += "partial match\n";
		break;
	case 4:
		str += "waiting for buying match\n";
		break;
	}
	 
	 str += this.securityType.toString();
	 str += "\n-------------------------------------------";
	 return str;
	}

	@Override
	public int compareTo(Order o) {
		
		return getActionDate().compareTo(o.getActionDate());
	}
	
	 
	
	

}
