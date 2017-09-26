
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;
import log.Interface.LogInterface;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import portfolioManager.iface.*;
public class MainInterface {

	public static void main(String[] args) throws NotBoundException, ClassNotFoundException, IOException {
		BankManager bank = (BankManager) Naming.lookup("rmi://172.20.18.151/Bank");
		ExchangeManager exchange = (ExchangeManager) Naming.lookup("rmi://172.20.18.151/Exchange");
		
		Scanner s = new Scanner(System.in);
		Integer id;
		String secret;
		boolean isValidAcc = false,exit=false;;
		Account acc;
		byte option=0;

		while (!isValidAcc) {
			try {
				/*System.out.print("Please enter log in:");
				id = s.nextInt();
				System.out.print("Please enter secret:");
				secret = s.next();*/
				acc = new Account("txgPzK", 204);
				//bank.getTransactionsSince(secret, id, new Date());
				System.out.println("Login Successfull");
				isValidAcc=true;
				System.out.println(bank.getAssets(acc.getSecret(), acc.getId()));
				System.out.println(bank.getTransactionsSince(acc.getSecret(), acc.getId(),new Date(0)));
				System.out.println(bank.getAssets(acc.getSecret(), acc.getId()));
				System.out.println(bank.getQuantityOfAsset(acc.getSecret(), acc.getId(),"NIS"));

			} catch (WrongSecretException ex) {
				System.out.println("Wrong login/secret");
			} catch (InputMismatchException ex) {
				System.out.println("Please enter correct values");
				s.next();
			} catch (InternalServerErrorException e) {
				System.out.println("Internal Server Error");
			} catch (DoesNotHaveThisAssetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		LogInterface log;
		log=new LogInterface();
		portfolioInterface port=new portfolioInterface() ;
		while(true)
		{
		
		System.out.println("1)Securities Managment:");
		System.out.println("2)View Portfolio:");
		System.out.println("3)View Log:");
		System.out.println("4)Exit:");
		System.out.println("Choose an option");
		try{
			option=s.nextByte();
		}
		 catch (InputMismatchException ex) {
				System.out.println("Please enter integer as an option [1-4]");
				s.next();
		}
		switch(option){
		case 1:
			
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			System.out.println("Goodbye!! Exit:");
			log.save();
			port.getLogData(log.moveToPortfolio());
			port.calculateProfits();
			System.exit(0);

			break;
		
		default:System.out.println("Choose correct option [1-4]");
			
		}
		}
	}


}
