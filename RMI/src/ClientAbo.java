import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;


public class ClientAbo implements java.rmi.Remote {

	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		GestionStation proxy = (GestionStation) Naming.lookup("rmi://localhost:1099/Gestionnaire");
	      String nom;
	      Scanner in = new Scanner(System.in);
	      System.out.println("Veuillez saisir votre nom :");
	      nom = in.nextLine();
	      in.close();
		  String[] retour = proxy.creerClient(nom);
		  System.out.println("Identifiant : " + retour[0] + ", mot de passe : " + retour[1]);
	}
}
