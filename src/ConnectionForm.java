

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.awt.image.BufferedImage;

public class ConnectionForm extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public ConnectionForm() {
        super(" Connexion Administrateur");
          try {
            File iconFile = new File("C:/Users/hp/OneDrive/Documents/pfe_2/k.jpeg"); 
            BufferedImage iconImage = ImageIO.read(iconFile);

            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        
     

        JPanel panel = new BackgroundPanel();  // Utilise le JPanel personnalisé avec arrière-plan
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        Font customFont = new Font("Segoe UI Black", Font.PLAIN, 25);

        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        usernameLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(usernameLabel, constraints);
        usernameLabel.setFont(customFont);

        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setForeground(Color.WHITE);
        constraints.gridy = 1;
        panel.add(passwordLabel, constraints);
        passwordLabel.setFont(customFont);

        usernameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(usernameField, constraints);

        passwordField = new JPasswordField(20);
        constraints.gridy = 1;
        panel.add(passwordField, constraints);

        JButton loginButton = new JButton("Se Connecter");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        Color couleurBouton = Color.decode("#0000");
        loginButton.setBackground(couleurBouton);
        Color couleurTexteBouton = Color.WHITE;
        loginButton.setForeground(couleurTexteBouton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginAdmin();
            }
        });
        panel.add(loginButton, constraints);

       
       

        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.NORMAL);
    }
    

    private void loginAdmin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
    
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (isAdminRegistered(username, password)) {
            JOptionPane.showMessageDialog(this, "Connexion réussie !");
            //openMainWindow();
        } else {
            JOptionPane.showMessageDialog(this, "Informations incorrectes. Veuillez contacter l'administrateur en chef. Erreur : " , "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean isAdminRegistered(String username, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
            String sql = "SELECT * FROM Administrateur WHERE LASTNAME = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
    
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
    
                    // Vérifie si le mot de passe saisi correspond au mot de passe stocké
                    if (password.equals(storedPassword)) {
                        String role = resultSet.getString("role");
    
                        if ("Chef Administrateur".equals(role)) {
                            // C'est un chef administrateur
                            openChefAdminWindow();
                        } else if ("Administrateur".equals(role)) {
                            // C'est un administrateur ordinaire
                            openMainWindow();
                        }
                        return true;
                    }
                }
    
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    

    private void openMainWindow() {
        // Ouvre la fenêtre principale de l'application
        SwingUtilities.invokeLater(() -> new GestionChambres().setVisible(true));
        dispose(); 
    }
    private void openChefAdminWindow() {
        // Ouvre la fenêtre spécifique au Chef Administrateur
        SwingUtilities.invokeLater(() -> new ChefAdminWindow(this).setVisible(true));
        dispose();
    }

   

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConnectionForm().setVisible(true));
    }


    
    
    // Classe personnalisée pour le JPanel avec arrière-plan
    class BackgroundPanel extends JPanel {

        private Image background;

        public BackgroundPanel() {
            try {
                // Charge l'image depuis le fichier
                background = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("hotel2.jpeg"))).getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Dessine l'image d'arrière-plan
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

