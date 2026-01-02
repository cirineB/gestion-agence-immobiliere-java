package View;

import Controller.ControleurConnexion;
import Model.Agent;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class InterfaceConnexion extends JFrame {

    private JTextField usernameField;
    private JTextField idField;
    private JButton loginButton;
    private ControleurConnexion controleurConnexion;

    public InterfaceConnexion(Agent modele) {
        // Configuration de la fenêtre
        setTitle("Cirine Immo - Connexion");
        setSize(1200, 800); // Format plus large
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Afficher en plein écran
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Panel principal avec image de fond
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Charge l'image de fond
                ImageIcon icon = new ImageIcon(getClass().getResource("/images/salonlast.jpg"));
                Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Panel droit transparent (70% de largeur)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false); // Transparent
        rightPanel.setBorder(new EmptyBorder(100, 0, 0, 30));
        rightPanel.setPreferredSize(new Dimension(getWidth() * 70 / 100, getHeight()));

        // Carte de connexion semi-transparente
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(255, 255, 255, 200)); // Blanc semi-transparent
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 2, 1, new Color(210, 200, 190)),
                new EmptyBorder(60, 60, 60, 60)
        ));
        card.setMaximumSize(new Dimension(450, 600));

        // Titre élégant
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLine1 = new JLabel("Offrez à vos biens immobiliers");
        titleLine1.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLine1.setForeground(new Color(70, 60, 50)); // Marron foncé
        titleLine1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLine2 = new JLabel("la gestion qu'ils méritent");
        titleLine2.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLine2.setForeground(new Color(70, 60, 50)); // Marron foncé
        titleLine2.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(titleLine1);
        titlePanel.add(Box.createVerticalStrut(10)); // Espacement entre les lignes
        titlePanel.add(titleLine2);
        titlePanel.setBorder(new EmptyBorder(0, 0, 40, 0));

        card.add(titlePanel);

        // Champ Nom d'utilisateur
        JPanel usernamePanel = createStyledField("NOM D'UTILISATEUR");
        usernameField = (JTextField) usernamePanel.getComponent(1);
        card.add(usernamePanel);
        card.add(Box.createRigidArea(new Dimension(0, 30)));

        // Champ ID
        JPanel idPanel = createStyledField("IDENTIFIANT");
        idField = (JTextField) idPanel.getComponent(1);
        card.add(idPanel);
        card.add(Box.createRigidArea(new Dimension(0, 40)));

        // Bouton Connexion
        loginButton = new JButton("SE CONNECTER");
        styleButton(loginButton);
        card.add(loginButton);

        // Instancier le contrôleur
        controleurConnexion = new ControleurConnexion(usernameField, idField, this, modele);

        // Relier le bouton au contrôleur
        loginButton.setActionCommand("Connexion");
        loginButton.addActionListener(controleurConnexion);

        // Signature
        JLabel signature = new JLabel("CIRINE IMMOBILIER", SwingConstants.CENTER);
        signature.setFont(new Font("Georgia", Font.ITALIC, 13));
        signature.setForeground(new Color(120, 110, 100));
        signature.setAlignmentX(Component.CENTER_ALIGNMENT);
        signature.setBorder(new EmptyBorder(30, 0, 0, 0));
        card.add(signature);

        rightPanel.add(Box.createVerticalGlue());

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new FlowLayout(FlowLayout.RIGHT));
        wrapper.add(card);

        rightPanel.add(wrapper);

        rightPanel.add(Box.createVerticalGlue());

        backgroundPanel.add(rightPanel, BorderLayout.EAST);
        add(backgroundPanel);

        setVisible(true);
    }

    private JPanel createStyledField(String labelText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Georgia", Font.PLAIN, 14));
        label.setForeground(new Color(100, 90, 80));
        panel.add(label);

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(350, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(180, 170, 160)),
                new EmptyBorder(5, 0, 5, 0)
        ));
        field.setFont(new Font("Georgia", Font.PLAIN, 16));
        field.setForeground(new Color(70, 60, 50));
        field.setOpaque(false);
        panel.add(field);

        return panel;
    }

    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setFont(new Font("Georgia", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(90, 80, 70)); // Marron moyen
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 60, 50), 1),
                new EmptyBorder(12, 40, 12, 40)
        ));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Agent modele = new Agent(System.getProperty("user.home") + "/agents.dat");
            new InterfaceConnexion(modele);
        });
    }
}