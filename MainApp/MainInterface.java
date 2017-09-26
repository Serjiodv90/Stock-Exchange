package MainApp;

import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import exchange.api.NotEnoughMoneyException;
import exchange.api.NotEnoughStockException;
import exchange.api.StockNotTradedException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import portfolioManager.iface.*;
import log.Interface.LogInterface;
import securityManager.iface.*;
public class MainInterface {

	public static void main(String[] args) throws NotBoundException, ClassNotFoundException, IOException, WrongSecretException, InternalServerErrorException, InterruptedException, DoesNotHaveThisAssetException, NotEnoughStockException, InternalExchangeErrorException, NotEnoughMoneyException, NoSuchAccountException, StockNotTradedException {
		String bIP = "rmi://13.59.120.241/Bank";
		BankManager bank = (BankManager) Naming.lookup(bIP);

		Scanner s = new Scanner(System.in);
		Integer id=0;
		String secret = null;
		boolean isValidAcc = false;
		Account acc;
		byte option=0;

		while (!isValidAcc) {
			try {
				System.out.print("Please enter log in:");
				id = s.nextInt();
				System.out.print("Please enter secret:");
				secret = s.next();
				
				bank.getTransactionsSince(secret, id, new Date());
				System.out.println("Login Successfull");
				isValidAcc=true;

			} catch (WrongSecretException ex) {
				System.out.println("Wrong login/secret");
			} catch (InputMismatchException ex) {
				System.out.println("Please enter correct values");
				s.next();
			} catch (InternalServerErrorException e) {
				System.out.println("Internal Server Error");
			}

		}
		
		
		acc = new Account(secret, id);
		LogInterface log=new LogInterface();
		portfolioInterface port=new portfolioInterface(acc);
		SecurityTrading trading=new SecurityTrading(acc.getId(),acc.getSecret(),log);
		while(true)
		{
		System.out.println("1)Show supply & demand:");
		System.out.println("2)Place bid (buy stock):");
		System.out.println("3)Place ask (sell stock):");
		System.out.println("4)Show pending orders:");
		System.out.println("5)View Portfolio:");
		System.out.println("6)View Log:");
		System.out.println("7)Exit:");
		System.out.println("Choose an option");
		try{
			option=s.nextByte();
		}
		 catch (InputMismatchException ex) {
				System.out.println("Please enter integer as an option [1-7]");
				s.next();
		}
		switch(option){
		case 1:
			trading.showSupplyDemand();
			break;
		case 2:
			trading.placeBid();
			break;
		case 3:
			trading.placeAsk();
			break;
		case 4:
			trading.showPending();
			break;
		case 5:
			port.viewPortfolio();
			//port.getLogData(log.moveToPortfolio());
			//port.calculateProfits();
			//port.viewPortfolio();
			break;
		case 6:
			log.printSecuritiesLog();
			break;
		case 7:
			System.out.println("Goodbye!! Exit:");
			log.save();
			//port.getLogData(log.moveToPortfolio());
			//port.calculateProfits();
			s.close();
			System.exit(0);
			break;
		
		default:System.out.println("Choose correct option [1-7]");
			
		}
		}
	}


}
