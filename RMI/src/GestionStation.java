import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;


public interface GestionStation {
	void GestionStationsImpl() throws RemoteException;

	public void main(String[] args) throws RemoteException, MalformedURLException;
	
	public int getClientMotDePasse();

	void log(String aMessage);
}
