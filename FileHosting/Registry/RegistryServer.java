 
//import com.edw.rmi.MessageImpl;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
 
public class RegistryServer {
     
     
    private void startServer() {
        try {
            // create on port 1099
            Registry registry = LocateRegistry.createRegistry(1099);
             
            // create a new service named rmiRegistry
            registry.rebind("rmiRegistry", new RegistryImp());
            
        } catch (Exception e) {
            e.printStackTrace();
        }     
        System.out.println("system is ready");
    }
     
    public static void main(String[] args) throws RemoteException{
        RegistryServer main = new RegistryServer();
       // int portNo = Integer.parseInt(args[0]);
        main.startServer();
    }
}
