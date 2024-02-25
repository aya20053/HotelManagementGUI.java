import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class DeleteRervationDialog extends JDialog {

    private JTextField reservationNumberField;

    public DeleteRervationDialog(JFrame parent) {
        super(parent, "Annuler une Reservation", true);
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
        panel.add(new JLabel("Numéro de reservations:"));
        reservationNumberField = new JTextField();
        panel.add(reservationNumberField);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        JButton deleteButton = new JButton("Annuler");
        Color couleurBouton = Color.decode("#8AAEE0");
        deleteButton.setBackground(couleurBouton);
        deleteButton.addActionListener(e -> {
            deleteReservationAndUpdateRoomStat();
            dispose();
        });
        panel.add(deleteButton);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        JButton cancelButton = new JButton("Quitter");
        cancelButton.setBackground(couleurBouton);
        cancelButton.addActionListener(e -> dispose());
        panel.add(cancelButton);

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    private void deleteReservationAndUpdateRoomStat() {
        String deleteSql = "DELETE FROM Reservations WHERE IDRESERVATION=?";
        String updateSql = "UPDATE Chambres SET Etat = 'Non Reserve' WHERE NumeroChambre IN (SELECT NumeroChambre FROM Reservations WHERE IDRESERVATION = ?)";
    
        int reservationId;
        try {
            reservationId = Integer.parseInt(reservationNumberField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un numéro de réservation valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005");
             PreparedStatement updateStatement = connection.prepareStatement(updateSql);
             PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
    
            connection.setAutoCommit(false);
    
            // Mettre à jour l'état de la chambre
            updateStatement.setInt(1, reservationId);
            int rowsUpdated = updateStatement.executeUpdate();
    
            if (rowsUpdated > 0) {
                System.out.println("Room status updated successfully. Deleting reservation...");
    
                // Supprimer la réservation
                deleteStatement.setInt(1, reservationId);
                int rowsDeleted = deleteStatement.executeUpdate();
    
                if (rowsDeleted > 0) {
                    System.out.println("Reservation deleted successfully.");
                    JOptionPane.showMessageDialog(this, "Réservation supprimée avec succès.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    connection.commit();
                } else {
                    System.out.println("No reservation found for ID: " + reservationId);
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la réservation. Aucune réservation trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    connection.rollback();
                }
            } else {
                System.out.println("No room found for reservation ID: " + reservationId);
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour de l'état de la chambre. Aucune chambre trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                connection.rollback();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur SQL : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Fermer les ressources dans le bloc finally
        }
            
        }
    }
    
