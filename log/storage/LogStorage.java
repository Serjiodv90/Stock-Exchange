package log.storage;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

public class LogStorage {

	final String filename = "logdata.bin";

	private Set<SecurityInfo> storage=new HashSet<SecurityInfo>();

	public LogStorage() throws IOException 
	{
		File f=new File(filename);
		if(!f.exists())
			f.createNewFile();
	}

	public Set<SecurityInfo> loadAll() throws ClassNotFoundException, IOException {
		ObjectInputStream oi;
		
		FileInputStream f=new FileInputStream(filename);
		if(f.available() < 1) {
			f.close();
			return this.storage;
		}
		
		 oi = new ObjectInputStream(f);
		
		 try{while (true)
			this.storage.add((SecurityInfo) oi.readObject());}
		 catch(EOFException e){
			 
		 }
		oi.close();
		return this.storage;

	}

	public void saveToFile(Set<SecurityInfo> s) throws IOException {
		File f=new File(filename);
		if(!f.exists())
		{	f.delete();}
		f.createNewFile();
		FileOutputStream fOut=new FileOutputStream(filename);
		ObjectOutputStream write=new ObjectOutputStream(fOut);
		for (SecurityInfo sec : s) {
			write.writeObject(sec);
		}
		write.close();
	}
}
