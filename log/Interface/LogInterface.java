package log.Interface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

import log.logic.*;
//3373
public class LogInterface {
	private LogManager Logic;
	enum Status { Buy, Sell, Pending, Error }
	private Date portfolioChanges;
	public LogInterface() throws IOException, ClassNotFoundException
	{
		Logic=new LogManager();
		portfolioChanges=new Date();
	}
/*	public double getStockAvgPrice(String stockName) {
		TreeSet<Security> ts=Logic.getData();
		double[] countPrice=new double[2];
		for(Security sc:ts)
		{
			if(sc.getStockName().equals(stockName))
				{
				countPrice[0]+=(sc.getAmount()*sc.getPricePerUnit());
				countPrice[1]++;
				}
		}
		return countPrice[0]/countPrice[1];
			
	}
*/
	
	public void printSecuritiesLog()
	{	TreeSet<Security> ts=Logic.getData();
	 Status[] s=Status.values();
		for(Security sc:ts)
		{	
			System.out.printf("Order id:%d ,StockName:%s,Amount:%d ,Price per unit:%d,Status:%s",sc.getOrderId(),sc.getStockName(),
					sc.getAmount(),sc.getPricePerUnit(),s[sc.getStatusOfOrder()].toString());
			System.out.println();
		}
	}
	public void addSecurityToLog(String stockName,int amount,int price, int orderId,int status,Date date)
	{
		try {
			Logic.addSecurityToLog(stockName,amount,price,orderId,status,date);
		} catch (IOException e) {
			System.out.println("IO");
		}
	}
	public void save() throws ClassNotFoundException, IOException
	{
		Logic.saveAll();
	}

	public ArrayList<Object> moveToPortfolio()
	{
		TreeSet<Security> ts=Logic.getData();
		ArrayList<Object> dataToPortfolio=new ArrayList<>();
		for(Security sc:ts)
		{
			if(sc.getDateOfLastChange().compareTo(portfolioChanges)<0)
				break;
			else
				if(sc.getStatusOfOrder()==0)
				{
					dataToPortfolio.add(sc.getOrderId());
					dataToPortfolio.add(sc.getStockName());
					dataToPortfolio.add("Buy");
					dataToPortfolio.add(sc.getAmount());
					dataToPortfolio.add(sc.getPricePerUnit());
				}
				else
					if(sc.getStatusOfOrder()==1)
					{
						dataToPortfolio.add(sc.getOrderId());
						dataToPortfolio.add(sc.getStockName());
						dataToPortfolio.add("Sell");
						dataToPortfolio.add(sc.getAmount());
						dataToPortfolio.add(sc.getPricePerUnit());
					}
			this.portfolioChanges=new Date();
		}
		return dataToPortfolio;
	}

}
