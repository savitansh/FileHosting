 
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.*;
 
public interface FileServIntf extends Remote {
 public int FileWrite64K(String filename, long offset, byte[] data) throws IOException, RemoteException;

public long NumFileChunks(String filename) throws IOException, RemoteException;

public byte[] FileRead64K(String filename, long offset) throws IOException, RemoteException;
   
}
