import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JRadioButton;
import javax.swing.JComboBox;

public class MenuClientAdmin extends JFrame {

	private JPanel contentPane;
	private JTextField txtNumv;
	private JTextField txtNumVEtat;
	private JTextField numStation;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuClientAdmin frame = new MenuClientAdmin();
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
	public MenuClientAdmin() throws MalformedURLException, RemoteException, NotBoundException {
		final GestionStation proxy = (GestionStation) Naming.lookup("rmi://localhost:1099/Gestionnaire");
		
		setTitle("Vélo Toulouse - Administration");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 679, 842);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPanneauDadministration = new JLabel("Panneau d'administration");
		lblPanneauDadministration.setBounds(5, 5, 632, 37);
		lblPanneauDadministration.setHorizontalAlignment(SwingConstants.CENTER);
		lblPanneauDadministration.setFont(new Font("Tahoma", Font.BOLD, 30));
		contentPane.add(lblPanneauDadministration);

		JLabel lblRechercherLtatDun = new JLabel("Rechercher l'état d'un vélo");
		lblRechercherLtatDun.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblRechercherLtatDun.setBounds(15, 100, 423, 37);
		contentPane.add(lblRechercherLtatDun);

		JLabel lblSaisirLeNumro = new JLabel("Saisir le numéro du vélo :");
		lblSaisirLeNumro.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSaisirLeNumro.setBounds(25, 153, 234, 30);
		contentPane.add(lblSaisirLeNumro);

		txtNumv = new JTextField();
		txtNumv.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtNumv.setBounds(233, 156, 67, 26);
		contentPane.add(txtNumv);
		txtNumv.setColumns(10);

		final JLabel lblMesg = new JLabel("");
		lblMesg.setFont(new Font("Tahoma", Font.ITALIC, 18));
		lblMesg.setBounds(5, 199, 632, 30);
		contentPane.add(lblMesg);

		final JLabel lblStationvelo = new JLabel("");
		lblStationvelo.setFont(new Font("Tahoma", Font.ITALIC, 18));
		lblStationvelo.setBounds(5, 230, 632, 30);
		contentPane.add(lblStationvelo);

