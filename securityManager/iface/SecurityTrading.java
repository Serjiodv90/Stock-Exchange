package securityManager.iface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import auth.api.WrongSecretException;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import exchange.api.NotEnoughMoneyException;
import exchange.api.NotEnoughStockException;
import exchange.api.StockNotTradedException;
import securityManager.logic.Order;
import securityManager.logic.Security;
import securityManager.logic.SecurityManager;
import log.Interface.*;


public class SecurityTrading {

	private  int ACCOUNTID = 204;
	private  String SECRET = "txgPzK";
	private  LogInterface logUpdate;
	private SecurityManager sec;
	private Scanner s = new Scanner(System.in);
/*
//	//at final stage delete the main and all the static definitions!!!!!
//	public static void main(String[] args) throws IOException, NotBoundException, WrongSecretException, InternalServerErrorException, InterruptedException, DoesNotHaveThisAssetException, NotEnoughStockException, InternalExchangeErrorException, NotEnoughMoneyException, NoSuchAccountException, StockNotTradedException, ClassNotFoundException {
//		// TODO Auto-generated method stub
//		Security sec1 = new Security(2, 500, "IBM", 'S');
//		Security sec2 = new Security(15, 200, "CISCO", 'S');
//
//		Order or1 = new Order(12312313, sec1, 1, 'B');
//		Order or2 = new Order(99999999, sec2, 2, 'B');
//
//		SecurityManager sec = new SecurityManager();
//		//		sec.addSecurity(sec2);
//		//		sec.addSecurity(sec1);
//
//		sec.addOrder(or1);
//		sec.addOrder(or2);
//		
//		start();
//
//		//		List<Order> lst = sec.getPreviousOrders();
//		//		for (Order order : lst) {
//		//			
//		//			System.out.println(order);
//		//		}
//
////		sec.printOpenOrders();
////
////		System.out.println("Demand & supply :");
////
////		try {
////			sec.showSupplyAndDemand();
////		} catch (RemoteException e) {
////
////			e.printStackTrace();
////		}
////
////		sec.save();
////		sec.load();
//
//	}*/
	
	
	
	public SecurityTrading(int id,String password,LogInterface log) throws IOException, NotBoundException, WrongSecretException, InternalServerErrorException
	{
		this.ACCOUNTID=id;
		this.SECRET=password;
		this.logUpdate=log;
		this.sec=  new SecurityManager();
	}

/*	public  void start(LogInterface log) throws IOException, NotBoundException, WrongSecretException, InternalServerErrorException, InterruptedException, DoesNotHaveThisAssetException, NotEnoughStockException, InternalExchangeErrorException, NotEnoughMoneyException, NoSuchAccountException, StockNotTradedException, ClassNotFoundException {

		int option;
		SecurityManager sec = new SecurityManager();
		logUpdate = log;

		do {
			System.out.println("\n#################################");
			System.out.println("Show supply & demand, press 1");
			System.out.println("Place bid (buy stock), press 2");
			System.out.println("Place ask (sell stock), press 3");
			System.out.println("Show pending orders, press 4");
			System.out.println("Return back, press 0");
			System.out.println("###################################");
			option = s.nextInt();

			switch(option) {
			case 1:
				try {
					sec.showSupplyAndDemand();
				}catch (RemoteException e) {
					System.out.println("Connection failed, try again later");
					System.out.println("Details: " +e.getMessage());
				}
				break;

			case 2:
				System.out.println("Please enter the stock's name:");
				String stockNameBid = s.next();
				System.out.println("Please enter the amount of stocks you would like to purchase:");
				int amountBid = s.nextInt();
				System.out.println("Please enter your price for the stock:");
				int priceBid = s.nextInt();
				Order tmpOrderBid = sec.placeBid(SECRET, ACCOUNTID, stockNameBid, amountBid, priceBid);	
				if(tmpOrderBid != null)
					updateLogMidStatus(tmpOrderBid);
				updateLogFinalStatus(sec);
				break;

			case 3:
				System.out.println("Please enter the stock's name:");
				String stockNameAsk = s.next();
				System.out.println("Please enter the amount of stocks you would like to sell:");
				int amountAsk = s.nextInt();
				System.out.println("Please enter your price for the stock:");
				int priceAsk = s.nextInt();
				Order tmpOrderAsk = sec.placeAsk(SECRET, ACCOUNTID, stockNameAsk, amountAsk, priceAsk);
				if(tmpOrderAsk != null)
					updateLogMidStatus(tmpOrderAsk);
				updateLogFinalStatus(sec);
				break;

			case 4:
				sec.printOpenOrders();
				break;

			case 0:
				return;

			default:
				System.out.println("Invalid choise, try again!!");

			}
		}while(option != 0);
	}
	*/
	public  void updateLogFinalStatus (SecurityManager sec) throws NoSuchAccountException, WrongSecretException, InternalExchangeErrorException, ClassNotFoundException, IOException {
		List <Order> lst = new LinkedList<>();
		lst = sec.checkOpenOrdersStatus(SECRET, ACCOUNTID);
		Iterator<Order> it = lst.iterator();
		 
		int status;
		
		while (it.hasNext()) {
			Order order = it.next();
			if(order.getType() == 'A' )
				  status = 1;
			else status = 0;
			logUpdate.addSecurityToLog(order.getSecurityType().getSecurityName(), order.getSecurityType().getAmount(), order.getPrice(), order.getOrderID(), status, order.getActionDate());
			
			
		}
	}
	
