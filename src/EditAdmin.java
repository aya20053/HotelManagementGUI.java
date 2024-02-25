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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.ResultSet;

public class EditAdmin extends JDialog {

    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField cinField;
    private JTextField emailField;
    private JTextField phoneNumberField;
    private JPasswordField passwordField;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private JTextField idField;
    private int adminId;

    public EditAdmin(JFrame parent) {
        super(parent, "Modifier mes données", true);
          try {
            File iconFile = new File("C:/Users/hp/OneDrive/Documents/pfe_2/k.jpeg"); // Remplacez par le chemin réel de votre icône
            BufferedImage iconImage = ImageIO.read(iconFile);

            // Changer l'icône de la JFrame
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (parent == null) {
            throw new IllegalArgumentException("Le parent ne peut pas être null.");
        }

        JPanel panel = new JPanel(new GridLayout(13, 5, 10, 10));
        panel.setBorder(new EmptyBorder(0, 20, 0, 20));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);

        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!idField.getText().isEmpty()) {
                    int enteredId = Integer.parseInt(idField.getText());
                    fetchAdminData(enteredId);
                }
            }
        });



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

        panel.add(new JLabel("Numéro de téléphone:"));
        phoneNumberField = new JTextField();
        panel.add(phoneNumberField);

        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        JButton editButton = new JButton("Modifier");
        Color couleurBouton = Color.decode("#8AAEE0");
        editButton.setBackground(couleurBouton);
        editButton.addActionListener(e -> {
            if (!idField.getText().isEmpty()) {
                this.adminId = Integer.parseInt(idField.getText());
                modifierAdmin();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez spécifier l'ID de l'administrateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(editButton);

        JButton cancelButton = new JButton("Annuler");
        cancelButton.setBackground(couleurBouton);
        cancelButton.addActionListener(e -> dispose());
        panel.add(cancelButton);

        add(panel);
        pack();
        setSize(800, 650);
        setLocationRelativeTo(parent);
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
    // ...

private void fetchAdminData(int enteredId) {
    try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
        String sql = "SELECT * FROM Administrateur WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, enteredId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                lastNameField.setText(resultSet.getString("lastname"));
                firstNameField.setText(resultSet.getString("firstname"));
                cinField.setText(resultSet.getString("cin"));
                emailField.setText(resultSet.getString("email"));
                String genre = resultSet.getString("genre");
                if ("Homme".equals(genre)) {
                    maleRadioButton.setSelected(true);
                    femaleRadioButton.setSelected(false);
                } else if ("Femme".equals(genre)) {
                    maleRadioButton.setSelected(false);
                    femaleRadioButton.setSelected(true);
                }
                passwordField.setText(resultSet.getString("password"));
                phoneNumberField.setText(resultSet.getString("phone_number"));
            } else {
                JOptionPane.showMessageDialog(this, "Aucun administrateur trouvé avec l'ID spécifié.", "Erreur", JOptionPane.ERROR_MESSAGE);
                clearFields();
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur de base de données lors de la récupération des données de l'administrateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
        clearFields();
    }
}


    private void clearFields() {
        lastNameField.setText("");
        firstNameField.setText("");
        cinField.setText("");
        emailField.setText("");
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
        passwordField.setText("");
        phoneNumberField.setText("");
    }


    private void modifierAdmin() {
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String cin = cinField.getText();
        String email = emailField.getText();
        String genre = maleRadioButton.isSelected() ? "Homme" : "Femme";
        String password = new String(passwordField.getPassword());
        String phoneNumber = phoneNumberField.getText();
    
        // Validation des champs si nécessaire
        if (lastName.isEmpty() || firstName.isEmpty() || cin.isEmpty() || email.isEmpty() ){
               
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
            String sql = "UPDATE Administrateur SET lastname=?, firstname=?, cin=?, email=?, genre=?, password=?, phone_number=? WHERE id=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, lastName);
                statement.setString(2, firstName);
                statement.setString(3, cin);
                statement.setString(4, email);
                statement.setString(5, genre);
                statement.setString(6, password);
                statement.setString(8, phoneNumber);
                statement.setInt(9, adminId);
    
                int affectedRows = statement.executeUpdate();
    
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Administrateur modifié avec succès !");
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de modification de l'administrateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
