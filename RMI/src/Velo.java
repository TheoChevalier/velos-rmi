import java.util.HashMap;


public class Velo {
	private static HashMap<String, Velo> listeVelos = new HashMap<String, Velo>();

	public static HashMap<String, Velo> getListeVelos() {
		return listeVelos;
	}

	public static void setListeVelos(HashMap<String, Velo> listeVelos) {
		Velo.listeVelos = listeVelos;
	}

	enum Etat{
		Emprunte, Libre, Maintenance
	}
	
	private String numV;
	private Etat etat;
	private Station station;
	
	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public Velo(String numV, boolean maintenance){
		this.numV=numV;
		if(maintenance){
			this.etat= Etat.Maintenance;
		}
		station=null;
		listeVelos.put(numV, this);
	}

	public String getNumV() {
		return numV;
	}

	public void setNumV(String numV) {
		this.numV = numV;
	}

	public Etat getEtat() {
		return etat;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}
	
}
