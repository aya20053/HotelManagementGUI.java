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

public class AddAdminDialog extends JDialog {

    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField cinField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField phoneNumberField;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private JRadioButton adminRadioButton;
    private JRadioButton chefAdminRadioButton;

    public AddAdminDialog(JFrame parent) {
        super(parent, "Ajouter un Administrateur", true);
        try {
            File iconFile = new File("C:/Users/hp/OneDrive/Documents/pfe_2/k.jpeg"); // Remplacez par le chemin réel de votre icône
            BufferedImage iconImage = ImageIO.read(iconFile);

            // Changer l'icône de la JFrame
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel(new GridLayout(14, 8, 10, 10));
        panel.setBorder(new EmptyBorder(0, 20, 0, 20));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel("Nom:"));
        lastNameField = new JTextField();
        panel.add(lastNameField);

        panel.add(new JLabel("Prénom:"));
        firstNameField = new JTextField();
        panel.add(firstNameField);

        panel.add(new JLabel("CIN:"));
        cinField = new JTextField();
        panel.add(cinField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        maleRadioButton = new JRadioButton("Homme");
        femaleRadioButton = new JRadioButton("Femme");

        ButtonGroup genreGroup = new ButtonGroup();
        genreGroup.add(maleRadioButton);
        genreGroup.add(femaleRadioButton);

        panel.add(new JLabel("Genre:"));
        panel.add(new JLabel(""));
        panel.add(maleRadioButton);
        panel.add(femaleRadioButton);

        panel.add(new JLabel("Mot de passe:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Rôle:"));      
         panel.add(new JLabel(""));
        adminRadioButton = new JRadioButton("Administrateur");
        chefAdminRadioButton = new JRadioButton("Chef Administrateur");

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(adminRadioButton);
        roleGroup.add(chefAdminRadioButton);

        panel.add(adminRadioButton);
        panel.add(chefAdminRadioButton);

        panel.add(new JLabel("Numéro de téléphone:"));
        phoneNumberField = new JTextField();
        panel.add(phoneNumberField);

        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        JButton addButton = new JButton("Ajouter");
        Color couleurBouton = Color.decode("#8AAEE0");
        addButton.setBackground(couleurBouton);
        addButton.addActionListener(e -> {
            ajouterAdmin();
            dispose();
        });
        panel.add(addButton);

        JButton cancelButton = new JButton("Annuler");
        cancelButton.setBackground(couleurBouton);
        cancelButton.addActionListener(e -> dispose());
        panel.add(cancelButton);

        add(panel);
        pack();
        setSize(800, 650);
        setLocationRelativeTo(parent);
    }

    private void ajouterAdmin() {
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String cin = cinField.getText();
        String email = emailField.getText();
        String genre = "";
        if (maleRadioButton.isSelected()) {
            genre = "Homme";
        } else if (femaleRadioButton.isSelected()) {
            genre = "Femme";
        }
        String password = new String(passwordField.getPassword());
        String role = "";
        if (adminRadioButton.isSelected()) {
            role = "Administrateur";
        } else if (chefAdminRadioButton.isSelected()) {
            role = "Chef Administrateur";
        }
        String phoneNumber = phoneNumberField.getText();

        // Validation des champs si nécessaire
        if (lastName.isEmpty() || firstName.isEmpty() || cin.isEmpty() || email.isEmpty() ||
                genre.isEmpty() || password.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Connexion à la base de données et ajout de l'administrateur
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
            String sql = "INSERT INTO Administrateur (id, lastname, firstname, cin, email, genre, password, role, phone_number) VALUES (administrateur_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, lastName);
                statement.setString(2, firstName);
                statement.setString(3, cin);
                statement.setString(4, email);
                statement.setString(5, genre);
                statement.setString(6, password);
                statement.setString(7, role);
                statement.setString(8, phoneNumber);

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Administrateur ajouté avec succès !");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'administrateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton openDialogButton = new JButton("Ouvrir le dialogue");
            openDialogButton.addActionListener(e -> {
                AddAdminDialog dialog = new AddAdminDialog(frame);
                dialog.setVisible(true);
            });

            frame.add(openDialogButton);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
