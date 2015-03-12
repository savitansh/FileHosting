import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
 
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.io.*;

public class FileServImp extends UnicastRemoteObject implements FileServIntf {
	
		String filePath = null;

 public FileServImp() throws RemoteException {    
	}

	  public byte[] FileRead64K(String filename, long offset) throws IOException,RemoteException{

	long chunkNumber = offset / 65536;
	String chunkName = new String("chunk"+chunkNumber);
	filename = filename + "/";
	File f = new File(filename+chunkName);
	FileInputStream fin = new FileInputStream(f);
	byte tmpb[] = new byte[(int)f.length()];
	fin.read(tmpb);
	fin.close();
	return tmpb;
	}

	public long NumFileChunks(String filename) throws IOException,RemoteException{
	int counter = new File(filename+"/").listFiles().length;
	return counter;
	}

		
	public int FileWrite64K(String filename, long offset, byte[] data) throws IOException,RemoteException{
	System.out.println("enterd");
	if(offset % 65536 != 0)
	return -3;
	
	if(data.length != 65536)
	return -4;
	
	long chunkNumber = offset / 65536;
	filename = filename+"/";
	System.out.println("filename with is:"+filename);
	File file = new File(filename);
	if (!file.exists()) {
		if (file.mkdir()) {
			System.out.println("Directory is created!");
		} else {
			return -1;
		}
	} else {
		System.out.println(" exists");
	}
	
	String chunkName = new String("chunk"+chunkNumber);
	File f = new File(filename+chunkName);
	
	if(!f.createNewFile())
	System.out.println("cannot create");
	
	FileOutputStream chunk = new FileOutputStream(f);
	
	int dataLen = 0;
	while(dataLen < 65536){
	//System.out.println(data[dataLen]);
	chunk.write(data[dataLen]);
	dataLen++;
	}
	chunk.close(); 
	 
	return 0;
	}

 }
