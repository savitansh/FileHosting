 
//import com.edw.rmi.MessageImpl;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
 
public class FileServer {
     
     
    private void startServer(int portNo){
        try {
            // create on port 1097
            //Registry registry = LocateRegistry.createRegistry(1097);
            
            Registry registry = LocateRegistry.getRegistry(portNo);
            long strt = (long)System.currentTimeMillis()/1000;
            long time1 = strt;
            while(time1 - strt < 3){
			time1 = (long)System.currentTimeMillis()/1000;
			}
             String passedName = "myFileServer_"+time1;
            System.out.println(passedName); 
             Registry myRegistry = LocateRegistry.getRegistry("127.0.0.1", 1099);
            RegistryIntf impl = (RegistryIntf) myRegistry.lookup("rmiRegistry");
            boolean result = impl.RegisterServer(passedName);
            System.out.println(result);
            // create a new service named with passed name to rmiRegistry server
            registry.rebind(passedName, new FileServImp());
            
        } catch (Exception e) {
            e.printStackTrace();
        }     
        System.out.println("system is ready");
    }
     
    public static void main(String[] args) throws RemoteException{
        FileServer main = new FileServer();
        int portNo = 0;
        portNo = Integer.parseInt(args[0]);
        //System.out.println(portNo);
        if(portNo > 0)
        main.startServer(portNo);
        else
        {
			System.out.println("Enter registry port No:");
			return;
		}
    }
}
