import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;


public class GestionStationsImpl extends UnicastRemoteObject implements GestionStation {


	protected GestionStationsImpl() throws RemoteException {
		// TODO Auto-generated constructor stub
		System.out.println("coucou");
		
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException {
		System.out.println("coucou2");
		LocateRegistry.createRegistry(1099);
		GestionStationsImpl gest = new GestionStationsImpl();
		Naming.rebind("Gestionnaire", gest);
		System.out.println("Running on port 1099");
	}
	
	public int getClientMotDePasse() {
        Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100000000);
		System.out.println("Generated : " + randomInt);
		return randomInt;
	}




}
