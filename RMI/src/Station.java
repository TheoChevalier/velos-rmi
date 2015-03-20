import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;


public class Station implements Remote {
	private static HashMap<String, Station> listeStations = new HashMap<String, Station>();

	public static HashMap<String, Station> getListeStations() {
		return listeStations;
	}

	public static void setListeStations(HashMap<String, Station> listeStations) {
		Station.listeStations = listeStations;
	}

	private String numS;
	private double longitude;
	private double latitude;
	private int capacite;
	private int nbPlaceLibre;
	private Vector<Velo> lesVelos;
	
	public Station(String numS, double longitude, double latitude, int capacite){
		this.numS=numS;
		this.longitude=longitude;
		this.latitude=latitude;
		this.capacite=capacite;
		lesVelos = new Vector<Velo>();
		listeStations.put(numS, this);
	}
	
	public String getNumS() {
		return numS;
	}

	public void setNumS(String numS) {
		this.numS = numS;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getCapacite() {
		return capacite;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	public int getNbPlaceLibre() {
		return nbPlaceLibre;
	}

	public void setNbPlaceLibre(int nbPlaceLibre) {
		this.nbPlaceLibre = nbPlaceLibre;
	}

	public Vector<Velo> getLesVelos() {
		return lesVelos;
	}

	public void setLesVelos(Vector<Velo> lesVelos) {
		this.lesVelos = lesVelos;
	}

	public Velo rechercherVeloLibre(){
		Velo v = null;
		boolean trouve=false;
		Iterator it = lesVelos.iterator();
		while(it.hasNext() && !trouve){
			v=(Velo)it.next();
			if(v.getEtat().toString().equals("Libre")){
				trouve=true;
			}
		}
		return v;
	}
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		GestionStation proxy = (GestionStation) Naming.lookup("rmi://localhost:1099/Gestionnaire");
		Station station = new Station("1", 0.6, 0.3, 10);
		station.lesVelos = proxy.majCacheStation("1");
		for (Velo velo : station.lesVelos) {
			System.out.println("Velo numero : " + velo.getNumV());
		}
		//Velo velo = station.rechercherVeloLibre();
		/*if(velo!=null){
			System.out.println("Vous pouvez prendre le v�lo "+ velo.getNumV());
		}
		else{
			//proxy.getVeloLibre();
		}
		*/
		//proxy.getClientMotDePasse();
		
		  Scanner in = new Scanner(System.in);
		  System.out.println("Veuillez saisir votre identifiant :\n");
		  String id = in.nextLine();
		  System.out.println("Veuillez saisir votre mot de passe :\n");
		  String mdp = in.nextLine();
		  while (! proxy.authentificationClient(id, mdp)) {
		      System.out.println("Échec de l’authentification.\nVeuillez saisir votre identifiant :\n");
		  id = in.nextLine();
		  System.out.println("Veuillez saisir votre mot de passe :\n");
		      mdp = in.nextLine();
		  }
		  System.out.println("Vous êtes connecté.\n");
		  in.close();
	      
	}
}
