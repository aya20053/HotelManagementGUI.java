import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.image.BufferedImage;

public class AddRoomDialog extends JDialog {

    private JTextField roomNumberField;
    private JTextField roomTypeField;
    private JTextField capacityField;
    private JTextField folderPath;
    private JTextField floorField;
    private JTextField descriptionField;
    private JTextField priceField;
    private ButtonGroup etatButtonGroup;
    private JRadioButton reserveButton;
     private JPanel mainPanel;

    public AddRoomDialog(JFrame parent) {
        super(parent, "Ajouter une Chambre", true);
  try {
            File iconFile = new File("C:/Users/hp/OneDrive/Documents/pfe_2/k.jpeg"); // Remplacez par le chemin réel de votre icône
            BufferedImage iconImage = ImageIO.read(iconFile);

            // Changer l'icône de la JFrame
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel(new GridLayout(14, 5, 10, 10));
        panel.setBorder(new EmptyBorder(0, 20, 0, 20));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel("Numéro de Chambre:"));
        roomNumberField = new JTextField();
        panel.add(roomNumberField);

        panel.add(new JLabel("Type de Chambre:"));
        roomTypeField = new JTextField();
        panel.add(roomTypeField);

        panel.add(new JLabel("Capacité:"));
        capacityField = new JTextField();
        panel.add(capacityField);

        panel.add(new JLabel("État:"));
        panel.add(new JLabel(""));

        etatButtonGroup = new ButtonGroup();
        reserveButton = new JRadioButton("Reserve");
        JRadioButton nonReserveButton = new JRadioButton("Non Reserve");
        etatButtonGroup.add(reserveButton);
        etatButtonGroup.add(nonReserveButton);
        panel.add(reserveButton);
        panel.add(nonReserveButton);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JEditorPane editorPane = new JEditorPane("text/html", "");
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
 
        folderPath = new JTextField();
        panel.add(new JLabel("Chemin du dossier :"));
        panel.add(folderPath);

        JButton browseButton = new JButton("Parcourir");
        Color colorBouton = Color.decode("#BDD1C5");
        Color couleurTexte = Color.decode("#225B7C"); 
        browseButton.setForeground(couleurTexte);
        browseButton.setBackground(colorBouton);
       

        browseButton.addActionListener(e -> browseImage());
        panel.add(browseButton);
        panel.add(new JLabel(""));
        panel.add(mainPanel);

        panel.add(new JLabel(""));

        panel.add(new JLabel("Prix:"));
        priceField = new JTextField();
        panel.add(priceField);

        panel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        panel.add(descriptionField);

        panel.add(new JLabel("Étage:"));
        floorField = new JTextField();
        panel.add(floorField);

        JButton addButton = new JButton("Ajouter");
        Color couleurBouton = Color.decode("#8AAEE0");
        addButton.setBackground(couleurBouton);
        addButton.addActionListener(e -> {
            addRoom();
            dispose();
        });
        panel.add(addButton);

        JButton cancelButton = new JButton("Annuler");
        cancelButton.setBackground(couleurBouton);

        cancelButton.addActionListener(e -> dispose());
        panel.add(cancelButton);

        add(panel);
        pack();
        setSize(800, 600); 
        setLocationRelativeTo(parent);
    }

    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(this);
    
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            folderPath.setText(selectedDirectory.getAbsolutePath());
            updateEditorPane(folderPath); // Passer le JTextField ici
        }
    }

    private void updateEditorPane(JTextField textFieldFolderPath) {
        String folderPathClicked = textFieldFolderPath.getText();
        JEditorPane editorPane = new JEditorPane("text/html", "<html><a href=\"file:///" + folderPathClicked + "\">Ouvrir le dossier</a></html>");
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
    
        editorPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().open(new File(folderPathClicked));
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'ouverture du dossier : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
    

        mainPanel.removeAll();
        mainPanel.add(editorPane);
        revalidate();
        repaint();
    }

  void addRoom() {

    try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
        String sql = "INSERT INTO Chambres (NumeroChambre, TypeChambre, Capacite, Etat, folderPath, Prix, Description, Etage) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String roomNumberText = roomNumberField.getText();
            String capacityText = capacityField.getText();
            String priceText = priceField.getText();
            String floorText = floorField.getText();

            if (!roomNumberText.isEmpty() && !capacityText.isEmpty() && !priceText.isEmpty() && !floorText.isEmpty()) {
                statement.setInt(1, Integer.parseInt(roomNumberText));
                statement.setString(2, roomTypeField.getText());
                statement.setInt(3, Integer.parseInt(capacityText));
                String etat = reserveButton.isSelected() ? "Reserve" : "Non Resere";
                statement.setString(4, etat);
                statement.setString(5, folderPath.getText());
                statement.setDouble(6, Double.parseDouble(priceText));
                statement.setString(7, descriptionField.getText());
                statement.setInt(8, Integer.parseInt(floorText));

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Chambre ajoutée avec succès.", "Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la chambre.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la chambre : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

}
