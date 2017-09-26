package portfolioManager.logic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;
import portfolioManager.storage.PortfolioStorage;
import portfolioManager.storage.SecurityInfo;

import log.logic.LogManager;

public class Portfolio {
//	private Set<String> assets;
	private Set<Security> securitiesFromFile, securitiesFromLog, securityProfits;
//	private ExchangeManager exchange;
//	private BankManager bank;
	private PortfolioStorage storage;
	private ArrayList<Object> dataFromLog;
	private String profit = "Profits", sell = "Sell", buy = "Buy";

	public Portfolio() throws MalformedURLException, RemoteException, NotBoundException{
	//	bank = (BankManager) Naming.lookup("rmi://172.20.18.151/Bank");
	//	exchange = (ExchangeManager) Naming.lookup("rmi://172.20.18.151/Exchange");
		securitiesFromFile = new TreeSet<Security>();
	}

/*	public Set<String> ownedAssets() {
		try {
			assets = bank.getAssets("yxgPzK", 204);

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (WrongSecretException e) {
			e.printStackTrace();
		} catch (InternalServerErrorException e) {
			e.printStackTrace();
		}
		return assets;
	}

	public void getQuantity() {
		Map<String, Integer> assetAmounts = new HashMap<String, Integer>(); 
		for(String asset : assets) {
			try {
				amount = bank.getQuantityOfAsset("yxgPzK", 204, asset);
				assetAmounts.put(asset, amount);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (WrongSecretException e) {
				e.printStackTrace();
			} catch (DoesNotHaveThisAssetException e) {
				e.printStackTrace();
			} catch (InternalServerErrorException e) {
				e.printStackTrace();
			}
		}
	}*/

	public void readAllSecurities() throws ClassNotFoundException, IOException {
		ArrayList<SecurityInfo> temp = new ArrayList<>();
		temp = storage.read();
		for(SecurityInfo info : temp) {
			securitiesFromFile.add(new Security(info.getOrderId(), info.getName(), info.getStatus(), info.getPrice()));
		}
	}

	public void writeAllSecurites() throws IOException {
		ArrayList<SecurityInfo> temp = new ArrayList<>();
		String name;
		String status;
		int price, id;
		for(Security sec : securitiesFromFile) {
			id = sec.getOrderId();
			name = sec.getName();
			price = sec.getPrice();
			status = sec.getStatus();
			temp.add(new SecurityInfo(id, name, status, price));
		}
		storage.write(temp);
	}

	/*public double calculateProfits(String name, int price) {
		int profit = 0;
		for(Security sec : securitiesFromLog) {
			if(sec.getName().equals(name))
				if(sec.getStatus().equals("profits"))
					return sec.getProfit();
				else {
					profit = price - sec.getPrice();
					break;
				}
		}
		return profit;
	}*/

	public void getFromLog(ArrayList<Object> l) throws IOException {
		dataFromLog = l;
		manageDataFromLog();
	}

	public void manageDataFromLog() {
		securitiesFromLog = new TreeSet<>();
		String name, status;
		int price, amount, id;
		for(int i=0 ; i < dataFromLog.size() ; i+=5) {
			id = (int)dataFromLog.get(i);
			name = (String)dataFromLog.get(i+1);
			status = (String)dataFromLog.get(i+2);
			amount = (int)dataFromLog.get(i+3);
			price = (int)dataFromLog.get(i+4);

			Security s = new Security(id, name, status, price, amount);
			securitiesFromLog.add(s);
		}
	}

	public void allProfits() {
		for(Security sec : securitiesFromLog) {
			if(sec.getStatus().equals(sell)) {
				int pr = calcProfit(sec);
				Security secProfit = new Security(sec.getOrderId(), sec.getName(), profit, pr);
				securityProfits.add(secProfit);
			}
		}
	}
	
	public int calcProfit(Security sec) {
		int sumProfit = sec.getPrice();
		for(Security secCheck : securitiesFromLog) {
			if(sec.getOrderId() != secCheck.getOrderId() &&
					secCheck.getName().equals(sec.getName())) {
				if(secCheck.getStatus().equals(buy)) {
					sumProfit -= sec.getTotalPrice();
				}
				else if(secCheck.getStatus().equals(sell)) {
					sumProfit += sec.getTotalPrice();
				}
			}
		}
		return sumProfit;
	}







}
