import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.image.BufferedImage;


class DeleteRoomDialog extends JDialog {

    private JTextField roomNumberField;

    public DeleteRoomDialog(JFrame parent) {
        super(parent, "Supprimer une Chambre", true);

          try {
            File iconFile = new File("C:/Users/hp/OneDrive/Documents/pfe_2/k.jpeg"); // Remplacez par le chemin réel de votre icône
            BufferedImage iconImage = ImageIO.read(iconFile);

            // Changer l'icône de la JFrame
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
 JPanel panel = new JPanel(new GridLayout(5, 5));
        panel.setBorder(new EmptyBorder(30, 20, 30, 20));
            panel.add(new JLabel("Numéro de Chambre:"));
        roomNumberField = new JTextField();
        panel.add(roomNumberField);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        JButton deleteButton = new JButton("Supprimer");
        Color couleurBouton = Color.decode("#8AAEE0");
       deleteButton.setBackground(couleurBouton);
        deleteButton.addActionListener(e -> {
            deleteRoom();
            dispose();
        });
        panel.add(deleteButton);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));


        JButton cancelButton = new JButton("Annuler");
        cancelButton.setBackground(couleurBouton);
        cancelButton.addActionListener(e -> dispose());
        panel.add(cancelButton);

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    private void deleteRoom() {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
            String sql = "DELETE FROM Chambres WHERE NumeroChambre=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Integer.parseInt(roomNumberField.getText()));

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Chambre supprimée avec succès.", "Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la chambre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la chambre : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
 