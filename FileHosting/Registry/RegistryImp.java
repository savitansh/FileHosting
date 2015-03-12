import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
 
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
 
public class RegistryImp extends UnicastRemoteObject implements RegistryIntf {
 
	ArrayList<String> serverNames = new ArrayList<String>();
	public RegistryImp() throws RemoteException {    
	}
     
    @Override
    public boolean RegisterServer(String name) throws RemoteException{
        System.out.println("Register server : "+name);
		int master = 0;
		if (serverNames.isEmpty())
		master = 1;
		
		serverNames.add(name);
		
		System.out.println("server registered");
		
		if(master == 1)
        return true;
        
        return false;
        
    }
    
    @Override
     public String[] getFileServers() throws RemoteException{
	
		String serverList[] = new String[10000];
		int count = 0;
		
		for(String tmp:serverNames){
		serverList[count++] = tmp;
		}	
		
		return serverList;
	 }
}
