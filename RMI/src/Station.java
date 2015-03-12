import java.util.Iterator;
import java.util.Vector;


public class Station {
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
}
