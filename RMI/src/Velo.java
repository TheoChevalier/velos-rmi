import java.io.Serializable;
import java.rmi.Remote;
import java.util.HashMap;


public class Velo implements Serializable{
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
	private Client client;

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Velo(String numV, boolean maintenance) {
		this.numV = numV;
		if(maintenance){
			this.etat = Etat.Maintenance;
		}else{
			this.etat = Etat.Libre;
		}
		client = null;
		listeVelos.put(numV, this);
	}

	public Velo(String numV, boolean maintenance, Client client) {
		this(numV, maintenance);
		this.client = client;
		if(client!=null){
			this.etat=Etat.Emprunte;
		}
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

	public String afficherEtat(){
		switch(etat){
		case Libre:
			return "Le vélo " + this.numV + " est opérationnel. Il est libre.";
		case Emprunte:
			return "Le vélo " + this.numV + " est opérationnel. Il est en cours d'usage locatif par un abonné.";
		case Maintenance:
			return "Le vélo " + this.numV + " est en maintenance dans les ateliers.";
		}
		return null;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}

}
