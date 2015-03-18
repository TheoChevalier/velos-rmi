import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;


public class Station implements Remote {
	private int numS;
	private double longitude;
	private double latitude;
	private int capacite;
	private int nbPlaceLibre;
	private Vector<Velo> lesVelos;
	
	public Station(int numS, double longitude, double latitude, int capacite){
		this.numS=numS;
		this.longitude=longitude;
		this.latitude=latitude;
		this.capacite=capacite;
		lesVelos = new Vector<Velo>();
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
		Station station = new Station(1, 0.6, 0.13, 14);
		Velo velo = station.rechercherVeloLibre();
		if(velo!=null){
			System.out.println("Vous pouvez prendre le vélo "+ velo.getNumV());
		}
		else{
			//proxy.getVeloLibre();
		}
		//proxy.getClientMotDePasse();
	}
}
