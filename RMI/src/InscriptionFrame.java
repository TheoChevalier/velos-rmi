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

public class InscriptionFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtNom;
	private static GestionStation proxy;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InscriptionFrame frame = new InscriptionFrame();
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
	public InscriptionFrame() throws MalformedURLException, RemoteException, NotBoundException {
		//Police des pop-up
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font(
				"Tahoma", Font.PLAIN, 18)));
		proxy = (GestionStation) Naming.lookup("rmi://localhost:1099/Gestionnaire");

		setTitle("Vélo Toulouse - Inscription");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 525, 323);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblPourVousInscrire = new JLabel("Pour vous inscrire, veuillez saisir les informations suivantes :");
		lblPourVousInscrire.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblPourVousInscrire.setBounds(15, 71, 492, 24);
		contentPane.add(lblPourVousInscrire);

		JLabel lblNomUtilisateur = new JLabel("Nom utilisateur :");
		lblNomUtilisateur.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNomUtilisateur.setBounds(25, 119, 151, 24);
		contentPane.add(lblNomUtilisateur);

		txtNom = new JTextField();
		txtNom.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtNom.setBounds(175, 116, 123, 30);
		contentPane.add(txtNom);
		txtNom.setColumns(10);

		final JLabel lblMsg = new JLabel("");
		lblMsg.setHorizontalAlignment(SwingConstants.CENTER);
		lblMsg.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMsg.setBounds(46, 214, 363, 37);
		contentPane.add(lblMsg);

		final JButton btnValider = new JButton("Valider");
		btnValider.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!txtNom.getText().equals("")){
					String[] retour;
					try {
						retour = proxy.creerClient(txtNom.getText());
						int choix = JOptionPane.showConfirmDialog(btnValider, txtNom.getText() + ", votre identifiant est : " + retour[0] + "\nVotre mot de passe est : " + retour[1] + "\n", "Vélo Toulouse - Inscription", JOptionPane.CLOSED_OPTION);
						if(choix == JOptionPane.OK_OPTION){
							setVisible(false);
						}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					lblMsg.setText("Veuillez saisir votre nom pour vous inscrire.");
				}
			}

		});
		btnValider.setBounds(82, 171, 107, 39);
		contentPane.add(btnValider);

		JButton btnAnnuler = new JButton("Annuler");
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
		btnAnnuler.setBounds(236, 171, 107, 39);
		contentPane.add(btnAnnuler);
		
		JLabel lblInscription = new JLabel("Inscription");
		lblInscription.setHorizontalAlignment(SwingConstants.CENTER);
		lblInscription.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblInscription.setBounds(-84, 18, 632, 37);
		contentPane.add(lblInscription);


	}
}
