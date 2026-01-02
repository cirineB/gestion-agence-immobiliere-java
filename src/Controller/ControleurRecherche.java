package Controller;

import View.*;
import Model.Bien; // Ensure the Bien class is in the Model package or adjust the import path accordingly
import Model.Client;
import Model.Contrat;
import Model.Visite;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControleurRecherche implements ActionListener {

    // Attributs : Objets graphiques nécessaires
    private JButton gestionBienButton;
    private JButton gestionClientButton;
    private JButton gestionAgentButton;
    private JButton gestionContratButton;
    private JButton gestionVisiteButton;
    private JButton deconnexionButton;

    private JFrame currentFrame; // Référence à la fenêtre actuelle

    public ControleurRecherche(JButton gestionBienButton, JButton gestionClientButton, JButton gestionAgentButton,
                               JButton gestionContratButton, JButton gestionVisiteButton, JButton deconnexionButton,
                               JFrame currentFrame) {
        this.gestionBienButton = gestionBienButton;
        this.gestionClientButton = gestionClientButton;
        this.gestionAgentButton = gestionAgentButton;
        this.gestionContratButton = gestionContratButton;
        this.gestionVisiteButton = gestionVisiteButton;
        this.deconnexionButton = deconnexionButton;
        this.currentFrame = currentFrame; // Stocker la fenêtre actuelle
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == gestionBienButton) {
            // Gérer l'action du bouton "Gestion des biens"
            System.out.println("Navigation vers GestionBien...");
            GestionBien gestionBien = new GestionBien(new Bien(System.getProperty("user.home") + "/biens.dat")); // Passez une instance valide de Bien
            gestionBien.setVisible(true);
            if (currentFrame != null) {
                currentFrame.dispose(); // Ferme la fenêtre actuelle
            }
        } else if (source == gestionClientButton) {
            // Gérer l'action du bouton "Gestion des clients"
            System.out.println("Navigation vers GestionClient...");
            GestionClient gestionClient = new GestionClient(new Client(System.getProperty("user.home") + "/clients.dat")); // Passez une instance valide de Client
            gestionClient.setVisible(true);
            if (currentFrame != null) {
                currentFrame.dispose(); // Ferme la fenêtre actuelle
            }
        } else if (source == gestionAgentButton) {
            // Gérer l'action du bouton "Gestion des agents"
            System.out.println("Navigation vers GestionAgent...");
            GestionAgent gestionAgent = new GestionAgent(null); // Passez une instance valide si nécessaire
            gestionAgent.setVisible(true);
            if (currentFrame != null) {
                currentFrame.dispose(); // Ferme la fenêtre actuelle
            }
        } else if (source == gestionContratButton) {
            // Gérer l'action du bouton "Gestion des contrats"
            System.out.println("Navigation vers GestionContrat...");
            GestionContrat gestionContrat = new GestionContrat(new Contrat(System.getProperty("user.home") + "/contrats.dat")); // Passez une instance valide si nécessaire
            gestionContrat.setVisible(true);
            if (currentFrame != null) {
                currentFrame.dispose(); // Ferme la fenêtre actuelle
            }
        } else if (source == gestionVisiteButton) {
            // Gérer l'action du bouton "Gestion des visites"
            System.out.println("Navigation vers GestionVisite...");
            GestionVisite gestionVisite = new GestionVisite(new Visite(System.getProperty("user.home") + "/visites.dat"));// Passez une instance valide si nécessaire
            gestionVisite.setVisible(true);
            if (currentFrame != null) {
                currentFrame.dispose(); // Ferme la fenêtre actuelle
            }
        } else if (source == deconnexionButton) {
            // Gérer l'action du bouton "Déconnexion"
            int confirmation = JOptionPane.showConfirmDialog(
                currentFrame,
                "Voulez-vous vraiment vous déconnecter ?",
                "Confirmation de déconnexion",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                System.out.println("Déconnexion confirmée. Retour à InterfaceConnexion...");
                InterfaceConnexion interfaceConnexion = new InterfaceConnexion(null);
                interfaceConnexion.setVisible(true); // Ouvre la fenêtre InterfaceConnexion
                if (currentFrame != null) {
                    currentFrame.dispose(); // Ferme la fenêtre actuelle
                }
            }
        }
    }
}
