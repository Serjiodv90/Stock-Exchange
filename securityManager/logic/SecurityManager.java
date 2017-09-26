package securityManager.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import securityManager.storage.*;

import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import bank.api.NotEnoughAssetException;
import exchange.api.DoesNotHaveThisStockException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import exchange.api.NotEnoughMoneyException;
import exchange.api.NotEnoughStockException;
import exchange.api.StockNotTradedException;

public class SecurityManager {

	private Security security;
	private Order order;
	private StorageManager store;
	private final String BANKIP = "rmi://13.59.120.241/Bank";
	private final String STOCKMARKETIP = "rmi://13.59.120.241/Exchange";

	/*
	private final String STOCKTYPE = "S";
	private final String BONDTYPE = "B";
	 */
	BankManager bank;
	ExchangeManager exchange;

	private LinkedHashMap<Integer, Order> openOrders = new LinkedHashMap<>();
	private LinkedHashMap<String, Security> ownedSecurities = new LinkedHashMap<>();

	//	public SecurityManager(String securityName, int amount, double price, int orderID, String type, int ) {
	//
	//		if (STOCKTYPE.equals(type))
	//			security = new Stock(amount, price, securityName);
	//		else
	//			security = new Bond(amount, price, securityName);
	//	}

	//getting Security and Order objects from the securityManager.iface
	public SecurityManager () throws IOException, NotBoundException, WrongSecretException, InternalServerErrorException{
		this.bank = (BankManager) Naming.lookup(BANKIP);
		this.exchange = (ExchangeManager) Naming.lookup(STOCKMARKETIP);
		this.store = new StorageManager();
		if(!store.isFileEmpty())
			this.load();
	}

	public SecurityManager (Security security, Order order) throws FileNotFoundException {
		this.addSecurity(security);
		this.addOrder(order);

	}

	public void addSecurity(Security security) {
		this.security = security;
		ownedSecurities.put(this.security.getSecurityName(), this.security);
	}

	public void addOrder (Order order) throws FileNotFoundException {
		this.order = order;

		openOrders.put(this.order.getOrderID(), this.order);
		this.save();

	}

	public Security findSecurity (String secName) {
		return ownedSecurities.get(secName);
	}

	public Order findOrder (Integer orderID) {
		return openOrders.get(orderID);
	}

	/*removes security from the owned list, by security name (asset name)*/
	public boolean removeSecurity (String secName) {
		if(ownedSecurities.remove(secName) != null)
			return true;
		return false;
	}

	/*remove order from order's list, by and orderID*/
	public boolean removeOrder (Integer orderID) throws FileNotFoundException {
		if(openOrders.remove(orderID) != null) 
			return true;

		return false;
	}

	public List<Security> getOwnedSecurities () {
		List <Security> list = new ArrayList<>();
		Iterator<Entry<String, Security>> it = ownedSecurities.entrySet().iterator();

		while(it.hasNext())
			list.add((Security)it.next().getValue());

		return list;

	}

	public List<Order> getOpenOrders () {
		List <Order> list = new ArrayList<>();
		Iterator<Entry<Integer, Order>> it = openOrders.entrySet().iterator();

		while(it.hasNext())
			list.add((Order)it.next().getValue());

		return list;
	}

	public void printOwnedSecurities () {
		Iterator<Entry<String, Security>> it = ownedSecurities.entrySet().iterator();
		Entry<String, Security> en;
		while(it.hasNext()) {
			en = it.next();
			System.out.println(en.getValue());
		}
	}

	public void printOpenOrders () {
		Iterator<Entry<Integer, Order>> it = openOrders.entrySet().iterator();
		Entry<Integer, Order> en;
		while(it.hasNext()) {
			en = it.next();
			System.out.println(en.getValue());
		}

	}

	public void showSupplyAndDemand () throws RemoteException {

		for(String stockName : exchange.getStockNames()) {

			System.out.println("\nThe supply of the stock : " +stockName);
			System.out.println("Price\tAmount");

			for (Map.Entry<Integer, Integer> entry : exchange.getSupply(stockName).entrySet()) 
				System.out.println(entry.getKey() +"\t"+ entry.getValue());

			System.out.println("\nThe demand of the stock : " +stockName);
			System.out.println("Price\tAmount");

			for (Map.Entry<Integer, Integer> entry : exchange.getDemand(stockName).entrySet())
				System.out.println(entry.getKey() +"\t"+ entry.getValue());

		}

	}

