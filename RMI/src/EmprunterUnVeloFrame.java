import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

import java.awt.Font;
import java.awt.TextField;
import java.awt.Color;
import javax.swing.SwingConstants;

public class EmprunterUnVeloFrame extends JFrame {

	private JPanel contentPane;
	private JTextField tbxIdentifiant;
	private JButton btnValider;
	private JButton btnNewButton;
	private JLabel lblOuInscrivezvous;
	private JButton btnSinscrire;
	private static GestionStation proxy;
	private static Station station;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmprunterUnVeloFrame frame = new EmprunterUnVeloFrame(station, proxy);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	public EmprunterUnVeloFrame(Station s, GestionStation leProxy) throws MalformedURLException, RemoteException, NotBoundException {
		//Police des pop-up
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(
	              "Tahoma", Font.PLAIN, 18)));
		//proxy = (GestionStation) Naming.lookup("rmi://localhost:1099/Gestionnaire");
		this.proxy = leProxy;
		
		this.station = s;
		setTitle("Vélo Toulouse - Emprunter un vélo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 583, 393);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPourEmprunterUn = new JLabel("Pour emprunter un vélo, veuillez saisir les informations suivantes :");
		lblPourEmprunterUn.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPourEmprunterUn.setBounds(15, 59, 536, 35);
		contentPane.add(lblPourEmprunterUn);

		JLabel lblNumroUtilisateur = new JLabel("Numéro utilisateur :");
		lblNumroUtilisateur.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNumroUtilisateur.setBounds(87, 99, 200, 28);
		contentPane.add(lblNumroUtilisateur);

		JLabel lblMotDePasse = new JLabel("Mot de passe :");
		lblMotDePasse.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMotDePasse.setBounds(87, 138, 142, 28);
		contentPane.add(lblMotDePasse);

		tbxIdentifiant = new JTextField();
		tbxIdentifiant.setFont(new Font("Tahoma", Font.PLAIN, 18));
		tbxIdentifiant.setBounds(248, 99, 135, 28);
		contentPane.add(tbxIdentifiant);
		tbxIdentifiant.setColumns(10);


		final JLabel test = new JLabel("");
		test.setForeground(Color.RED);
		test.setFont(new Font("Tahoma", Font.ITALIC, 18));
		test.setBounds(11, 228, 540, 46);
		contentPane.add(test);
		

		final TextField txtMdp = new TextField();
		txtMdp.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtMdp.setEchoCharacter('*');
		txtMdp.setBounds(248, 139, 135, 27);
		contentPane.add(txtMdp);

		btnValider = new JButton("Valider");
		btnValider.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!tbxIdentifiant.getText().equals("") && !txtMdp.getText().equals("")){
					try {
						if (proxy.authentificationClient(tbxIdentifiant.getText(), txtMdp.getText())){
							Velo velo = station.rechercherVeloLibre();
							if(velo != null) {
								System.out.println("Vous pouvez prendre le vélo "+ velo.getNumV());
								try {
									if (proxy.emprunterVelo(tbxIdentifiant.getText(), velo.getNumV())) {
										station.setLesVelos(proxy.majCacheStation(station.getNumS()));
										int choix1 = JOptionPane.showConfirmDialog(btnValider, "Vous pouvez emprunter le vélo " + velo.getNumV() + ".\nBonne journée !", "Vélo Toulouse - Message", JOptionPane.CLOSED_OPTION);
										
										if(choix1 == JOptionPane.OK_OPTION){
											StationFrame sf;
											try {
												sf = new StationFrame();
												setVisible(false);
												sf.setVisible(true);
											} catch (MalformedURLException | RemoteException
													| NotBoundException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
										}
									} else {
										if(proxy.getVeloClient(tbxIdentifiant.getText()) != null) {
											int choix1 = JOptionPane.showConfirmDialog(btnValider, "Vous empruntez déjà le vélo " + ((Velo)proxy.getVeloClient(tbxIdentifiant.getText())).getNumV() + ".", "Vélo Toulouse - Message", JOptionPane.CLOSED_OPTION);
										}
										if (proxy.rechercherVelo(velo.getNumV()) == null){
											int choix1 = JOptionPane.showConfirmDialog(btnValider, "Erreur lors de la recherche du vélo.", "Vélo Toulouse - Message", JOptionPane.CLOSED_OPTION);
										}
									}
								} catch (RemoteException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							} else {
								try {
									if (proxy.getVeloClient(tbxIdentifiant.getText()) == null) {
										if (proxy.rechercherStationPlusProche(station.getNumS()) != null){
											Station stationPlusProche = proxy.rechercherStationPlusProche(station.getNumS());
											int choix1 = JOptionPane.showConfirmDialog(btnValider, "La station la plus proche ayant des vélos disponibles est la numéro " + stationPlusProche.getNumS() + ". Il reste " + stationPlusProche.getNbVeloLibre() + " vélos.", "Vélo Toulouse - Message", JOptionPane.CLOSED_OPTION);
										}
									} else {
										test.setText("Vous ne pouvez pas emprunter un vélo.");
									}
									
								} catch (RemoteException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}
						else{
							test.setText("Echec lors de la connexion.");
						}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					test.setText("Veuillez saisir votre identifiant et votre mot de passe.");
				}
				//si numC et mdpC OK
				//s'il y a des vélos disponibles dans la station

				//sinon plus de vélo dispo
				//int choix2 = JOptionPane.showConfirmDialog(btnValider, "Nous sommes désolé.\nIl n'y a plus de vélo disponible\nLa station la plus proche est la station 'numS'", "Vélo Toulouse - Message", JOptionPane.CLOSED_OPTION);
				/*
				 * if(choix2 == JOptionPane.OK_OPTION){
						StationFrame sf = new StationFrame();
						setVisible(false);
						sf.setVisible(true);
						}
				 */
				//sinon numéro et/ou mdp incorrect
				//JOptionPane.showConfirmDialog(btnValider, "Numéro ou mot de passe incorrect.\nRecommencez.", "Vélo Toulouse - Message", JOptionPane.CLOSED_OPTION);						 
			}
		});
		btnValider.setBounds(164, 177, 98, 35);
		contentPane.add(btnValider);

		btnNewButton = new JButton("Quitter");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StationFrame sf;
				try {
					sf = new StationFrame();
					setVisible(false);
					sf.setVisible(true);
				} catch (MalformedURLException | RemoteException
						| NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(310, 177, 98, 35);
		contentPane.add(btnNewButton);

		lblOuInscrivezvous = new JLabel("Ou, inscrivez-vous :");
		lblOuInscrivezvous.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblOuInscrivezvous.setBounds(11, 293, 200, 28);
		contentPane.add(lblOuInscrivezvous);

		btnSinscrire = new JButton("S'inscrire");
		btnSinscrire.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnSinscrire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InscriptionFrame ins;
				try {
					ins = new InscriptionFrame();
					setVisible(false);
					ins.setVisible(true);
				} catch (MalformedURLException | RemoteException
						| NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSinscrire.setBounds(172, 290, 115, 31);
		contentPane.add(btnSinscrire);
		
		JLabel lblConnexion = new JLabel("Connexion");
		lblConnexion.setHorizontalAlignment(SwingConstants.CENTER);
		lblConnexion.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblConnexion.setBounds(-42, 0, 632, 37);
		contentPane.add(lblConnexion);
		
	}
}
