import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;
import java.util.Vector;


public interface GestionStation extends Remote {
	
	public String[] creerClient(String nom) throws RemoteException;
	public void creerStation(String string, double longitude, double latitude, int capacite) throws RemoteException;
	public void creerVelo(String numV, boolean maintenance) throws RemoteException;
	public void affecterVeloStation(String numVelo, String numStation) throws RemoteException;
	public Velo rechercherVelo(String numVelo) throws RemoteException;
	public Station rechercherStation(String numStation) throws RemoteException;
	public Vector majCacheStation(String numStation) throws RemoteException;
	public Client rechercherClient(String numClient) throws RemoteException;
	public boolean authentificationClient(String numClient, String mdpClient) throws RemoteException;
	public boolean emprunterVelo(String id, String numV) throws RemoteException;
	public Velo getVeloClient(String idClient) throws RemoteException;
	public boolean rendreVelo(String idStation, String idVelo) throws RemoteException;
}
