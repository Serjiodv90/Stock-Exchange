package securityManager.logic;
/*
 * secret - txgPzk
 * accountID - 204
*/


import java.util.Date;
import java.util.InputMismatchException;



public class Security {
	
//	private int securityID;
	private final char STOCK = 'S';
	private final char BOND = 'B';
	
	private String securityName;
	private int amount;
	private int paidPrice;
	private Date purchaseDate;
	private char securityType;
	
	public Security() { }
	
	public Security (int amount, int paidPrice, String secName, char securityType)throws InputMismatchException {
	//	this.securityID = secId;
		this.securityName = secName;
		setAmount(amount);
		setPaidPrice(paidPrice);
		setDate();
		setType(securityType);
	}
	
	public void setName (String name) {
		this.securityName = name;
	}
	
	public void setAmount (int amount) throws InputMismatchException {
		if(amount > 0)
			this.amount = amount;
		else if(amount == 0)
			throw new InputMismatchException("You can't requested for 0 securities quantity");
		else throw new InputMismatchException("You can't requested for negative number of securities");
	}
	
	public void setPaidPrice(int price)throws InputMismatchException{
		if(price > 0)
			this.paidPrice = price;
		else throw new InputMismatchException("The paid price intput must be possitive and bigger than 0");
		
	}
	
	private void setDate () {
		purchaseDate = (new Date());
	}
	
//	public int getId () { 
//		return this.securityID;
//	}
	
	private void setType (char type) {
		if (Character.toLowerCase(type) == 's')
			this.securityType = STOCK;
		else this.securityType = BOND;
	}
	
	public char getSecurtyType () {
		return this.securityType;
	}
	
	public int getAmount () { 
		return this.amount; 
	}
	
	public int getPaidPrice () {
		return this.paidPrice;
	}
	
	public Date getPurcaseDate () {
		return this.purchaseDate;
	}
	
	public String getSecurityName () {
		return this.securityName;
	}
	
	@Override
	public String toString() {
		return "Security details:"
				+ "\nName: " +getSecurityName()
				+ "\nAmount: " +getAmount()
				+ "\nPaid price: " +getPaidPrice()
		//		+ "\nPurchase date: " +getPurcaseDate()
				+ "\nType: " +(getSecurtyType() == STOCK ? "stock" : "bond") ;
	}
	
	
	
	
	

}
