import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
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
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;


public class StationFrame extends JFrame {

	private static GestionStation proxy;
	private JPanel contentPane;
	private static Station station;
	private static String numS;
	private static double lon;
	private static double lat;
	private static int cap;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		numS = args[0];
		lon = Double.parseDouble(args[1]);
		lat = Double.parseDouble(args[2]);
		cap = Integer.parseInt(args[3]);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StationFrame frame = new StationFrame();
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
	public StationFrame() throws MalformedURLException, RemoteException, NotBoundException {
		//Police des pop-up
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(
				"Tahoma", Font.PLAIN, 18)));
		proxy = (GestionStation) Naming.lookup("rmi://localhost:1099/Gestionnaire");
		station = new Station(numS, lon, lat, cap);
		if (proxy.rechercherStation(numS) == null) {
			proxy.creerStation(numS,  lon, lat,  cap);
		}
		station.setLesVelos(proxy.majCacheStation(station.getNumS()));

		setTitle("Vélo Toulouse - Accueil");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 617, 366);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblBonjourBienvenue = new JLabel("Bonjour, bienvenue à la station " + station.getNumS() + " !");
		lblBonjourBienvenue.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblBonjourBienvenue.setBounds(10, 11, 593, 47);
		contentPane.add(lblBonjourBienvenue);

		JLabel lblCapacitDeLa = new JLabel("Capacité de la station : " + station.getCapacite());
		lblCapacitDeLa.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblCapacitDeLa.setBounds(66, 74, 315, 29);
		contentPane.add(lblCapacitDeLa);

		JLabel lblNombreDeVlos = new JLabel("Nombre de vélos disponibles : " + station.getNbVeloLibre());
		lblNombreDeVlos.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNombreDeVlos.setBounds(66, 104, 414, 29);
		contentPane.add(lblNombreDeVlos);

		JButton btnEmprunterUnVlo = new JButton("Emprunter un vélo");
		btnEmprunterUnVlo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnEmprunterUnVlo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EmprunterUnVeloFrame ev;
				try {
					ev = new EmprunterUnVeloFrame(station, proxy);
					setVisible(false);
					ev.setVisible(true);
				} catch (MalformedURLException | RemoteException
						| NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnEmprunterUnVlo.setBounds(200, 152, 181, 34);
		contentPane.add(btnEmprunterUnVlo);

		JButton btnRendreUnVlo = new JButton("Rendre un vélo");
		btnRendreUnVlo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnRendreUnVlo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RendreUnVeloFrame rv;
				try {
					rv = new RendreUnVeloFrame(station, proxy);
					setVisible(false);
					rv.setVisible(true);
				} catch (MalformedURLException | RemoteException
						| NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnRendreUnVlo.setBounds(200, 197, 181, 34);
		contentPane.add(btnRendreUnVlo);

		JButton btnAdministration = new JButton("Administration");
		btnAdministration.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnAdministration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MenuClientAdmin fr = new MenuClientAdmin();
					fr.setVisible(true);
					setVisible(false);
				} catch (MalformedURLException | RemoteException
						| NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnAdministration.setBounds(10, 262, 199, 34);
		contentPane.add(btnAdministration);
		
		final JButton btnQuitter = new JButton("Quitter");
		btnQuitter.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quitter();
			}
		});
		btnQuitter.setBounds(465, 262, 115, 32);
		contentPane.add(btnQuitter);
	}
	public void quitter(){
		int i=JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment quitter l'application ?","Quitter",JOptionPane.YES_NO_OPTION);
		switch(i)
		 {
		  case 0 :
		   System.exit(0);
		  case 1 :
		 }
	}
}
