
public class Client {
	private String numC;
	private String nomC;
	private String mdpC;
	
	public Client(String numC, String nomC, String mdpC){
		this.numC = numC;
		this.nomC = nomC;
		this.mdpC = mdpC;
	}

	public String getNumC() {
		return numC;
	}

	public void setNumC(String numC) {
		this.numC = numC;
	}

	public String getNomC() {
		return nomC;
	}

	public void setNomC(String nomC) {
		this.nomC = nomC;
	}

	public String getMdpC() {
		return mdpC;
	}

	public void setMdpC(String mdpC) {
		this.mdpC = mdpC;
	}
}
