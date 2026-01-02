package Controller;

import Model.Agent;
import View.InterfaceConnexion;
import View.InterfaceRecherche;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControleurConnexion implements ActionListener {

    private JTextField usernameField;
    private JTextField idField;
    private InterfaceConnexion connexionView;
    private Agent modele;

    public ControleurConnexion(JTextField usernameField, JTextField idField, InterfaceConnexion connexionView, Agent modele) {
        this.usernameField = usernameField;
        this.idField = idField;
        this.connexionView = connexionView;
        this.modele = modele;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("Connexion".equals(command)) {
            String username = usernameField.getText().trim(); // Prénom Nom
            String idText = idField.getText().trim(); // ID saisi

            System.out.println("Nom saisi : " + username);
            System.out.println("ID saisi : " + idText);

            // Validation des champs
            if (username.isEmpty() || idText.isEmpty()) {
                JOptionPane.showMessageDialog(connexionView, "Veuillez remplir tous les champs !");
                return;
            }

            // Conversion de l'ID en entier
            int id;
            try {
                id = Integer.parseInt(idText); // Convertir l'ID en entier
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(connexionView, "L'identifiant doit être un nombre valide !");
                return;
            }

            // Vérification de l'authenticité de l'agent
            if (modele.verifierAgent(username, id)) {
                JOptionPane.showMessageDialog(connexionView, "Connexion réussie !");
                System.out.println("Navigation vers InterfaceRecherche...");
                new InterfaceRecherche().setVisible(true);
                connexionView.dispose(); // Fermer la fenêtre de connexion
            } else {
                JOptionPane.showMessageDialog(connexionView, "Nom d'utilisateur ou ID incorrect !");
            }
        }
    }
}
