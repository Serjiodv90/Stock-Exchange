package portfolioManager.iface;

import java.io.IOException;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;
import portfolioManager.logic.Portfolio;
import MainApp.Account;


public class portfolioInterface {
	ArrayList<Object> a=new ArrayList<>();
	Portfolio portfolio;
//	private ExchangeManager exchange;
//	private BankManager bank;
//	Set<String> assets = new TreeSet<>();
	private BankManager bank;
	ExchangeManager exchange;
	private Account acc;
	
	public portfolioInterface(Account acc) throws MalformedURLException, RemoteException, NotBoundException {
		this.acc=acc;
		bank = (BankManager) Naming.lookup("rmi://13.59.120.241/Bank");
		exchange = (ExchangeManager) Naming.lookup("rmi://13.59.120.241/Exchange");
		portfolio = new Portfolio();
	}
	
	public void calculateProfits() {
		try {
			portfolio.getFromLog(a);
			portfolio.allProfits();
			portfolio.writeAllSecurites();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void loadFromStorage() {
		try {
			portfolio.readAllSecurities();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	//
	public void getLogData(ArrayList<Object> arr) {
		this.a=arr;
	}
	
	public void viewPortfolio()
	{
		ArrayList<String> toStr=new ArrayList<String>();
		Set<String> assets;
		try {
			assets=bank.getAssets(acc.getSecret(), acc.getId());
			
			for(String st:assets)
			{
				toStr.add(st);
				toStr.add(bank.getQuantityOfAsset(acc.getSecret(), acc.getId(), st)+"");
				toStr.add(getAvgPrice(st)+"");
			}
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WrongSecretException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalServerErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DoesNotHaveThisAssetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Owned assets:");
		for(int i=0;i<toStr.size();i+=3)
		{
			System.out.printf("Asset Name:%s.\tAsset amount:%s.\tAsset avg price on exchange:%s.\t",toStr.get(i),toStr.get(i+1),toStr.get(i+2));
			if(Double.parseDouble(toStr.get(i+2))==0)
				System.out.print("<---- Not available on stock market");
				
			System.out.println();
		}
	}
	public double getAvgPrice(String AssetName)
	{
		try {
			Map<Integer,Integer> supply=exchange.getSupply(AssetName);
			Set<Entry<Integer,Integer>> s=supply.entrySet();
			int min=2147483647,offset=777,i=0,AmountToCheck=10;
			for(Entry<Integer,Integer> e:s)
			{
				if(e.getKey()<min)
				min=e.getKey();
			}
			long totalCost=0,totalAmount=0;
			for(Entry<Integer,Integer> e:s)
			{
				if(e.getKey()<min+offset){
				totalAmount+=e.getValue();
				totalCost+=(e.getValue()*e.getKey());}
				if(i>AmountToCheck)
					break;
				i++;
			}
			if(totalAmount==0)
				return 0;
			return totalCost/totalAmount;
				
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}
	
	

	
	

}
