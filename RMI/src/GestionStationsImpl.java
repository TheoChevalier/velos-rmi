
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
	        	s.execute("create table STATIONS  ( " +
	        			" numS VARCHAR( 256 ) NOT NULL PRIMARY KEY, " +
	        			" longitude NUMERIC , " +
	        			" latitude NUMERIC , " +
	        			" capacite INTEGER)");
	        	
	        	s.executeUpdate("insert into STATIONS values ('1', 0.6, 0.3, 10)");
	        	s.executeUpdate("insert into STATIONS values ('2', 0.8, 0.9, 15)");
	        }
	        query = "select numV from VELOS limit 1";
	        /*try {
	        	s.executeQuery(query);
	        } catch(Exception e) {*/
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
	        //}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void creerVelo(String numV, boolean maintenance) throws RemoteException{
		try{
			Statement s = conn.createStatement();
			s.executeUpdate("insert into VELOS values ('"+numV+"', "+maintenance+", null, null)");
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
			Station station = new Station(numS, longitude, latitude, capacite);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
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
	        	System.out.println(nom + ", Client : " + client);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select nomC from CLIENTS");
	        while (rs.next()) {
	        	String nom = rs.getString("nomC");
	        	System.out.println("Client : " + nom);
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

	@Override
	public void affecterVeloStation(String numVelo, String numStation)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean rechercherVelo(String numVelo) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rechercherStation(String numStation) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}




}
