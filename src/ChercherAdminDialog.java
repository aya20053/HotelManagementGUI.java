import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChercherAdminDialog extends JDialog {

    private JTextField cinField;

    public ChercherAdminDialog(JFrame parent) {
        super(parent, "Chercher un Administrateur", true);
          try {
            File iconFile = new File("C:/Users/hp/OneDrive/Documents/pfe_2/k.jpeg"); // Remplacez par le chemin réel de votre icône
            BufferedImage iconImage = ImageIO.read(iconFile);

            // Changer l'icône de la JFrame
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        panel.add(new JLabel("saisir le CIN:"));
        cinField = new JTextField();
        panel.add(cinField);
        

        JButton chercherButton = new JButton("Chercher");
        Color couleurBouton = Color.decode("#8AAEE0");
        chercherButton.setBackground(couleurBouton);
       chercherButton.addActionListener(e ->  chercherAdmin() );
        panel.add(chercherButton);

        JButton annulerButton = new JButton("Annuler");
        annulerButton.setBackground(couleurBouton);
        annulerButton.addActionListener(e -> dispose());
        panel.add(annulerButton);

        add(panel);
        pack();
        setSize(400, 250);
        setLocationRelativeTo(parent);
    }

    private void chercherAdmin() {
        String cin = cinField.getText();
    
        if (cin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir le champ.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Administrateur WHERE 1 = 1");
    
            // Ajoutez une condition pour filtrer par CIN si le champ n'est pas vide
            if (!cin.isEmpty()) {
                sqlBuilder.append(" AND CIN = ?");
            }
    
            try (PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString())) {
                int parameterIndex = 1;
    
                // Ajoutez le paramètre CIN si le champ n'est pas vide
                if (!cin.isEmpty()) {
                    statement.setString(parameterIndex++, cin);
                }
    
                try (ResultSet resultSet = statement.executeQuery()) {
                    // StringBuilder pour afficher les résultats
                    StringBuilder resultBuilder = new StringBuilder();
    
                    while (resultSet.next()) {
                        // Récupérer les données et les ajouter au StringBuilder
                        resultBuilder.append("ID: ").append(resultSet.getString("ID")).append("\n");
                        resultBuilder.append("Prénom: ").append(resultSet.getString("FIRSTNAME")).append("\n");
                        resultBuilder.append("Nom: ").append(resultSet.getString("LASTNAME")).append("\n");
                        resultBuilder.append("CIN: ").append(resultSet.getString("CIN")).append("\n");
                        resultBuilder.append("Genre: ").append(resultSet.getString("GENRE")).append("\n");
                        resultBuilder.append("Role: ").append(resultSet.getString("ROLE")).append("\n");
                        resultBuilder.append("Numero de telephone: ").append(resultSet.getString("PHONE_NUMBER")).append("\n");
    
                        resultBuilder.append("\n");  // Ajoutez une ligne vide entre les enregistrements
                    }
    
                    // Affichez le résultat où vous en avez besoin (peut-être dans une zone de texte ou une autre interface utilisateur)
                    afficherResultats(resultBuilder.toString());
                }
    
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur de base de données : " + ex.getMessage(), "Erreur SQL", JOptionPane.ERROR_MESSAGE);
            }
    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de base de données : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void afficherResultats(String results) {
        // Vérifier si la chaîne de résultats n'est pas vide avant d'afficher la boîte de dialogue
        if (results != null && !results.isEmpty()) {
            // Afficher les résultats dans une boîte de dialogue d'information
            JOptionPane.showMessageDialog(this, results, "Résultats de la recherche", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Afficher un message si les résultats sont vides
            JOptionPane.showMessageDialog(this, "Aucun résultat trouvé.", "Résultats de la recherche", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    
}    




