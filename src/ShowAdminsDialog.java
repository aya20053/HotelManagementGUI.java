import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShowAdminsDialog extends JDialog {
    private JFrame parentFrame;
    public ShowAdminsDialog(JFrame parent) {

        super(parent, "Liste des Administrateurs", true);
          try {
            File iconFile = new File("C:/Users/hp/OneDrive/Documents/pfe_2/k.jpeg"); // Remplacez par le chemin réel de votre icône
            BufferedImage iconImage = ImageIO.read(iconFile);

            // Changer l'icône de la JFrame
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.parentFrame = parent;

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 0, 0, 0)); // Ajustez les marges supérieures selon vos besoins

        StringBuilder dataStringBuilder = new StringBuilder("<div style='font-size: 20px; color: #8AAEE0; text-align: center;'><br>Liste d'administrateur :  <br><br></div>");
        fillDataStringBuilder(dataStringBuilder);

        JLabel htmlLabel = new JLabel("<html>" + dataStringBuilder.toString() + "</html>");
        htmlLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
         contentPanel.add(htmlLabel);

        JScrollPane scrollPane = new JScrollPane(htmlLabel);
        add(scrollPane);
      
        pack(); 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-20);

        setLocationRelativeTo(parent);
    }

    private String style(String text, String color, int fontSize, boolean bold) {
        String style = String.format(
                "<span style='color: %s; font-size: %dpx; %s;'>%s</span>",
                color, fontSize, bold ? "font-weight: bold;" : "", text
        );
        return style;
    }

    private void fillDataStringBuilder(StringBuilder dataStringBuilder) {
        // Connexion à la base de données et récupération des administrateurs
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
            String sql = "SELECT * FROM Administrateur";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                ResultSet resultSet = statement.executeQuery();
               
                while (resultSet.next()) {
                    dataStringBuilder.append(style("ID: ", "#3C5772", 13, true)).append(resultSet.getInt("id")).append("<br>");
                    dataStringBuilder.append(style("Nom: ", "#3C5772", 13, true)).append(resultSet.getString("lastname")).append("<br>");
                    dataStringBuilder.append(style("Prénom: ", "#3C5772", 13, true)).append(resultSet.getString("firstname")).append("<br>");
                    dataStringBuilder.append(style("CIN: ", "#3C5772", 13, true)).append(resultSet.getString("cin")).append("<br>");
                    dataStringBuilder.append(style("Email: ", "#3C5772", 13, true)).append(resultSet.getString("email")).append("<br>");
                    dataStringBuilder.append(style("Genre: ", "#3C5772", 13, true)).append(resultSet.getString("genre")).append("<br>");
                    dataStringBuilder.append(style("Rôle: ", "#3C5772", 13, true)).append(resultSet.getString("role")).append("<br>");
                    dataStringBuilder.append(style("Numéro de téléphone: ", "#3C5772", 13, true)).append(resultSet.getString("phone_number")).append("<br><br>");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ShowAdminsDialog(null).setVisible(true));
    }
}
