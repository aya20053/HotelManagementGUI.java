import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;


public class ChefAdminWindow extends JFrame {

    public ChefAdminWindow(JFrame fenetrePrecedente) {
        super("Espace Chef Administrateur");
        try {
            File iconFile = new File("C:/Users/hp/OneDrive/Documents/pfe_2/k.jpeg"); // Remplacez par le chemin réel de votre icône
            BufferedImage iconImage = ImageIO.read(iconFile);

            // Changer l'icône de la JFrame
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }


        JPanel panel = new JPanel(new GridBagLayout());
        getContentPane().setBackground(Color.WHITE); 
        Color couleurBouton = Color.decode("#709CA7");
        Font customFont = new Font("Segoe UI", Font.PLAIN,18);
       
        JLabel titre = new JLabel("BIENVENUE DANS L'ESPACE CHEF ADMINISTRATEUR ");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 29));

        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0;
        titleConstraints.gridy = 0;
        titleConstraints.gridwidth = 5;
        titleConstraints.weightx = 1.0;
        titleConstraints.anchor = GridBagConstraints.NORTH;
        panel.add(titre, titleConstraints);

        JButton ajouterAdminButton = createStyledButton("Ajouter un Administrateur",couleurBouton, Color.WHITE, customFont);
        JButton supprimerAdminButton = createStyledButton("Supprimer un Administrateur",couleurBouton, Color.WHITE, customFont);
        JButton modifierAdminButton = createStyledButton("Modifier un Administrateur",couleurBouton, Color.WHITE, customFont);
        JButton afficherAdminsButton = createStyledButton("Afficher tous les Administrateurs",couleurBouton, Color.WHITE, customFont);
        JButton chercherAdminButton = createStyledButton("Chercher un Administrateur", couleurBouton, Color.WHITE, customFont);
        JButton gestionChambresButton = createStyledButton("Gestion de Chambres", couleurBouton, Color.WHITE, customFont);
        
    
        Dimension buttonSize = new Dimension(300, 40); 
        ajouterAdminButton.setPreferredSize(buttonSize);
        modifierAdminButton.setPreferredSize(buttonSize);
         supprimerAdminButton.setPreferredSize(buttonSize);
        afficherAdminsButton.setPreferredSize(buttonSize);
        chercherAdminButton.setPreferredSize(buttonSize);
        gestionChambresButton.setPreferredSize(buttonSize);

        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 0;
        buttonConstraints.gridy = 1;
        buttonConstraints.insets = new Insets(20, 20, 20, 20);
        buttonConstraints.gridwidth = 5; 
        panel.add(ajouterAdminButton, buttonConstraints);
        buttonConstraints.gridy = 2;
        panel.add(modifierAdminButton, buttonConstraints);

        buttonConstraints.gridy = 3;
        panel.add(supprimerAdminButton, buttonConstraints);

        buttonConstraints.gridy = 4;
        panel.add(afficherAdminsButton, buttonConstraints);

        buttonConstraints.gridy =5;  
        panel.add(chercherAdminButton, buttonConstraints);

        buttonConstraints.gridy = 6;
        buttonConstraints.anchor = GridBagConstraints.SOUTH; 
        panel.add(gestionChambresButton, buttonConstraints);

        ajouterAdminButton.addActionListener(e -> AjouterAdmin());
        supprimerAdminButton.addActionListener(e -> deleteAdmin());
        modifierAdminButton.addActionListener(e -> modifierAdmin());
        afficherAdminsButton.addActionListener(e -> afficherAdmin());
        chercherAdminButton.addActionListener(e -> chercherAdmin());
        gestionChambresButton.addActionListener(e -> openGestionChambres());

        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-20);

        setLocationRelativeTo(null);
        setVisible(true);
    }


    private JButton createStyledButton(String text, Color backgroundColor, Color textColor, Font font) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(font);
        return button;
    }

    private void AjouterAdmin() {
        AddAdminDialog addAdminDialog = new AddAdminDialog(this);
        addAdminDialog.setVisible(true);
    }

    private void deleteAdmin() {
        DeleteAdminDialog deleteAdminDialog = new DeleteAdminDialog(this);
        deleteAdminDialog.setVisible(true);
    }

    private void modifierAdmin() {
        System.out.println("bouton clic");
        EditAdminDialog editAdminDialog = new EditAdminDialog(this);
        editAdminDialog.setVisible(true);
    }

    private void afficherAdmin() {
        ShowAdminsDialog showAdminsDialog = new ShowAdminsDialog(this);
        showAdminsDialog.setVisible(true);
    }
    private void chercherAdmin() {
        ChercherAdminDialog chercherAdminDialog = new ChercherAdminDialog(this);
        chercherAdminDialog.setVisible(true);
    }

    private void openGestionChambres() {
        GestionChambres gestionChambres = new GestionChambres();
          gestionChambres.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                ChefAdminWindow.this.setVisible(true);
            }
        });
    
         gestionChambres.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        gestionChambres.setVisible(true);
        ChefAdminWindow.this.setVisible(false);
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChefAdminWindow(null).setVisible(true));
    }
}
