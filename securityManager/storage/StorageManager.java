package securityManager.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;



public class StorageManager {
	
	private File openOrdersFile;
	private final String STORAGEFILENAME = "openOrders.txt";
	
	public StorageManager() throws IOException {
		
		openOrdersFile = new File(STORAGEFILENAME);
		
		if(!openOrdersFile.exists())
			openOrdersFile.createNewFile();		
	}
	
	public boolean isFileEmpty() {
		return openOrdersFile.length() == 0 ? true : false;
	}
	
	public void saveOpenOders (List<OrderInfo> lst) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(openOrdersFile);
		for(OrderInfo info : lst) 
			info.writeOrders(pw);
		
		pw.close();
		
	}
	
	public List<OrderInfo> loadOrders () throws FileNotFoundException {
		Scanner in = new Scanner(openOrdersFile);
		List<OrderInfo> lst = new LinkedList<>();
		
		while(in.hasNext()) 
			lst.add(new OrderInfo(in));
		
		in.close();
		return lst;
	}

}