	public  void updateLogMidStatus (Order order) throws ClassNotFoundException, IOException {
		 
		int status = 2;
		logUpdate.addSecurityToLog(order.getSecurityType().getSecurityName(), order.getSecurityType().getAmount(), order.getPrice(), order.getOrderID(), status, order.getActionDate());
	}
	public void setAccDetals(int accid,String pass)
	{
		this.ACCOUNTID=accid;
		this.SECRET=pass;
	}
	
	public void showSupplyDemand() throws RemoteException
	{
		sec.showSupplyAndDemand();
	}
	public void placeBid() throws InterruptedException, DoesNotHaveThisAssetException, WrongSecretException, InternalServerErrorException, NotEnoughStockException, InternalExchangeErrorException, NotEnoughMoneyException, ClassNotFoundException, IOException, NoSuchAccountException
	{
		System.out.println("Please enter the stock's name to buy:");
		String stockNameBid = s.next();
		System.out.println("Please enter the amount of stocks you would like to purchase:");
		int amountBid = s.nextInt();
		System.out.println("Please enter your price for the stock:");
		int priceBid = s.nextInt();
		Order tmpOrderBid = sec.placeBid(SECRET, ACCOUNTID, stockNameBid, amountBid, priceBid);	
		if(tmpOrderBid != null)
			updateLogMidStatus(tmpOrderBid);
		updateLogFinalStatus(sec);
	}
	
	public void placeAsk() throws InterruptedException, DoesNotHaveThisAssetException, WrongSecretException, InternalServerErrorException, NotEnoughStockException, InternalExchangeErrorException, NotEnoughMoneyException, ClassNotFoundException, IOException, NoSuchAccountException, StockNotTradedException
	{
		System.out.println("Please enter the stock's name to sell:");
		String stockNameAsk = s.next();
		System.out.println("Please enter the amount of stocks you would like to sell:");
		int amountAsk = s.nextInt();
		System.out.println("Please enter your price for the stock:");
		int priceAsk = s.nextInt();
		Order tmpOrderAsk = sec.placeAsk(SECRET, ACCOUNTID, stockNameAsk, amountAsk, priceAsk);
		if(tmpOrderAsk != null)
			updateLogMidStatus(tmpOrderAsk);
		updateLogFinalStatus(sec);
	}
	
	public void showPending()
	{
		sec.printOpenOrders();
	}
	
	
}
