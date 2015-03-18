import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;


public interface GestionStation extends Remote {
	
	public int getClientMotDePasse() throws RemoteException;

}
