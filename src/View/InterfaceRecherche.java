package View;

import javax.swing.*;
import java.awt.*;
import Controller.ControleurRecherche;

public class InterfaceRecherche extends JFrame {

    private JButton gestionBienButton;
    private JButton gestionClientButton;
    private JButton gestionAgentButton;
    private JButton gestionContratButton;
    private JButton gestionVisiteButton;
    private JButton deconnexionButton;
    private ControleurRecherche controleurRecherche;

    public InterfaceRecherche() {
        setTitle("Accueil - Agence Immobilière");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Afficher en plein écran
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initialiserUI(); // Initialiser l'interface utilisateur

        // Instancier le contrôleur
        controleurRecherche = new ControleurRecherche(
            gestionBienButton, gestionClientButton, gestionAgentButton,
            gestionContratButton, gestionVisiteButton, deconnexionButton, this
        );

        // Relier les boutons au contrôleur
        gestionBienButton.addActionListener(controleurRecherche);
        gestionClientButton.addActionListener(controleurRecherche);
        gestionAgentButton.addActionListener(controleurRecherche);
        gestionContratButton.addActionListener(controleurRecherche);
        gestionVisiteButton.addActionListener(controleurRecherche);
        deconnexionButton.addActionListener(controleurRecherche);
    }

    private void initialiserUI() {
        // Panel principal avec fond blanc cassé
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 240, 230)); // Blanc cassé
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Panel gauche avec l'image
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/maison.png"));
                if (imageIcon.getImage() != null) {
                    Image image = imageIcon.getImage();
                    // Dessiner l'image pour qu'elle occupe tout l'espace du panneau
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        leftPanel.setPreferredSize(new Dimension(400, 800)); // Ajuster la largeur
        leftPanel.setLayout(new GridBagLayout());

        // Panel droit pour les boutons et le titre
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(245, 240, 230)); // Blanc cassé

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30); // Espacement entre les composants
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titre
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("RECHERCHE DE BIENS");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(70, 60, 50)); // Marron foncé
        rightPanel.add(titleLabel, gbc);

        // Panel des boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(6, 1, 20, 20)); // 6 lignes, 1 colonne, espacement vertical et horizontal
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40)); // Marges autour du panel

        // Ajouter les boutons
        gestionBienButton = new JButton("Gestion des biens");
        gestionClientButton = new JButton("Gestion des clients");
        gestionAgentButton = new JButton("Gestion des agents");
        gestionContratButton = new JButton("Gestion des contrats");
        gestionVisiteButton = new JButton("Gestion des visites");
        deconnexionButton = new JButton("Déconnexion");

        // Appliquer le style commun à tous les boutons
        JButton[] boutons = {
            gestionBienButton,
            gestionClientButton,
            gestionAgentButton,
            gestionContratButton,
            gestionVisiteButton,
            deconnexionButton
        };

        for (JButton bouton : boutons) {
            bouton.setBackground(new Color(78, 59, 50));         // Marron foncé
            bouton.setForeground(Color.WHITE);                   // Texte blanc
            bouton.setFont(new Font("Georgia", Font.BOLD, 20));  // Police augmentée
            bouton.setFocusPainted(false);
            bouton.setBorderPainted(false);
            bouton.setPreferredSize(new Dimension(270, 60));     // Largeur x Hauteur
            bouton.setMargin(new Insets(10, 20, 10, 20));        // Marges internes

            // Effet de survol
            bouton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    bouton.setBackground(new Color(100, 80, 60)); // Changer la couleur au survol
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    bouton.setBackground(new Color(78, 59, 50)); // Rétablir la couleur initiale
                }
            });
            buttonPanel.add(bouton);
        }

        // Ajouter le panel des boutons au panel droit
        gbc.gridy = 1;
        rightPanel.add(buttonPanel, gbc);

        // Ajout des panels au split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.4); // Ajustez la proportion pour donner plus d'espace au panneau gauche
        splitPane.setDividerSize(0); // Supprime la bordure entre les panneaux
        mainPanel.add(splitPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfaceRecherche frame = new InterfaceRecherche();
            frame.setVisible(true);
        });
    }
}
