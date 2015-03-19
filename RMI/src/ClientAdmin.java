
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class ClientAdmin implements java.rmi.Remote {

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		GestionStation proxy = (GestionStation) Naming.lookup("rmi://localhost:1099/Gestionnaire");
		//proxy.creerVelo("5", false);
		proxy.affecterVeloStation("5", "3");
	}
}

