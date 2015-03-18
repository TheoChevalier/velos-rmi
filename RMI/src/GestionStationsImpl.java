
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
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
	
	        // On regarde si la table existe deja
	        String query = "select numC from CLIENTS limit 1";
	        try {
	        	s.executeQuery(query);
	        } catch(Exception e) {
	        	// sinon on l'a cree
	        	s.execute("create table CLIENTS  ( " +
	        			" numC VARCHAR( 256 ) NOT NULL PRIMARY KEY, " +
	        			" nomC VARCHAR( 256 ) , " +
	        			" mdpC VARCHAR( 10 ))");
	        	// on ajoute des entrees de test
	        	s.executeUpdate("insert into CLIENTS values ('5', 'Léa', '000000001')");
	        	s.executeUpdate("insert into CLIENTS values ('6', 'Paul', '000000002')");
	        }
	        
	        // On regarde si la table existe deja
	       query = "select numS from STATIONS limit 1";
	        try {
	        	s.executeQuery(query);
	        } catch(Exception e) {
	        	// sinon on l'a cree
	        	s.execute("create table STATIONS  ( " +
	        			" numS VARCHAR( 256 ) NOT NULL PRIMARY KEY, " +
	        			" longitude NUMERIC , " +
	        			" latitude NUMERIC , " +
	        			" capacite INTEGER)");
	        	
	        	// on ajoute des entrees de test
	        	s.executeUpdate("insert into STATIONS values ('1', 0.6, 0.3, 10)");
	        	s.executeUpdate("insert into STATIONS values ('2', 0.8, 0.9, 15)");
	        }
		} catch(Exception e) {
			// il y a eu une erreur
			e.printStackTrace();
		}
	}
	
	public void creerStation(int numS, double longitude, double latitude, int capacite) throws RemoteException{
		try{
			Statement s = conn.createStatement();
			//s.executeUpdate("insert into STATIONS values ('"+ +"', 0.6, 0.3, 10)");
		}
		catch(SQLException e){
			
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
			ResultSet rs = s.executeQuery("select nomC from CLIENTS where numC = '5'");
	        if (rs.next()) {
	        	String nom = rs.getString("nomC");
	        	System.out.println(nom);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("select numS from STATIONS");
			
	        while (rs.next()) {
	        	String num = rs.getString("numS");
	        	System.out.println(num);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getClientMotDePasse() {
        Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100000000);
		System.out.println("Generated : " + randomInt);
		return randomInt;
	}




}
