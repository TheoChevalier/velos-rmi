import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;


public interface GestionStation extends Remote {
	
	public String[] creerClient(String nom) throws RemoteException;
	public void creerStation(String string, double longitude, double latitude, int capacite) throws RemoteException;
	public void creerVelo(String numV, boolean maintenance) throws RemoteException;
	public void affecterVeloStation(String numVelo, String numStation) throws RemoteException;
	public Velo rechercherVelo(String numVelo) throws RemoteException;
	public boolean rechercherStation(String numStation) throws RemoteException;
}
