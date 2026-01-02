package Controller;

import Model.Agent;
import Model.Bien;
import Model.Client;
import Model.Visite;
import View.GestionVisite;
import View.InterfaceRecherche;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

/**
 * Classe contrôleur pour gérer les interactions entre la vue et le modèle pour les visites.
 */
public class ControleurVisite implements ActionListener {

    // Attributs : Objets graphiques nécessaires
    private JTextField dateVisiteField;
    private JTextField heureVisiteField;
    private JTextField commentaireField;
    private JTextField idClientField;
    private JTextField idBienField;
    private JTextField idAgentField;
    private JTextField champRecherche; // Champ dédié pour la recherche
    private JButton ajouterButton;
    private JButton rechercherButton;
    private JButton reinitialiserButton;
    private JButton homeButton; // Bouton "Accueil"

    private Visite modele; // Attribut pour le modèle
    private GestionVisite vue; // Référence à la vue

   
    public ControleurVisite(JTextField dateVisiteField, JTextField heureVisiteField, JTextField commentaireField,
                            JTextField idClientField, JTextField idBienField, JTextField idAgentField,
                            JTextField champRecherche, JButton ajouterButton, JButton rechercherButton,
                            JButton reinitialiserButton, JButton homeButton, Visite modele, GestionVisite vue) {
        this.dateVisiteField = dateVisiteField;
        this.heureVisiteField = heureVisiteField;
        this.commentaireField = commentaireField;
        this.idClientField = idClientField;
        this.idBienField = idBienField;
        this.idAgentField = idAgentField;
        this.champRecherche = champRecherche;
        this.ajouterButton = ajouterButton;
        this.rechercherButton = rechercherButton;
        this.reinitialiserButton = reinitialiserButton;
        this.homeButton = homeButton; // Stocker le bouton "Accueil"
        this.modele = modele; // Stocker l'instance de Visite
        this.vue = vue; // Stocker la référence à la vue
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == ajouterButton) {
            ajouterVisite();
        } else if (source == rechercherButton) {
            rechercherVisite();
        } else if (source == reinitialiserButton) {
            reinitialiserListe();
        } else if (source == homeButton) {
            // Gérer l'action du bouton "Accueil"
            InterfaceRecherche interfaceRecherche = new InterfaceRecherche();
            interfaceRecherche.setVisible(true);
            ((JFrame) SwingUtilities.getWindowAncestor(homeButton)).dispose(); // Fermer la fenêtre actuelle
        } else if (source instanceof JButton) {
            JButton button = (JButton) source;

            // Vérifier si le bouton a un ActionCommand (ID de la visite)
            String idVisite = button.getActionCommand();
            if (idVisite != null) {
                supprimerVisite(idVisite); // Appeler la méthode pour supprimer la visite
            }
        }
    }

    // Méthode pour ajouter une visite
    private void ajouterVisite() {
        try {
            // Récupérer les données des champs
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dateVisite = dateFormat.parse(dateVisiteField.getText().trim());
            String heureVisite = heureVisiteField.getText().trim();
            String commentaire = commentaireField.getText().trim();
            String idClient = idClientField.getText().trim();
            String idBien = idBienField.getText().trim();
            String idAgent = idAgentField.getText().trim();

            // Vérifier que tous les champs sont remplis
            if (heureVisite.isEmpty() || commentaire.isEmpty() || idClient.isEmpty() || idBien.isEmpty() || idAgent.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs !");
                return;
            }

            // Rechercher les objets associés
            Client client = rechercherClient(idClient);
            Bien bien = rechercherBien(idBien);
            Agent agent = rechercherAgent(idAgent);

            if (client == null || bien == null || agent == null) {
                JOptionPane.showMessageDialog(null, "Erreur : Client, Bien ou Agent introuvable !");
                return;
            }

            // Créer un nouvel objet Visite
            Visite visite = new Visite(null, dateVisite, heureVisite, commentaire, client, bien, agent);

            // Ajouter la visite au modèle
            modele.ajouterVisite(visite);

            // Rafraîchir les cartes dans la vue
            refreshCartes();
            JOptionPane.showMessageDialog(null, "Visite ajoutée avec succès !");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer des valeurs valides pour les champs numériques !");
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer une date valide au format dd/MM/yyyy !");
        }
    }

    // Méthode pour rechercher une visite
    private void rechercherVisite() {
        String recherche = champRecherche.getText().trim(); // Supprimer les espaces
        if (recherche.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un ID pour rechercher !");
            return;
        }

        // Rechercher la visite correspondant à l'ID
        Visite visite = modele.rechercherVisite(recherche); // Utiliser le modèle pour rechercher la visite
        if (visite != null) {
            // Notifier la vue pour afficher uniquement la visite trouvée
            List<Visite> resultats = List.of(visite); // Créer une liste contenant uniquement la visite trouvée
            vue.afficherCartes(resultats); // Appeler une méthode de la vue pour afficher les résultats
        } else {
            JOptionPane.showMessageDialog(null, "Aucune visite trouvée avec l'ID : " + recherche);
        }
    }

    // Méthode pour réinitialiser la liste
    private void reinitialiserListe() {
        champRecherche.setText(""); // Effacer le champ de recherche
        vue.afficherCartes(); // Recharger toutes les visites
    }

    // Méthode pour rafraîchir les cartes
    public void refreshCartes() {
        // Récupérer toutes les visites depuis le modèle
        List<Visite> visites = modele.getToutesLesVisites(); // Utiliser le modèle pour obtenir les visites

        // Notifier la vue pour afficher les cartes mises à jour
        vue.afficherCartes(visites); // Appeler la méthode de la vue pour afficher les cartes
    }

    // Méthode pour supprimer une visite
    public void supprimerVisite(String idVisite) {
        // Supprimer la visite du modèle
        boolean success = modele.supprimerVisite(idVisite);
        if (success) {
            JOptionPane.showMessageDialog(null, "Visite supprimée avec succès !");
            // Notifier la vue pour rafraîchir les cartes
            vue.afficherCartes();
        } else {
            JOptionPane.showMessageDialog(null, "Erreur : Visite non trouvée !");
        }
    }

    // Méthodes pour rechercher les objets associés
    private Client rechercherClient(String idClient) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(System.getProperty("user.home") + "/clients.dat"))) {
            List<Client> clients = (List<Client>) ois.readObject();
            return clients.stream()
                    .filter(client -> String.valueOf(client.getIdPersonne()).equals(idClient))
                    .findFirst()
                    .orElse(null);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la recherche du client : " + e.getMessage());
            return null;
        }
    }

    private Bien rechercherBien(String idBien) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(System.getProperty("user.home") + "/biens.dat"))) {
            List<Bien> biens = (List<Bien>) ois.readObject();
            return biens.stream()
                    .filter(bien -> String.valueOf(bien.getIdBien()).equals(idBien))
                    .findFirst()
                    .orElse(null);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la recherche du bien : " + e.getMessage());
            return null;
        }
    }

    private Agent rechercherAgent(String idAgent) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(System.getProperty("user.home") + "/agents.dat"))) {
            List<Agent> agents = (List<Agent>) ois.readObject();
            return agents.stream()
                    .filter(agent -> String.valueOf(agent.getIdPersonne()).equals(idAgent))
                    .findFirst()
                    .orElse(null);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la recherche de l'agent : " + e.getMessage());
            return null;
        }
    }
}
