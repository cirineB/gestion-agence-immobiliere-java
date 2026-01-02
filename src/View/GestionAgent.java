package View;

import Controller.ControleurAgent;
import Model.Agent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;   
import java.util.List;

public class GestionAgent extends JFrame {

    private JTextField nomField, prenomField, telField, emailField, adresseField, searchField;
    private JPanel cardContainer; // Conteneur pour les cartes des agents
    private ControleurAgent controleur; // Contrôleur pour gérer les agents
    private Agent modele;

    public GestionAgent(Agent modele) {
        setTitle("Gestion des Agents 🧑‍💼");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Afficher en plein écran
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Initialisation du contrôleur
        controleur = new ControleurAgent();

        // Configuration de l'interface
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0xF6F1E7)); // Beige clair
        add(mainPanel);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(0x4E342E), getWidth(), 0, new Color(0x6D4C41));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 80)); // Hauteur de l'en-tête
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(0x6D4C41))); // Bordure inférieure

        // Conteneur pour le bouton "Accueil"
        JPanel leftPanel = new JPanel(new GridBagLayout()); // Utilise GridBagLayout pour centrer verticalement
        leftPanel.setOpaque(false); // Rendre le panneau transparent

        // Bouton "Accueil"
        JButton homeButton = new JButton("🏠 Accueil");
        homeButton.setFont(new Font("Roboto", Font.BOLD, 16)); // Police Roboto en gras
        homeButton.setForeground(Color.WHITE); // Texte blanc
        homeButton.setBackground(new Color(0x795548)); // Fond marron
        homeButton.setFocusPainted(false); // Supprime le contour de focus
        homeButton.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30)); // Bordures internes pour espacement
        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Curseur en forme de main
        homeButton.setOpaque(true); // Rendre le fond visible
        homeButton.setContentAreaFilled(true); // Remplir le fond
        homeButton.setBorderPainted(false); // Supprime la bordure par défaut
        homeButton.setFocusable(false); // Supprime le focus visuel

        // Ajout d'un ActionListener pour ouvrir InterfaceRecherche
        homeButton.addActionListener(e -> {
            InterfaceRecherche interfaceRecherche = new InterfaceRecherche();
            interfaceRecherche.setVisible(true); // Affiche l'interface InterfaceRecherche
            this.dispose(); // Ferme l'interface actuelle (GestionAgent)
        });

        // Ajout de marges pour pousser le bouton un peu à droite
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 20, 0, 0); // Marges : haut, gauche, bas, droite
        leftPanel.add(homeButton, gbc); // Ajoute le bouton au panneau avec les marges

        // Ajout du panneau à gauche du header
        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Titre au centre
        JLabel titleLabel = new JLabel("Gestion des Agents 💼 ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Section principale
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0)); // Utilisation de BorderLayout
        contentPanel.setBackground(new Color(0xF6F1E7));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Colonne gauche : Formulaire
        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fond dégradé pour le formulaire
                GradientPaint gradient = new GradientPaint(0, 0, new Color(0xF5F5F5), getWidth(), getHeight(), new Color(0xE0E0E0));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0x4E342E), 2), // Bordure marron foncé
                " Ajouter un Agent",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Roboto", Font.BOLD, 16),
                new Color(0x4E342E) // Couleur du titre
        ));

        // Définir une largeur minimale pour le formulaire
        formPanel.setPreferredSize(new Dimension(400, 0)); // Largeur : 400px, Hauteur flexible

        // Déclarez `GridBagConstraints` une seule fois au début
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacement par défaut
        gbc.fill = GridBagConstraints.HORIZONTAL; // Remplissage horizontal

        // Champs du formulaire
        String[] labels = {"Nom", "Prénom", "Téléphone", "Email", "Adresse"};
        JTextField[] fields = new JTextField[labels.length];

        // Modification des champs du formulaire
        for (int i = 0; i < labels.length; i++) {
            // Ajouter le label
            gbc.gridx = 0;
            gbc.gridy = i * 2; // Chaque champ occupe deux lignes (label + champ)
            gbc.gridwidth = 2; // Le label occupe toute la largeur
            gbc.anchor = GridBagConstraints.WEST; // Alignement à gauche
            gbc.insets = new Insets(5, 0, 2, 0); // Espacement au-dessus et en dessous du label

            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Roboto", Font.BOLD, 14)); // Police agrandie
            label.setForeground(new Color(0x4E342E)); // Couleur marron foncé
            formPanel.add(label, gbc);

            // Ajouter le champ de texte
            gbc.gridy = i * 2 + 1; // Ligne suivante pour le champ
            gbc.insets = new Insets(0, 0, 10, 0); // Espacement en dessous du champ
            fields[i] = new JTextField(25);
            formPanel.add(createStyledTextField(fields[i]), gbc);
        }

        // Associer les champs aux variables
        nomField = fields[0];
        prenomField = fields[1];
        telField = fields[2];
        emailField = fields[3];
        adresseField = fields[4];

        // Bouton Ajouter
        gbc.gridx = 0;
        gbc.gridy = labels.length * 2; // Place le bouton après tous les champs
        gbc.gridwidth = 2; // Le bouton occupe toute la largeur
        gbc.anchor = GridBagConstraints.CENTER; // Centrer le bouton
        gbc.insets = new Insets(20, 0, 0, 0); // Ajoute un espacement au-dessus du bouton

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton addButton = new JButton("✅ Ajouter");
        styleButton(addButton, new Color(0x795548)); // Marron
        addButton.addActionListener(e -> ajouterAgent());
        buttonPanel.add(addButton);
        formPanel.add(buttonPanel, gbc);

        // Ajout du formulaire au panneau principal
        contentPanel.add(formPanel, BorderLayout.WEST); // Place le formulaire à gauche

        // Colonne droite : Recherche et liste des agents
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(10, 10));
        rightPanel.setBackground(new Color(0xF6F1E7));

        cardContainer = new JPanel();
        cardContainer.setLayout(new BoxLayout(cardContainer, BoxLayout.Y_AXIS));
        cardContainer.setBackground(new Color(0xF6F1E7)); // Fond beige clair
        cardContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Bordure vide autour des cartes

        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0x4E342E), 2),
                "📋 Liste des Agents",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Roboto", Font.BOLD, 16),
                new Color(0x4E342E)
        ));
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(rightPanel, BorderLayout.CENTER); // Place la liste des agents à droite

        // Ajouter les cartes pour les agents existants
        for (Agent agent : controleur.getTousLesAgents()) {
            ajouterCarteAgent(agent);
        }

        // Ajouter la barre de recherche
        ajouterBarreDeRecherche(rightPanel);

        // Charger les agents existants
        chargerAgentsExistants();
    }

    private void ajouterAgent() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String tel = telField.getText().trim();
        String email = emailField.getText().trim();
        String adresse = adresseField.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || email.isEmpty() || adresse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs !");
            return;
        }

        // Création d'un nouvel agent
        Agent agent = new Agent(0, nom, prenom, tel, email, adresse);
        controleur.ajouterAgent(agent); // Ajout via le contrôleur (sauvegarde incluse)
        ajouterCarteAgent(agent); // Ajout de la carte dans l'interface
        resetForm();
        JOptionPane.showMessageDialog(this, "Agent ajouté avec succès !");
        System.out.println("Agent ajouté : " + agent.getNom() + " - " + agent.getIdPersonne());
    }

    private void resetForm() {
        nomField.setText("");
        prenomField.setText("");
        telField.setText("");
        emailField.setText("");
        adresseField.setText("");
    }

    private void ajouterCarteAgent(Agent agent) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ombre
                g2d.setColor(new Color(0, 0, 0, 50)); // Couleur de l'ombre (noir transparent)
                g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);

                // Fond de la carte
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth() - 10, getHeight() - 10, 20, 20);
            }
        };
        card.setBackground(new Color(0xD7CCC8)); // Fond plus foncé pour les cartes
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Espacement interne
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250)); // Hauteur maximale augmentée
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Conteneur pour le contenu de la carte
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); // Fond transparent

        // Ajouter chaque ligne avec une icône et un texte
        contentPanel.add(createInfoRow("🆔", String.valueOf(agent.getIdPersonne())));
        contentPanel.add(createInfoRow("👤", agent.getPrenom() + " " + agent.getNom()));
        contentPanel.add(createInfoRow("📞", agent.getNumeroTel()));
        contentPanel.add(createInfoRow("✉️", agent.getEmail()));
        contentPanel.add(createInfoRow("🏠", agent.getAdresse()));

        card.add(contentPanel, BorderLayout.CENTER);

        // Boutons Modifier et Supprimer
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false); // Fond transparent

        JButton deleteButton = new JButton("🗑️ Supprimer");
        styleButton(deleteButton, new Color(0x795548)); // Marron pour Supprimer

        // Appeler la méthode du contrôleur pour gérer la suppression
        controleur.ajouterActionSupprimerCarte(deleteButton, card, agent, cardContainer);

        buttonPanel.add(deleteButton);
        card.add(buttonPanel, BorderLayout.SOUTH);

        // Ajout de la carte au conteneur avec un espace entre les cartes
        cardContainer.add(card);
        cardContainer.add(Box.createVerticalStrut(15)); // Espace vertical entre les cartes
        cardContainer.revalidate();
        cardContainer.repaint();
    }
    
    private void ajouterCarteAgent(String prenom, String nom, String tel, String email, String adresse) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Ombre
                g2d.setColor(new Color(0, 0, 0, 50)); // Couleur de l'ombre (noir transparent)
                g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 20, 20);

                // Fond de la carte
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth() - 10, getHeight() - 10, 20, 20);
            }
        };
        card.setBackground(new Color(0xD7CCC8)); // Fond plus foncé pour les cartes
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x4E342E), 1), // Bordure marron foncé
                BorderFactory.createEmptyBorder(15, 15, 15, 15) // Espacement interne
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150)); // Taille maximale
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Contenu de la carte
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); // Fond transparent

        // Ajouter chaque ligne avec une icône et un texte
        contentPanel.add(createInfoRow("👤", prenom + " " + nom));
        contentPanel.add(createInfoRow("📞", tel));
        contentPanel.add(createInfoRow("✉️", email)); // Email à côté de l'icône
        contentPanel.add(createInfoRow("🏠", adresse));

        card.add(contentPanel, BorderLayout.CENTER);

        // Boutons Modifier et Supprimer
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false); // Fond transparent

        JButton deleteButton = new JButton("🗑️ Supprimer");
        styleButton(deleteButton, new Color(0x795548)); // Marron pour Supprimer

        

        buttonPanel.add(deleteButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        // Ajout de la carte au conteneur avec un espace entre les cartes
        cardContainer.add(card);
        cardContainer.add(Box.createVerticalStrut(15)); // Espace vertical entre les cartes
        cardContainer.revalidate();
        cardContainer.repaint();
    }

    private JPanel createInfoRow(String emoji, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false); // Fond transparent

        JLabel emojiLabel = new JLabel(emoji); // Emoji comme label
        emojiLabel.setFont(new Font("Roboto", Font.PLAIN, 16)); // Taille de l'emoji

        JLabel textLabel = new JLabel(text); // Texte associé
        textLabel.setFont(new Font("Roboto", Font.PLAIN, 14));

        row.add(emojiLabel);
        row.add(textLabel);

        return row;
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("Roboto", Font.BOLD, 14)); // Police en gras
        button.setForeground(Color.WHITE); // Texte en blanc
        button.setBackground(backgroundColor); // Couleur de fond
        button.setFocusPainted(false); // Supprime le contour de focus
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Espacement interne
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Curseur en forme de main
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusable(false);
    }

    // Méthode utilitaire pour créer un champ de texte stylisé
    private JPanel createStyledTextField(JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE); // Couleur de fond blanche
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xA1887F), 1), // Bordure marron clair
                BorderFactory.createEmptyBorder(3, 8, 3, 8) // Espacement interne réduit
        ));
        textField.setBorder(BorderFactory.createEmptyBorder()); // Supprime la bordure par défaut
        textField.setBackground(Color.WHITE); // Même couleur que le fond
        textField.setFont(new Font("Roboto", Font.PLAIN, 12)); // Police légèrement plus petite
        textField.setForeground(new Color(0x4E342E)); // Couleur du texte (marron foncé)
        textField.setCaretColor(new Color(0x4E342E)); // Couleur du curseur

        // Ajuster la taille du champ de texte
        textField.setPreferredSize(new Dimension(200, 25)); // Largeur : 200px, Hauteur : 25px

        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSearchField(JTextField searchField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0xF6F1E7)); // Couleur de fond (vert clair)
        // Remplacer CompoundBorder par EmptyBorder pour supprimer la ligne verte
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Espacement interne uniquement

        // Ajuster la taille du champ de recherche
        searchField.setPreferredSize(new Dimension(150, 30)); // Largeur réduite à 150px
        searchField.setBorder(BorderFactory.createEmptyBorder()); // Supprime la bordure par défaut
        searchField.setBackground(Color.WHITE); // Fond blanc pour le champ
        searchField.setFont(new Font("Roboto", Font.PLAIN, 14)); // Police personnalisée
        searchField.setForeground(new Color(0x4E342E)); // Couleur du texte (marron foncé)
        searchField.setCaretColor(new Color(0x4E342E)); // Couleur du curseur

        // Ajouter une loupe emoji à droite
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Roboto", Font.PLAIN, 18)); // Taille de l'emoji
        searchIcon.setForeground(new Color(0x4E342E)); // Couleur marron foncé
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0)); // Espacement à gauche de l'emoji

        panel.add(searchField, BorderLayout.CENTER); // Place le champ de recherche au centre
        panel.add(searchIcon, BorderLayout.EAST); // Place l'emoji à droite
        return panel;
    }

    private void ajouterBarreDeRecherche(JPanel parentPanel) {
        // Utiliser le champ de la classe
        this.searchField = new JTextField(); 

        // Créer le panneau stylisé avec le champ et l'emoji
        JPanel searchPanel = createSearchField(this.searchField); // Appel de la méthode de stylisation

        // Ajouter l'écouteur au champ de texte DANS le panneau stylisé
        this.searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().toLowerCase();
                cardContainer.removeAll();
                // Filtrer et réafficher les cartes
                for (Agent agent : controleur.getTousLesAgents()) {
                    if (agent.getNom().toLowerCase().contains(query) || agent.getPrenom().toLowerCase().contains(query)) {
                        ajouterCarteAgent(agent); // Assurez-vous que cette méthode ajoute la carte correctement
                    }
                }
                // Si aucun résultat, on peut afficher un message ou laisser vide
                if (cardContainer.getComponentCount() == 0) {
                     // Optionnel: Ajouter un label "Aucun résultat"
                     // JLabel noResultLabel = new JLabel("Aucun agent trouvé.");
                     // cardContainer.add(noResultLabel);
                }
                cardContainer.revalidate();
                cardContainer.repaint();
            }
        });

        // Ajouter le PANNEAU STYLISÉ (contenant champ + emoji) au panneau parent
        parentPanel.add(searchPanel, BorderLayout.NORTH); 
    }

    private void chargerAgentsExistants() {
        cardContainer.removeAll(); // Supprime toutes les cartes existantes
        for (Agent agent : controleur.getTousLesAgents()) {
            ajouterCarteAgent(agent); // Ajoute une carte pour chaque agent existant
        }
        cardContainer.revalidate();
        cardContainer.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Agent modele = new Agent(System.getProperty("user.home") + "/agents.dat"); // Initialisation du modèle Agent
            GestionAgent vue = new GestionAgent(modele); // Création de la vue avec le modèle
            vue.setVisible(true); // Rendre la vue visible
        });
    }
    
}
