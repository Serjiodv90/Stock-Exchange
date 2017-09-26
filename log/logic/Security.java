package log.logic;

import java.io.Serializable;
import java.util.Date;

public class Security implements Comparable<Security>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 989158818638139041L;
	private String stockName;
	private int amount;
	private int pricePerUnit;
	private int orderId;
	private int statusOfOrder;
	private Date dateOfLastChange;
	public Security(String stockName, int amount, int price, int orderId, int status, Date date) {
		this.stockName = stockName;
		this.amount = amount;
		this.pricePerUnit = price;
		this.dateOfLastChange = date;
		this.orderId = orderId;
		this.statusOfOrder = status;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getStatusOfOrder() {
		return statusOfOrder;
	}

	public Date getDateOfLastChange() {
		return dateOfLastChange;
	}

	public String getStockName() {
		return stockName;
	}

	public int getAmount() {
		return amount;
	}

	public int getPricePerUnit() {
		return pricePerUnit;
	}

	@Override
	public int compareTo(Security o) {
		if (this.orderId == o.orderId && this.dateOfLastChange.equals(o.dateOfLastChange))
			return 0;
	/*	if (this.orderId == o.orderId && this.dateOfLastChange.compareTo(o.dateOfLastChange) < 0) {
			this.stockName = o.getStockName();
			this.amount = o.getAmount();
			this.pricePerUnit = o.getPricePerUnit();
			this.orderId = o.getOrderId();
			this.statusOfOrder = o.getStatusOfOrder();
			this.dateOfLastChange = o.getDateOfLastChange();
		}*/
		return this.dateOfLastChange.compareTo(o.dateOfLastChange);

	}

}
