import java.util.HashMap;


public class Velo {
	private static HashMap<String, Velo> listeVelos = new HashMap<String, Velo>();

	enum Etat{
		Emprunte, Libre, Maintenance
	}
	
	private String numV;
	private Etat etat;
	
	public Velo(String numV, boolean maintenance){
		this.numV=numV;
		if(maintenance){
			this.etat= Etat.Maintenance;
		}
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
