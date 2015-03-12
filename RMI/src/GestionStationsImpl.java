import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;


public class GestionStationsImpl extends UnicastRemoteObject implements GestionStation {
	protected GestionStationsImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	public void main(String[] args) throws RemoteException, MalformedURLException {
		LocateRegistry.createRegistry(1099);
		GestionStationsImpl gest = new GestionStationsImpl();
		Naming.rebind("Gestionnaire", gest);
		log("Running on port 1099");
	}
	
	public int getClientMotDePasse() {
        Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100);
		log("Generated : " + randomInt);
		return randomInt;
	}


	public void log(String aMessage){
		System.out.println(aMessage);
	}

	@Override
	public void GestionStationsImpl() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
