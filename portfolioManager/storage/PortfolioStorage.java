package portfolioManager.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;


public class PortfolioStorage {
	public static final String FILENAME = "profits";
	private SecurityInfo sec;
	
	public PortfolioStorage() {
		
	}
	
	public ArrayList<SecurityInfo> read() throws IOException, ClassNotFoundException {
		ArrayList<SecurityInfo> profits = new ArrayList<>();
		FileInputStream in = new FileInputStream(FILENAME);
		ObjectInputStream ob = new ObjectInputStream(in);
		while(ob.available() > 0) {
			profits.add((SecurityInfo)ob.readObject());
		}
		ob.close();
		return profits;
	}
	
	public void write(ArrayList<SecurityInfo> profits) throws IOException {
		File f = new File(FILENAME);
		if(f.exists())
			f.delete();
		
		FileOutputStream out = new FileOutputStream(FILENAME);
		ObjectOutputStream ob = new ObjectOutputStream(out);
		
		for(SecurityInfo sec : profits) {
			ob.writeObject(sec);
		}
		
		ob.close();
		
	}
}
