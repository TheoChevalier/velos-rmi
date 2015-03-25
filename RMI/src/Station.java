import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;


public class Station implements Remote , Serializable{
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

	public int getNbPlacesLibres() {
		int nb = 0;
		for (Velo v : lesVelos) {
			if (v.getEtat().toString().equals("Maintenance") || v.getEtat().toString().equals("Emprunte")) {
				nb++;
			}
		}
		return capacite - nb;
	}

	public Vector<Velo> getLesVelos() {
		return lesVelos;
	}
	
	public int getNbVeloLibre() {
		int nb=0;
		for (Velo velo : lesVelos) {
			if (velo.getEtat().toString().equals("Libre")){
				nb++;
			}
		}
		return nb;
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
			if(v.getClient()==null && !v.getEtat().toString().equals("Maintenance")){
				trouve=true;
			}
		}
		return v;
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		GestionStation proxy = (GestionStation) Naming.lookup("rmi://localhost:1099/Gestionnaire");
		Station station = new Station(args[0], Double.parseDouble(args[1]), Double.parseDouble(args[2]), Integer.parseInt(args[3]));
		station.lesVelos = proxy.majCacheStation(station.getNumS());
		for (Velo velo : station.lesVelos) {
			System.out.println("Velo numero : " + velo.getNumV());
		}
		
		System.out.println(station.getNbVeloLibre());

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
		//in.close();

		Velo velo = station.rechercherVeloLibre();
		if(velo != null) {
			System.out.println("Vous pouvez prendre le vélo "+ velo.getNumV());
			if (proxy.emprunterVelo(id, velo.getNumV())) {
				station.lesVelos = proxy.majCacheStation(station.getNumS());
				System.out.println("Vous avez emprunté le vélo "+ velo.getNumV());
			} else {
				System.out.println("Vous ne pouvez pas emprunter le vélo " + velo.getNumV());
			}
		} else {
			if (proxy.rechercherStationPlusProche(station.getNumS()) != null){
				Station stationPlusProche = proxy.rechercherStationPlusProche(station.getNumS());
				System.out.println("La station la plus proche ayant des vélos disponibles est la numéro " + stationPlusProche.getNumS());
			}
		}
		
		System.out.println("\nRendre un vélo :");
		in = new Scanner(System.in);
		System.out.println("Veuillez saisir le numéro du vélo :\n");
		String idVelo = in.nextLine();
		while (proxy.rechercherVelo(idVelo) == null) {
			System.out.println("Échec.\nVeuillez saisir le numéro du vélo :\n");
			idVelo = in.nextLine();
		}
		in.close();
		
		if(station.getNbPlacesLibres()>0){
			if(proxy.rendreVelo(station.getNumS(), idVelo)){
				station.lesVelos = proxy.majCacheStation(station.getNumS());
				System.out.println("Vous avez bien rendu le vélo " + idVelo);
			} else {
				System.out.println("Vous ne pouvez pas rendre le vélo " + idVelo);
			}
		} else {
			System.out.println("La station n'a plus de place disponible.");
		}
	}
}
