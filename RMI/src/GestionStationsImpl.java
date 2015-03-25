
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Vector;


public class GestionStationsImpl extends UnicastRemoteObject implements GestionStation {
	static Connection conn;

	protected GestionStationsImpl(String nomBD) throws RemoteException {
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:"+nomBD+";IGNORECASE=TRUE", "sa", "");
			// on cree un objet Statement qui va permettre l'execution des requetes
			Statement s = conn.createStatement();

			String query = "select numC from CLIENTS limit 1";
			try {
				s.executeQuery(query);
			} catch(Exception e) {
				// sinon on l'a cree
				s.execute("DROP TABLE CLIENTS");
				s.execute("create table CLIENTS  ( " +
						" numC bigint auto_increment NOT NULL PRIMARY KEY, " +
						" nomC VARCHAR( 256 ) , " +
						" mdpC VARCHAR( 10 ))");
				// on ajoute des entrees de test
				s.executeUpdate("insert into CLIENTS (nomC, mdpC) values ('Léa', '000000001')");
				s.executeUpdate("insert into CLIENTS (nomC, mdpC) values ('Paul', '000000002')");
			}

			query = "select numS from STATIONS limit 1";
			try {
				s.executeQuery(query);
			} catch(Exception e) {
				s.execute("DROP TABLE STATIONS");
				s.execute("create table STATIONS  ( " +
						" numS VARCHAR( 256 ) NOT NULL PRIMARY KEY, " +
						" longitude NUMERIC , " +
						" latitude NUMERIC , " +
						" capacite INTEGER)");

				s.executeUpdate("insert into STATIONS values ('1', 0.6, 0.3, 10)");
				s.executeUpdate("insert into STATIONS values ('2', 0.8, 0.9, 15)");
			}
			query = "select numV from VELOS limit 1";
			try {
				s.executeQuery(query);
			} catch(Exception e) {
				s.execute("DROP TABLE VELOS");
				s.execute("create table VELOS  ( " +
						" numV VARCHAR( 256 ) NOT NULL PRIMARY KEY, " +
						" maintenance BOOLEAN, " +
						" client VARCHAR( 256 ), " +
						" station VARCHAR( 256 ))");
				s.executeUpdate("insert into VELOS values ('1', false, null, '1')");
				s.executeUpdate("insert into VELOS values ('2', false, null, '2')");
				s.executeUpdate("insert into VELOS values ('3', false, '1', '1')");
				s.executeUpdate("insert into VELOS values ('4', true, null, '1')");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void creerVelo(String numV, boolean maintenance) throws RemoteException{
		try{
			int n;
			Statement s = conn.createStatement();
			n=s.executeUpdate("insert into VELOS values ('"+numV+"', "+maintenance+", null, null)");
			System.out.println(n);
			System.out.println("insert into VELOS values ('"+numV+"', "+maintenance+", null, null)");
			Velo velo = new Velo(numV, maintenance);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void creerStation(String numS, double longitude, double latitude, int capacite) throws RemoteException{
		try{
			Statement s = conn.createStatement();
			s.executeUpdate("insert into STATIONS values ('"+ numS +"', "+longitude+", "+latitude+", "+capacite+")");
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}


	public void affecterVeloStation(String numVelo, String numStation) throws RemoteException{
		if(rechercherStation(numStation) != null && rechercherVelo(numVelo) != null){
			try{
				Station station = Station.getListeStations().get(numStation);
				Velo velo = Velo.getListeVelos().get(numVelo);
				Statement s = conn.createStatement();
				s.executeUpdate("update VELOS set station='"+ numStation +"' WHERE numV='"+numVelo+"'");
			}
			catch(SQLException e){
				e.printStackTrace();
			}
		}
	}

	public Velo rechercherVelo(String numVelo) throws RemoteException {
		try{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select * from VELOS where numV = '"+numVelo+"'");
			if (rs.next()) {
				String numV = rs.getString("numV");
				boolean maintenance = rs.getBoolean("maintenance");
				String numC = rs.getString("client");
				Client client = rechercherClient(numC);
				return new Velo(numV, maintenance, client);
			}
			else{
				return null;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public boolean emprunterVelo(String id, String numV) throws RemoteException {
		if(rechercherVelo(numV) != null && rechercherClient(id) != null && getVeloClient(id) == null) {
			try {
				Statement s = conn.createStatement();
				s.executeUpdate("update VELOS set client='" + id + "' WHERE numV='" + numV + "'");
				return true;
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
		return false;
	}
	
	public boolean rendreVelo(String idStation, String idVelo) throws RemoteException {
		if(rechercherStation(idStation) != null && rechercherVelo(idVelo) != null) {
			Velo v = rechercherVelo(idVelo);
			if (v.getClient() != null) {
				try {
					Statement s = conn.createStatement();
					s.executeUpdate("update VELOS set client=null, station='"+ idStation+"' WHERE numV='" + idVelo + "'");
					return true;
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
			} else {
				return false;
			}
		}
		return false;
	}
	
	

	public Station rechercherStation(String numStation) throws RemoteException{
		try{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select * from STATIONS where numS = '"+numStation+"'");
			if (rs.next()) {
				String numS = rs.getString("numS");
				double longitude = rs.getDouble("longitude");
				double latitude = rs.getDouble("latitude");
				int capacite = rs.getInt("capacite");
				return new Station(numS, longitude, latitude, capacite);
			}
			else{
				return null;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public Vector majCacheStation(String numStation) throws RemoteException{
		Vector<Velo> lesVelos = new Vector<Velo>();
		try{
			Velo velo;
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select numV, maintenance, station, client from VELOS where station = '"+numStation+"'");
			while (rs.next()) {
				String numV = rs.getString("numV");
				boolean maintenance = rs.getBoolean("maintenance");
				String numC = rs.getString("client");
				Client client = rechercherClient(numC);
				velo = new Velo(numV, maintenance, client);
				lesVelos.add(velo);
			}
			return lesVelos;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public Client rechercherClient(String numClient) throws RemoteException {
		if(numClient != null){
			try{
				Statement s = conn.createStatement();
				ResultSet rs = s.executeQuery("select * from CLIENTS where numC = '"+numClient+"'");
				if (rs.next()) {
					String numC = rs.getString("numC");
					String nomC = rs.getString("nomC");
					String mdpC = rs.getString("mdpC");
					return new Client(numC, nomC, mdpC);
				}
				else{
					return null;
				}
			}
			catch(SQLException e){
				e.printStackTrace();
			}
		}
		return null;
	}

	public Velo getVeloClient (String numClient) throws RemoteException {
		try{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select * from VELOS where client = '"+numClient+"'");
			if (rs.next()) {
				return rechercherVelo(rs.getString("numV"));
			}
			else {
				return null;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public boolean authentificationClient(String numClient, String mdpClient) throws RemoteException{
		try{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select * from CLIENTS where numC = '"+numClient+"' AND mdpC='"+mdpClient+"'");
			if (rs.next()) {
				return true;
			}
			else{
				return false;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}


	public static void main(String[] args) throws RemoteException, MalformedURLException {
		System.out.println("coucou2");
		LocateRegistry.createRegistry(1099);
		GestionStationsImpl gest = new GestionStationsImpl("ServiceVelos");
		Naming.rebind("Gestionnaire", gest);
		System.out.println("Running on port 1099");

		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select * from VELOS");
			while (rs.next()) {
				String nom = rs.getString("numV");
				String client = rs.getString("client");
				String station = rs.getString("station");
				System.out.println("Vélo " + nom + ", Client : " + client + " dans la station "+station);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select * from CLIENTS");
			while (rs.next()) {
				String nom = rs.getString("nomC");
				String numC = rs.getString("numC");
				String mdp = rs.getString("mdpC");
				System.out.println("Client : " + nom + " numéro : " + numC + " mdp : " + mdp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select numS from STATIONS");

			while (rs.next()) {
				String num = rs.getString("numS");
				System.out.println("Station " + num);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] creerClient(String nom) {
		String[] retour = new String[2];
		retour[0] = "Aucun";
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100000000);
		System.out.println("Generated : " + randomInt);
		retour[1] = Integer.toString(randomInt);

		// INSERT + récupération du dernier ID autoincrémenté inséré
		try {
			String sql = "INSERT INTO CLIENTS (nomC, mdpC) values ('" + nom + "', '" + randomInt +"')";

			Statement s = conn.createStatement();
			s.executeUpdate(sql);
			sql = "SELECT MAX(numC) FROM CLIENTS";
			ResultSet rs = s.executeQuery(sql);
			while (rs.next()) {
				retour[0] = rs.getString("MAX(numC)");
				System.out.println("Last ID : " + retour[0]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}


		return retour;
	}


	public Station stationDuVelo(String velo) throws RemoteException{
		try{
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select station from VELOS where numV = '"+ velo +"'");
			while (rs.next()) {
				String station = rs.getString("station");
				return rechercherStation(station);
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}


}
