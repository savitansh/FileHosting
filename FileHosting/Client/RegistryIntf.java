 
import java.rmi.Remote;
import java.rmi.RemoteException;
 
public interface RegistryIntf extends Remote {
    public boolean RegisterServer(String name) throws RemoteException;
    public String[] getFileServers() throws RemoteException;
    
}
