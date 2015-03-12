
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.*;
import java.net.*;

//import com.edw.rmi.Message;

 class ReaderThread extends Thread {
	 FileServIntf fileservintf[] = new FileServIntf[1000];
	 int n_servers;
	 int threadId;
	 String filename;
	 boolean threadOn = true;
	 int chnoToRead;
	ReaderThread(FileServIntf fileservintf[] , int n_servers , int threadId , String filename){
		this.n_servers = n_servers;
		this.threadId = threadId;
		this.filename = filename;
		this.fileservintf = fileservintf;
		this.chnoToRead = threadId;
	}
	
	public void run(){
	try {
		//File op = new File("output");
		//op.mkdir();
		while(threadOn){
    long  chCount = fileservintf[threadId].NumFileChunks(filename);
    int offset = 0;
    for(int i=0; i<chCount; i++){
		if(i % n_servers == chnoToRead)			
		{
			byte[] b = fileservintf[threadId].FileRead64K(filename, offset);
			FileOutputStream tmpStored = new FileOutputStream("tmpstore/"+"chunk"+i);
			tmpStored.write(b);
			tmpStored.close();
		}
	offset = offset + 65536;
	}
		System.out.println("thread executed for fileserver" + threadId);
		threadOn = false;
	}
    }
    
    catch(java.rmi.ConnectException e) {
		// If fileserver is down then increment the server id and restart the current thread.
		System.out.println("\n\nFileserver no"+this.threadId + " is down ");
      System.out.println("Assigning new fileserver object to current thread\n\n");
	   this.threadOn = true;
	   this.threadId = (this.threadId + 1) % n_servers;
	   this.run();		
    }
	catch(Exception e){
		System.out.println("Exception occured: " + e); 
	}
	}
	}
 
public class Client {
      FileServIntf fileservintf[] = new FileServIntf[1000];
            int n_servers = 0;
    private void doTest(int servPortNo){
        try {
            // fire to localhost port 1099
            Registry myRegistry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            Registry FileServRegistry =  LocateRegistry.getRegistry("127.0.0.1", servPortNo);
            // search for myMessage service
            RegistryIntf impl = (RegistryIntf) myRegistry.lookup("rmiRegistry");
             
            // call server's method        
            
             String list1[] = impl.getFileServers();
             int i=0;
             for(String t:list1){
				 if(t != null)
				{
					System.out.println(t);
					 fileservintf[n_servers++] = (FileServIntf)FileServRegistry.lookup(t);
					System.out.println("obtained server name:"+t);	
				}
			 }
            
        } catch (Exception e) {
            e.printStackTrace();
        }       
    }
    
    
     public void mergeChunks(String filename ){
		 try{
		 long  chCount = fileservintf[0].NumFileChunks(filename);
    
		FileOutputStream fout = new FileOutputStream("output/"+filename,true);
		for(long i=0; i<chCount; i++){
			FileInputStream fis = new FileInputStream("tmpstore/chunk"+i);
			System.out.println("merged chunk no : "+i);
			while(fis.available() > 0){
			byte b = (byte)fis.read();
			fout.write(b);
			}
			fis.close();
		}
		fout.close();
		
		}catch(Exception e){
			}
	 }
     
     public  void writeFile(String args[]){
	try {
      
     
    FileInputStream fis = new FileInputStream(args[0]);
    
    byte toSend[] = new byte[65536];
    int bytesRead = -1;
    int offset = 0;
	byte b;
    while(fis.available() > 0){
	//System.out.println((char)b);
	b = (byte)fis.read();
	toSend[++bytesRead] = b;
		if((bytesRead+1) % 65536 == 0){
		int status = fileservintf[0].FileWrite64K(args[0] , offset, toSend);
		//System.out.println(status);		
		
		bytesRead = -1;
		offset = offset + 65536;
		if(status < 0)
		{
			System.out.println("Error writing file" + status);
			break;
		}
		}
	}
    }
    catch(Exception e) {
      System.out.println("Exception: " + e);
    }
	}
	
	

    public static void main(String[] args) throws IOException,InterruptedException{
		
		File tmpdir = new File("tmpstore");
		tmpdir.mkdir();
		
        Client main = new Client();
        int servPortNo = Integer.parseInt(args[1]);
        main.doTest(servPortNo);
        int n_servers = main.n_servers;
        FileServIntf fileservintf[] = main.fileservintf;
        int threadId = 0;
        String filename = args[0];
        ReaderThread thread1[] = new ReaderThread[1000];
        
        
        
        main.writeFile(args);
        
        
        for(int i=0; i<n_servers; i++){
         thread1[i] = new ReaderThread(fileservintf , n_servers , i , filename);
			thread1[i].start();
		}
		
		for(int i=0; i<n_servers; i++){
         	thread1[i].join();
		}
		
		main.mergeChunks(args[0]);
		
		tmpdir.delete();
    }
}