	//placing new bid on the stock market, purchasing a stock
	public Order placeBid (String secret, int accountId, String stockName, int amount, int price ) throws InterruptedException, RemoteException, DoesNotHaveThisAssetException, WrongSecretException, InternalServerErrorException, NotEnoughStockException, InternalExchangeErrorException, NotEnoughMoneyException, FileNotFoundException {
		Order tmpOrder = null;
		try {
			bank.transferAssets(secret, accountId, 3373, "NIS", price);

			/*add here the connection to the log of the transfer*/

			//waiting till the transaction made
			Thread.sleep(1000);

			int orderId = exchange.placeBid(secret, accountId, stockName, amount, price);
			Security tmpSecurity = new Security(amount, amount*price, stockName, 'S');
			 tmpOrder = new Order(3373, tmpSecurity, orderId, 'B');

			addOrder(tmpOrder);
			return tmpOrder;

			/*add here the connection to the log of the bid that been placed*/
		}catch(NotEnoughAssetException e) {
			System.out.println("You don't have enough money for the purchase, you need: "
					+(e.getRequestedAmount() - e.getCurrentAmount())+ " more");
		}catch (NoSuchAccountException e) {
			System.out.println("The destination account number: "+e.getBankAccountId()+", doesn't exists");
		}catch (StockNotTradedException e) {
			System.out.println("The stock: " +e.getStockName()+ "isn't traded");
		}

		return tmpOrder;
	}

	public Order placeAsk (String secret, int accountId, String stockName, int amount, int price) throws RemoteException, DoesNotHaveThisAssetException, WrongSecretException, InternalServerErrorException, InterruptedException, NoSuchAccountException, NotEnoughStockException, StockNotTradedException, InternalExchangeErrorException, FileNotFoundException {

		Order tmpOrder = null;
		try {
			bank.transferAssets(secret, accountId, 3373, stockName, amount);

			//add here the connection to the log of the selling stock

			Thread.sleep(1000);

			int orderId = exchange.placeAsk(secret, accountId, stockName, amount, price);
			Security tmpSecurity = new Security(amount, amount*price, stockName, 'S');
			 tmpOrder = new Order(3373, tmpSecurity, orderId, 'A');

			addOrder(tmpOrder);
			return tmpOrder;

			/*add here the connection to the log of the ask that been placed*/
		}catch (NotEnoughAssetException e) {
			System.out.println("You don't own enough stocks to sell this amount, "
					+ "you need " +(e.getRequestedAmount() - e.getCurrentAmount())+ " more");
		}catch (DoesNotHaveThisStockException e) {
			System.out.println("You don't own this stock: " +e.getAssetName());
		}catch (DoesNotHaveThisAssetException e) {
			System.out.println("You don't own this stock: " +e.getAsset());
		}
		
		
			return tmpOrder;
		
	}

	public List<Order> checkOpenOrdersStatus (String secret, int accountId) throws RemoteException, NoSuchAccountException, WrongSecretException, InternalExchangeErrorException, FileNotFoundException {
		List<Integer> openOrderId = exchange.getOpenOrders(secret, accountId);
		Iterator<Integer> it = openOrders.keySet().iterator();
		List <Order> tmpLst = new LinkedList<>();

		while(it.hasNext()) {
			Integer id = it.next();
			if(!openOrderId.contains(id)) {
				tmpLst.add(openOrders.get(id));

				it.remove();
				//	removeOrder(id);
			}
		}
		this.save();
		return tmpLst;
	}

	public void save () throws FileNotFoundException {
		List <OrderInfo> lst = new LinkedList<>();
		Iterator<?> it = openOrders.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>)it.next();
			Order tmpOrder = (Order)entry.getValue();
			lst.add(new OrderInfo(tmpOrder.getOrderID(),
					tmpOrder.getSecurityType().getSecurityName(),
					tmpOrder.getActionDate(),
					tmpOrder.getDestination(),
					tmpOrder.getType(),
					tmpOrder.getSecurityType().getAmount(),
					tmpOrder.getPrice(),
					tmpOrder.getSecurityType().getSecurtyType()));
		}

		store.saveOpenOders(lst);
	}

	public void load () throws FileNotFoundException {
		List <OrderInfo> lst = new LinkedList<>();
		try {
			lst = store.loadOrders();

			for(OrderInfo info : lst) {
				Security sec = new Security(info.getAmount(), info.getAmount()*info.getStockPrice(), info.getStockName(), info.getSecurityType());
				Order order = new Order(info.getDestination(), sec, info.getOrderId(), info.getOrderType());
				openOrders.put(info.getOrderId(), order);
			}
		}catch (FileNotFoundException e) {
			return;
		}
	}

















}
