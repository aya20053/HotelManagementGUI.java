import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.image.BufferedImage;

public class DeleteAdminDialog extends JDialog {
    private JTextField adminIdField;
    public DeleteAdminDialog(JFrame parent) {
        super(parent, "Supprimer un Administrateur", true);
          try {
            File iconFile = new File("C:/Users/hp/OneDrive/Documents/pfe_2/k.jpeg"); // Remplacez par le chemin réel de votre icône
            BufferedImage iconImage = ImageIO.read(iconFile);

            // Changer l'icône de la JFrame
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel(new  GridLayout(5, 5));
        panel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel adminIdLabel = new JLabel("ID de l'Administrateur à supprimer:");
        panel.add(adminIdLabel);

        adminIdField = new JTextField();
        panel.add(adminIdField);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        Color couleurBouton = Color.decode("#8AAEE0");
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.setBackground(couleurBouton);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAdmin();
            }
        });
        panel.add(deleteButton);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        JButton cancelButton = new JButton("Annuler");
        cancelButton.setBackground(couleurBouton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Fermez le dialogue si l'opération est annulée
            }
        });
        panel.add(cancelButton);

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    private void deleteAdmin() {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
            String sql = "DELETE FROM Administrateur WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, Integer.parseInt(adminIdField.getText()));

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Admin supprimée avec succès.", "Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l Admin.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l Admin : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setSize(400, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton openDialogButton = new JButton("Ouvrir le dialogue");
            openDialogButton.addActionListener(e -> {
                DeleteAdminDialog dialog = new DeleteAdminDialog(frame);
                dialog.setVisible(true);
            });

            frame.add(openDialogButton);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}