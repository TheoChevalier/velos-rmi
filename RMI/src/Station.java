import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


public class Station implements Remote {
	private static HashMap<String, Station> listeStations = new HashMap<String, Station>();

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
		proxy.creerStation("3", 0.5, 0.9, 12);
		/*Velo velo = station.rechercherVeloLibre();
		if(velo!=null){
			System.out.println("Vous pouvez prendre le vélo "+ velo.getNumV());
		}
		else{
			//proxy.getVeloLibre();
		}
		*/
		//proxy.getClientMotDePasse();
	}
}
