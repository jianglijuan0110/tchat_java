import java.util.List;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileReader;

/** Classe implémentant le bouton de connexion au tchat */
class BoutonConnexion extends JButton implements MouseListener {

	private String nomBouton;
	private JTextField champNom;
	private JTextField champIP;
	private JTextField champPort;
	private JTextArea champConnectes;
	private JTextArea champDiscussion;
	private List<Client> listeClients;
	private JButton envoyer, deconnexion;
	private Fenetre fenetre;
	
	/** Constructeur d'un bouton de connexion */
	public BoutonConnexion(JTextField nom, JTextField ip, JTextField port, JTextArea connectes, JTextArea discussion, JButton b1, JButton b2, Fenetre f){
		super("Connexion");
		nomBouton = "Connexion";
		champNom = nom;
		champIP = ip;
		champPort = port;
		champConnectes = connectes;
		champDiscussion = discussion;
		listeClients = new LinkedList<Client>();
		envoyer = b1;
		deconnexion = b2;
		fenetre = f;
		/* Grâce à cette instruction, notre objet va s'écouter. Dès qu'un événement de la souris sera intercepté, il en sera averti */
		this.addMouseListener(this);
	}

	// Méthode appelée lors du clic de souris
	public void mouseClicked(MouseEvent event) {
	
		/* Chargement (désérialisation) de la liste des clients */
		ObjectInputStream fichierClientsInput = null; // ObjectInputStream : fichier comportant des objets	
		try {
			fichierClientsInput = new ObjectInputStream(new FileInputStream("ListeClients.txt")); // Ouverture du fichier de clients en mode lecture
			Client c;
			listeClients = new LinkedList<Client>(); // La liste est clients est réinitialisée pour pouvoir être chargée à partir du fichier
			while ((c = (Client)(fichierClientsInput.readObject())) != null) // Ajout de chaque client du fichier à la liste des clients
				listeClients.add(c);
		
		} catch (EOFException e) { // Cette exception est levée lorsque la fin du fichier est atteinte
			System.out.println("Liste des clients chargée.");
		} catch (ClassNotFoundException e) { // Cette exception est levée si l'objet désérialisé n'a pas de classe connue
			e.printStackTrace();
		} catch (FileNotFoundException e) { // Cette exception est levée si l'objet ObjectInputStream ne trouve aucun fichier
			e.printStackTrace();
		} catch (IOException e) { // Celle-ci se produit lors d'une erreur d'écriture ou de lecture
			e.printStackTrace();
		} finally {
			// On ferme nos flux de données dans un bloc finally pour s'assurer que ces instructions seront exécutées dans tous les cas même si une exception est levée !
			try {
				if (fichierClientsInput != null)
				fichierClientsInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/* Ajout d'un client à une liste de clients */
		try {
			Client c = new Client(champNom.getText(), champIP.getText(), Integer.parseInt(champPort.getText()));
			if (c.estConnecte()) {// Si le client n'a pas eu de problèmes de connexion
				listeClients.add(c);
				((BoutonDeconnexion)deconnexion).setClient(c);
				((BoutonEnvoyer)envoyer).setClient(c);
				fenetre.changerDeconnexion(); // Le bouton de connexion devient un bouton de déconnexion 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/* Sauvegarde (sérialisation) de tous les clients dans le fichier client */
		ObjectOutputStream fichierClientsOutput = null;
			
		try {
			fichierClientsOutput = new ObjectOutputStream(new FileOutputStream("ListeClients.txt")); // Ouverture du fichier de clients en mode écriture
			champConnectes.setText("");
			for (Client c : listeClients) {// Écriture dans le fichier de tous les clients
				champConnectes.setText(champConnectes.getText() + c.getNom() + "\n");
				fichierClientsOutput.writeObject(c);
			}
			
		} catch (FileNotFoundException e) { // Cette exception est levée si l'objet FileInputStream ne trouve aucun fichier
			e.printStackTrace();
		} catch (IOException e) { // Celle-ci se produit lors d'une erreur d'écriture ou de lecture
			e.printStackTrace();
		} finally {
			// On ferme nos flux de données dans un bloc finally pour s'assurer que ces instructions seront exécutées dans tous les cas même si une exception est levée !
			try {
				if (fichierClientsOutput != null)
				fichierClientsOutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/* Actualisation du champ discussion */
		String str = "";
		try {
			//Création de l'objet
			FileReader fr = new FileReader("HistoriqueMessages.txt");
			int i = 0;
			//Lecture des données
			while((i = fr.read()) != -1)
				str += (char)i;
			//On ferme le flux
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		champDiscussion.setText(str);
		
	}
	// Méthode appelée lors du survol de la souris
	public void mouseEntered(MouseEvent event) { }
	// Méthode appelée lorsque la souris sort de la zone du bouton
	public void mouseExited(MouseEvent event) { }
	// Méthode appelée lorsque l'on presse le bouton gauche de la souris
	public void mousePressed(MouseEvent event) { }
	// Méthode appelée lorsque l'on relâche le clic de souris
	public void mouseReleased(MouseEvent event) { }
}
