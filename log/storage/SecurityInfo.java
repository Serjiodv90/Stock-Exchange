package log.storage;

import java.io.Serializable;
import java.util.Date;


public class SecurityInfo implements Serializable{

		
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String stockName;
		private int amount;
		private int pricePerUnit;
		private int orderId;
		private int statusOfOrder;
		private Date dateOfLastChange;
		public SecurityInfo(String stockName,int amount,int price, int orderId,int status,Date date)
		{
			this.stockName=stockName;
			this.amount=amount;
			this.pricePerUnit=price;
			this.dateOfLastChange=date;
			this.orderId=orderId;
			this.statusOfOrder=status;
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

}
