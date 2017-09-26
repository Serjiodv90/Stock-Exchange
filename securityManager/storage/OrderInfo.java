package securityManager.storage;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class OrderInfo {
	
	private int orderId;
	private String stockName;
	private Date actionDate;
	private int destination;
	private char orderType;
	
	private int amount;
	private int stockPrice;
	private char securityType;
	
	public OrderInfo (int orderId, String stockName, Date actionDate, int destination, char orderType, int amount, int stockPrice, char securityType) {
		this.orderId = orderId;
		this.stockName = stockName;
		this.amount = amount;
		this.stockPrice = stockPrice;
		this.securityType = securityType;
		this.orderType = orderType;
		this.actionDate = actionDate;
		
		//in case that the order is "ask" then there is a destination else dest. = 0;
		if(this.orderType == 'A')
			this.destination = destination;
		else this.destination =0;
	}
	
	public int getOrderId() {
		return orderId;
	}

	public String getStockName() {
		return stockName;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public int getDestination() {
		return destination;
	}

	public char getOrderType() {
		return orderType;
	}

	public int getAmount() {
		return amount;
	}

	public int getStockPrice() {
		return stockPrice;
	}

	public char getSecurityType() {
		return securityType;
	}

	public OrderInfo (Scanner s) {
		//s.useDelimiter(",");
		
		this.stockName = s.next();
	//	System.out.println("stockname: "+stockName);
		
		this.orderId = Integer.valueOf(s.next());
		this.orderType = s.next().charAt(0);
		this.destination = Integer.valueOf(s.next());
		this.amount = Integer.valueOf(s.next());
		this.stockPrice = Integer.valueOf(s.next());
		this.securityType = s.next().charAt(0);
		this.actionDate = new Date();
		/*
		try {
			s.nextLine();
			String tmp = s.nextLine();
			System.out.println("Date:" + tmp);
			SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
			this.actionDate = (Date)df.parse(tmp);
		//	this.actionDate = new SimpleDateFormat().parse(s.next());
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		
	}
	
	public void writeOrders (PrintWriter pw) {
		
		pw.println(this.stockName
				+" "+ String.valueOf(this.orderId)
				+" "+ String.valueOf(this.orderType)
				+" "+ String.valueOf(this.destination)
				+" "+ String.valueOf(this.amount)
				+" "+ String.valueOf(this.stockPrice)
				+" "+ String.valueOf(this.securityType)
				//+"\n"+ this.actionDate
				);
	}

}
