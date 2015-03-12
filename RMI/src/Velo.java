
public class Velo {
	enum Etat{
		Emprunte, Libre, Maintenance
	}
	
	private int numV;
	private Etat etat;
	
	public Velo(int numV){
		this.numV=numV;
		this.etat= Etat.Libre;
	}

	public int getNumV() {
		return numV;
	}

	public void setNumV(int numV) {
		this.numV = numV;
	}

	public Etat getEtat() {
		return etat;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}
	
}
