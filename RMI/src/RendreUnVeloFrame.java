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

import javax.swing.SwingConstants;

public class RendreUnVeloFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtNumv;
	private JButton btnValider;
	private JButton btnAnnuler;
	private static GestionStation proxy;
	private static Station station;
	private JLabel lblRendreUnVlo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RendreUnVeloFrame frame = new RendreUnVeloFrame(station, proxy);
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
	public RendreUnVeloFrame(Station s, GestionStation leProxy) throws MalformedURLException, RemoteException, NotBoundException {
		//Police des pop-up
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(
				"Tahoma", Font.PLAIN, 18)));
		//proxy = (GestionStation) Naming.lookup("rmi://localhost:1099/Gestionnaire");
		this.proxy = leProxy;

		this.station = s;
		setTitle("Vélo Toulouse - Rendre un vélo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 537, 319);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblEntrerLeNumro = new JLabel("Entrer le numéro du vélo à rendre :");
		lblEntrerLeNumro.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblEntrerLeNumro.setBounds(60, 87, 286, 28);
		contentPane.add(lblEntrerLeNumro);

		txtNumv = new JTextField();
		txtNumv.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtNumv.setBounds(361, 87, 74, 28);
		contentPane.add(txtNumv);
		txtNumv.setColumns(10);

		btnValider = new JButton("Valider");
		btnValider.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//si la station a des places disponibles
				if (!txtNumv.getText().equals("")){
					if(station.getNbPlacesLibres()>0){
						try {
							if(proxy.rendreVelo(station.getNumS(), txtNumv.getText())){
								station.setLesVelos(proxy.majCacheStation(station.getNumS()));
								JOptionPane.showMessageDialog(btnValider, "Vous avez bien rendu le vélo " + txtNumv.getText() + ".");

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
							} else {
								if(proxy.rechercherVelo(txtNumv.getText()) == null){
									JOptionPane.showMessageDialog(btnValider, "Vous ne pouvez pas rendre le vélo " + txtNumv.getText() + ", il n'existe pas.", "Message d'erreur", JOptionPane.ERROR_MESSAGE);
								} else {
									Velo v = proxy.rechercherVelo(txtNumv.getText());
									if (v.getClient() == null) {
										JOptionPane.showMessageDialog(btnValider, "Erreur, le vélo " + txtNumv.getText() + " n'est pas emprunté.", "Message d'erreur", JOptionPane.ERROR_MESSAGE);
									}
								}
								if(proxy.rechercherStation(station.getNumS()) == null) {
									JOptionPane.showMessageDialog(btnValider, "Erreur, la station " + station.getNumS() + " n'existe pas.", "Message d'erreur", JOptionPane.ERROR_MESSAGE);
								}
							}
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						System.out.println("La station n'a plus de place disponible.");
						try {
							if (proxy.rechercherStationPlusProcheDepot(station.getNumS()) != null){
								Station stationPlusProche = proxy.rechercherStationPlusProcheDepot(station.getNumS());
								JOptionPane.showMessageDialog(btnValider, "La station la plus proche ayant des places disponibles est la numéro " + stationPlusProche.getNumS() + ".");

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
							} else {
								JOptionPane.showMessageDialog(btnValider, "Nous nous excusons, il n'y a plus de station libre.");

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
						} catch (RemoteException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}else{
					JOptionPane.showMessageDialog(btnValider, "Veuillez saisir le numéro du vélo.");
				}
			}
		});
		btnValider.setBounds(136, 147, 112, 44);
		contentPane.add(btnValider);

		btnAnnuler = new JButton("Annuler");
		btnAnnuler.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnAnnuler.addActionListener(new ActionListener() {
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
		btnAnnuler.setBounds(278, 147, 118, 44);
		contentPane.add(btnAnnuler);

		lblRendreUnVlo = new JLabel("Rendre un vélo");
		lblRendreUnVlo.setHorizontalAlignment(SwingConstants.CENTER);
		lblRendreUnVlo.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblRendreUnVlo.setBounds(-55, 16, 632, 37);
		contentPane.add(lblRendreUnVlo);
	}

}
