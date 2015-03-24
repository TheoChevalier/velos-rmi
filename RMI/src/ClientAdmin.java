
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;


public class ClientAdmin implements java.rmi.Remote {

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		GestionStation proxy = (GestionStation) Naming.lookup("rmi://localhost:1099/Gestionnaire");
		//proxy.creerVelo("5", false);
		//proxy.affecterVeloStation("5", "3");
		
		Scanner in = new Scanner(System.in);
		System.out.println("Veuillez saisir le numéro du vélo :\n");
		String id = in.nextLine();
		while (proxy.rechercherVelo(id) == null) {
			System.out.println("Échec le vélo n'existe pas.\nVeuillez saisir le numéro du vélo :\n");
			id = in.nextLine();
		}
		Velo v = proxy.rechercherVelo(id);
		v.afficherEtat();
	}
}