		JButton btnRechercher = new JButton("Rechercher");
		btnRechercher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblMesg.setText("");
				lblStationvelo.setText("");
				if (!txtNumv.getText().equals("")){
					Velo v;
					try {
						v = proxy.rechercherVelo(txtNumv.getText());
						if (v != null) {
							lblMesg.setText(v.afficherEtat());
							//station où est garé
							if(v.getEtat().toString().equals("Libre")){
								Station s = proxy.stationDuVelo(txtNumv.getText());
								lblStationvelo.setText("Le vélo " + txtNumv.getText() + " est dans la station " + s.getNumS() + ".");
							}
						} else {
							lblMesg.setText("Ce vélo n'existe pas");
						}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					lblMesg.setText("Veuillez saisir le numéro du vélo");
				}
			}
		});
		btnRechercher.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnRechercher.setBounds(309, 155, 139, 29);
		contentPane.add(btnRechercher);

		String[] comboTypes = { "Opérationnel", "Maintenance" };
		final JComboBox comboBox = new JComboBox(comboTypes);
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 18));
		comboBox.setBounds(309, 329, 165, 27);
		contentPane.add(comboBox);

		JLabel lblModifierLtatDun = new JLabel("Modifier l'état d'un vélo");
		lblModifierLtatDun.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblModifierLtatDun.setBounds(15, 276, 423, 37);
		contentPane.add(lblModifierLtatDun);

		JLabel label = new JLabel("Saisir le numéro du vélo :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label.setBounds(25, 329, 234, 30);
		contentPane.add(label);

		txtNumVEtat = new JTextField();
		txtNumVEtat.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtNumVEtat.setColumns(10);
		txtNumVEtat.setBounds(233, 332, 67, 26);
		contentPane.add(txtNumVEtat);

		final JLabel lblMsgEtat = new JLabel("");
		lblMsgEtat.setFont(new Font("Tahoma", Font.ITALIC, 18));
		lblMsgEtat.setBounds(5, 378, 632, 30);
		contentPane.add(lblMsgEtat);

		JButton btnModifier = new JButton("Modifier");
		btnModifier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!txtNumVEtat.getText().equals("")){
					try {
						Velo v = proxy.rechercherVelo(txtNumVEtat.getText());
						if (v != null) {
							boolean etat = false;
							String valeur = comboBox.getSelectedItem().toString();
							if (valeur.equals("Maintenance")){
								System.out.println("Valeur : " + valeur);
								etat = true;
							}
							if(proxy.modifierEtatVelo(txtNumVEtat.getText(), etat)){
								lblMsgEtat.setText("L'état du vélo " + txtNumVEtat.getText() + " a bien été modifié.");
							} else {
								lblMsgEtat.setText("Échec lors de la modification de l'état du vélo " + txtNumVEtat.getText() + ".");
							}
						} else {
							lblMsgEtat.setText("Ce vélo n'existe pas");
						}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					lblMsgEtat.setText("Veuillez saisir le numéro du vélo");
				}
			}
		});
		btnModifier.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnModifier.setBounds(489, 329, 115, 29);
		contentPane.add(btnModifier);
		
		JButton btnQuitter = new JButton("Quitter");
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
		btnQuitter.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnQuitter.setBounds(527, 726, 110, 32);
		contentPane.add(btnQuitter);
		
		JLabel lblAffecterDesVlos = new JLabel("Affecter des vélos à une station");
		lblAffecterDesVlos.setFont(new Font("Dialog", Font.BOLD, 20));
		lblAffecterDesVlos.setBounds(15, 442, 423, 37);
		contentPane.add(lblAffecterDesVlos);
		
		JLabel lblSaisirLeNumro_1 = new JLabel("Saisir le numéro de la station :");
		lblSaisirLeNumro_1.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblSaisirLeNumro_1.setBounds(25, 491, 292, 30);
		contentPane.add(lblSaisirLeNumro_1);
		
		numStation = new JTextField();
		numStation.setFont(new Font("Dialog", Font.PLAIN, 18));
		numStation.setColumns(10);
		numStation.setBounds(395, 493, 67, 26);
		contentPane.add(numStation);
		
		JLabel lblSaisirLeNombre = new JLabel("Saisir le nombre de vélos à affecter :");
		lblSaisirLeNombre.setFont(new Font("Dialog", Font.PLAIN, 18));
		lblSaisirLeNombre.setBounds(25, 543, 352, 30);
		contentPane.add(lblSaisirLeNombre);
		
		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.PLAIN, 18));
		textField.setColumns(10);
		textField.setBounds(395, 545, 67, 26);
		contentPane.add(textField);
		
		final JLabel labelAffectation = new JLabel("");
		labelAffectation.setFont(new Font("Dialog", Font.ITALIC, 18));
		labelAffectation.setBounds(15, 663, 632, 30);
		contentPane.add(labelAffectation);
		
		JButton btnAffecter = new JButton("Affecter");
		btnAffecter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!numStation.getText().equals("") && !textField.getText().equals("")) {
					try {
						Station s = proxy.rechercherStation(numStation.getText());
						if (s == null) {
							labelAffectation.setText("La station n'existe pas.");
						} else {
							int nb = Integer.parseInt(textField.getText());
							if (nb > 0) {
								if (nb <= s.getNbPlacesLibres()) {
									for(int i = 0; i < nb; i++) {
										int numVelo = proxy.creerVelo(false);
										proxy.affecterVeloStation(((Integer)numVelo).toString(), numStation.getText());
									}
									if (nb > 1) {
										labelAffectation.setText("Les vélos ont été correctement ajoutés.");
									} else {
										labelAffectation.setText("Le vélo a été correctement ajouté.");
									}
								} else {
									labelAffectation.setText("Il n'y a pas assez de places libres.");
								}
							} else {
								labelAffectation.setText("Le nombre d'affectations doit être positif.");
							}
						}
					}
					catch (NumberFormatException e) {
						e.printStackTrace();
						labelAffectation.setText("Le format du nombre d'affectations est incorrect.");
					}
					catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					labelAffectation.setText("Le numéro de station et le nombre d'affectations doivent être renseignés.");
				}
			}
		});
		btnAffecter.setFont(new Font("Dialog", Font.PLAIN, 18));
		btnAffecter.setBounds(489, 593, 115, 29);
		contentPane.add(btnAffecter);
	}
}
