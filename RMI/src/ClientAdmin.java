
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;


public class ClientAdmin implements java.rmi.Remote {

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		GestionStation proxy = (GestionStation) Naming.lookup("rmi://localhost:1099/Gestionnaire");
		//int num = proxy.creerVelo(false);
		//proxy.affecterVeloStation(num, "3");
		
		Scanner in = new Scanner(System.in);
		System.out.println("Veuillez saisir le numéro du vélo :\n");
		String id = in.nextLine();
		while (proxy.rechercherVelo(id) == null) {
			System.out.println("Échec lors de la création, le vélo n’existe pas.\nVeuillez saisir le numéro du vélo :\n");
			id = in.nextLine();
		}
		Velo v = proxy.rechercherVelo(id);
		v.afficherEtat();
		
		//station où est garé
		if(v.getEtat().toString().equals("Libre")){
			Station s = proxy.stationDuVelo(id);
			System.out.println("Le vélo " + id + " est dans la station " + s.getNumS() + ".");
		}
	}
}

