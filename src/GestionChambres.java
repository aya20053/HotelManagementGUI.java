import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.io.IOException;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.image.BufferedImage;


    public class GestionChambres extends JFrame {

        private JEditorPane roomDetailsTextArea;
        private JPanel mainPanel;
        private JPanel imagePanel;
        private JPanel filterPanel;
        private JTextField numeroChambreFilterField;
        private JTextField typeChambreFilterField;
        private JTextField capaciteFilterField;
        private JTextField etatFilterField;
        private JTextField prixFilterField;
        private JTextField descriptionFilterField;
        private JTextField etageFilterField;
        private JButton filterButton;

        public GestionChambres() {
            super("Gestion des Chambres");
  try {
            File iconFile = new File("C:/Users/hp/OneDrive/Documents/pfe_2/k.jpeg"); // Remplacez par le chemin réel de votre icône
            BufferedImage iconImage = ImageIO.read(iconFile);

            // Changer l'icône de la JFrame
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

            roomDetailsTextArea = new JEditorPane();  
            roomDetailsTextArea.setEditable(false);
            roomDetailsTextArea.setContentType("text/html");
        
            Color couleurBouton = Color.decode("#8AAEE0");
            mainPanel = new JPanel();
            mainPanel.setLayout(new FlowLayout());
    

            numeroChambreFilterField = new JTextField(10);
            typeChambreFilterField = new JTextField(10);
            capaciteFilterField = new JTextField(10);
            etatFilterField = new JTextField(10);
            prixFilterField = new JTextField(10);
            descriptionFilterField = new JTextField(10);
            etageFilterField = new JTextField(10);
    
            


            imagePanel = new JPanel();
            JButton displayButton = new JButton("Afficher les Chambres");
            displayButton.setBackground(couleurBouton);
            displayButton.addActionListener(e -> displayRoomDetails());
    
            
        imagePanel.setLayout(new FlowLayout()); 
        JButton reserveButton = new JButton("Réserver une Chambre");
        reserveButton.setBackground(couleurBouton);
        reserveButton.addActionListener(e -> reserveRoom());
    
        JButton addButton = new JButton("Ajouter une Chambre");
        addButton.setBackground(couleurBouton);
        addButton.addActionListener(e -> addRoom());
    
        JButton editButton = new JButton("Modifier une Chambre");
        editButton.setBackground(couleurBouton);
        editButton.addActionListener(e -> editRoom());
    
        JButton deleteButton = new JButton("Supprimer une Chambre");
        deleteButton.setBackground(couleurBouton);
        deleteButton.addActionListener(e -> deleteRoom());
    
        JButton displayReservedButton = new JButton("Afficher les reservations");
        displayReservedButton.setBackground(couleurBouton);
        displayReservedButton.addActionListener(e -> displayReservedRoomDetails());
    
        JButton statisticsButton = new JButton("Statistiques");
        statisticsButton.setBackground(couleurBouton);
        statisticsButton.addActionListener(e -> showStatistics());
    

        JButton deleteReservationButton = new JButton("Annuler une reservation");
        deleteReservationButton.setBackground(couleurBouton);
        deleteReservationButton.addActionListener(e -> deleteReservation());

        JButton editProfilButton = new JButton("Modifier mes données");
        editProfilButton.setBackground(couleurBouton);
        editProfilButton.addActionListener(e -> editProfil());


        filterPanel = createFilterPanel();
        filterButton = new JButton("Filtrer");
        filterButton.setBackground(couleurBouton);
        filterButton.addActionListener(e -> toggleFilterPanel());

   

// Création d'une bordure avec une ligne noire
Border paddingBorder = new EmptyBorder(5, 5, 5, 5);  Border lineBorder = new LineBorder(Color.BLACK);
Border buttonBorder = BorderFactory.createCompoundBorder(lineBorder, paddingBorder);

// Appliquer la bordure à chaque bouton
displayButton.setBorder(buttonBorder);
filterButton.setBorder(buttonBorder);
addButton.setBorder(buttonBorder);
editButton.setBorder(buttonBorder);
deleteButton.setBorder(buttonBorder);
reserveButton.setBorder(buttonBorder);
displayReservedButton.setBorder(buttonBorder);
deleteReservationButton.setBorder(buttonBorder);
editProfilButton.setBorder(buttonBorder);
statisticsButton.setBorder(buttonBorder);

        
        JPanel panel = new JPanel();
        panel.add(displayButton);
        imagePanel.add(filterButton);
        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(reserveButton);
        panel.add(displayReservedButton);
        panel.add(deleteReservationButton);
        panel.add(editProfilButton);
        panel.add(statisticsButton);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setLayout(new FlowLayout());
        this.add(mainPanel);

        // Ajoutez une barre de défilement à droite du texte des détails des chambres
        JScrollPane scrollPane = new JScrollPane(roomDetailsTextArea);
        add(scrollPane, BorderLayout.CENTER);
    
        add(panel, BorderLayout.SOUTH);
        add(new JScrollPane(imagePanel), BorderLayout.EAST);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height-20);

        setLocationRelativeTo(null);
       }
     
      
    
    
       private void toggleFilterPanel() {
        JPanel filterPanel = createFilterPanel();
    
        int result = JOptionPane.showConfirmDialog(this, filterPanel, "Filtrer les Chambres", JOptionPane.OK_CANCEL_OPTION);
    
        if (result == JOptionPane.OK_OPTION) {
            filterRooms();
        }
    }
    
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new GridLayout(14, 5, 10, 10));
        filterPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        filterPanel.add(new JLabel("Numéro de Chambre:"));
        filterPanel.add(numeroChambreFilterField);
    
        filterPanel.add(new JLabel("Type de Chambre:"));
        filterPanel.add(typeChambreFilterField);
    
        filterPanel.add(new JLabel("Capacité:"));
        filterPanel.add(capaciteFilterField);
    
        filterPanel.add(new JLabel("État:"));
        filterPanel.add(etatFilterField);
    
        filterPanel.add(new JLabel("Prix:"));
        filterPanel.add(prixFilterField);
    
        filterPanel.add(new JLabel("Description:"));
        filterPanel.add(descriptionFilterField);
    
        filterPanel.add(new JLabel("Étage:"));
        filterPanel.add(etageFilterField);
    
        return filterPanel;
    }

    
    

    private void setIntOrZero(PreparedStatement statement, int parameterIndex, String value) throws SQLException {
        try {
            if (value.isEmpty()) {
                statement.setNull(parameterIndex, java.sql.Types.INTEGER);
            } else {
                int intValue = Integer.parseInt(value);
                statement.setInt(parameterIndex, intValue);
            }
        } catch (NumberFormatException e) {
            statement.setNull(parameterIndex, java.sql.Types.INTEGER);
        }
    }
    
    private void setDoubleOrZero(PreparedStatement statement, int parameterIndex, String value) throws SQLException {
        try {
            if (value.isEmpty()) {
                statement.setNull(parameterIndex, java.sql.Types.DOUBLE);
            } else {
                double doubleValue = Double.parseDouble(value);
                statement.setDouble(parameterIndex, doubleValue);
            }
        } catch (NumberFormatException e) {
            statement.setNull(parameterIndex, java.sql.Types.DOUBLE);
        }
    }
    
        
//filtrer
private void filterRooms() {
    try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
        String filterTextNumeroChambre = numeroChambreFilterField.getText().trim();
        String filterTextTypeChambre = typeChambreFilterField.getText().trim();
        String filterTextCapacite = capaciteFilterField.getText().trim();
        String filterTextEtat = etatFilterField.getText().trim();
        String filterTextPrix = prixFilterField.getText().trim();
        String filterTextDescription = descriptionFilterField.getText().trim();
        String filterTextEtage = etageFilterField.getText().trim();

        String sql = "SELECT * FROM Chambres WHERE " +
                "(NumeroChambre LIKE ? OR ? IS NULL OR ? = '') AND " +
                "(TypeChambre LIKE ? OR ? IS NULL OR ? = '') AND " +
                "(Capacite = ? OR ? IS NULL OR ? = '' OR ? = '0') AND " +
                "(Etat LIKE ? OR ? IS NULL OR ? = '') AND " +
                "(Prix = ? OR ? IS NULL OR ? = '' OR ? = '0') AND " +
                "(Description LIKE ? OR ? IS NULL OR ? = '') AND " +
                "(Etage = ? OR ? IS NULL OR ? = '' OR ? = '0') " +
                "ORDER BY NumeroChambre";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + filterTextNumeroChambre + "%");
            statement.setString(2, filterTextNumeroChambre);
            statement.setString(3, filterTextNumeroChambre);
            statement.setString(4, "%" + filterTextTypeChambre + "%");
            statement.setString(5, filterTextTypeChambre);
            statement.setString(6, filterTextTypeChambre);
            setIntOrZero(statement, 7, filterTextCapacite);
            setIntOrZero(statement, 8, filterTextCapacite);
            setIntOrZero(statement, 9, filterTextCapacite);
            setIntOrZero(statement, 10, filterTextCapacite);
            statement.setString(11, "%" + filterTextEtat + "%");
            statement.setString(12, filterTextEtat);
            statement.setString(13, filterTextEtat);
            setDoubleOrZero(statement, 14, filterTextPrix);
            setDoubleOrZero(statement, 15, filterTextPrix);
            setDoubleOrZero(statement, 16, filterTextPrix);
            setDoubleOrZero(statement, 17, filterTextPrix);
            statement.setString(18, "%" + filterTextDescription + "%");
            statement.setString(19, filterTextDescription);
            statement.setString(20, filterTextDescription);
            setIntOrZero(statement, 21, filterTextEtage);
            setIntOrZero(statement, 22, filterTextEtage);
            setIntOrZero(statement, 23, filterTextEtage);
            setIntOrZero(statement, 24, filterTextEtage);

            try (ResultSet resultSet = statement.executeQuery()) {
                StringBuilder filteredDetails = new StringBuilder("<div style='font-size: 20px; color: #8AAEE0; text-align: center;'><br>Détails des Chambres (Filtré) :  <br><br></div>");

                while (resultSet.next()) {
                    int roomNumber = resultSet.getInt("NumeroChambre");
                    String roomType = resultSet.getString("TypeChambre");
                    int capacity = resultSet.getInt("Capacite");
                    String status = resultSet.getString("Etat");
                    double price = resultSet.getDouble("Prix");
                    String description = resultSet.getString("Description");
                    int floor = resultSet.getInt("Etage");

                    filteredDetails.append(folderPath ("Numéro de Chambre : ")).append(roomNumber).append("<br>");
                    filteredDetails.append(folderPath ("Type : ")).append(roomType).append("<br>");
                    filteredDetails.append(folderPath ("Capacité : ")).append(capacity).append("<br>");
                    filteredDetails.append(folderPath ("État : ")).append(status).append("<br>");
                    filteredDetails.append(folderPath ("Prix : ")).append(price).append("<br>");
                    filteredDetails.append(folderPath ("Description : ")).append(description).append("<br>");
                    filteredDetails.append(folderPath ("Étage : ")).append(floor).append("<br>");
                    filteredDetails.append("<br>");
                    filteredDetails.append("<br>");
                }

                roomDetailsTextArea.setText(filteredDetails.toString());
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des détails des chambres filtrés.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    // Rafraîchir l'interface pour afficher les nouveaux résultats filtrés
    revalidate();
    repaint();
}



    //AFFICHER LES CHAMBRES


    private void displayRoomDetails() {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
            String sql = "SELECT * FROM Chambres ORDER BY NumeroChambre";       
                 try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {

                    StringBuilder details = new StringBuilder("<div style='font-size: 20px; color: #8AAEE0; text-align: center;'><br>Détails des Chambres :  <br><br></div>");

                    while (resultSet.next()) {
                    int roomNumber = resultSet.getInt("NumeroChambre");
                    String roomType = resultSet.getString("TypeChambre");
                    int capacity = resultSet.getInt("Capacite");
                    String status = resultSet.getString("Etat");
                    String folderPath = resultSet.getString("folderPath");
                    double price = resultSet.getDouble("Prix");
                    String description = resultSet.getString("Description");
                    int floor = resultSet.getInt("Etage");
                    details.append(folderPath ("Numéro de Chambre : ")).append(roomNumber).append("<br>");
                    details.append(folderPath ("Type : ")).append(roomType).append("<br>");
                    details.append(folderPath ("Capacité : ")).append(capacity).append("<br>");
                    details.append(folderPath ("État : ")).append(status).append("<br>");
                    details.append(folderPath ("Prix : ")).append(price).append("  dh").append("<br>");
                    details.append(folderPath ("Description : ")).append(description).append("<br>");
                    details.append(folderPath ("Étage : ")).append(floor).append("<br>");
                   
                    String folderLink = folderPath != null && !folderPath.isEmpty() ? "<a href='" + folderPath + "'> voir les images </a>" : "Aucun dossier disponible";
                    details.append(folderPath (folderLink)).append("<br>");
                   details.append("<br>");
                   details.append("<br>");
                                }
                    roomDetailsTextArea.setText(details.toString());
    
                roomDetailsTextArea.addHyperlinkListener(e -> {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        // Lorsque le lien est activé, ouvrez le fichier ou le dossier
                        String clickedText = e.getDescription();
                        if (isValidFilePath(clickedText)) {
                            openFileOrFolder(clickedText);
                        }
                    }
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des détails des chambres.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    
        revalidate();
        repaint();
    }
    private boolean isValidFilePath(String path) {
        File file = new File(path);
        return file.exists();
    }
    
    private void openFileOrFolder(String path) {
        try {
            File file = new File(path);
            if (file.isFile()) {
                Desktop.getDesktop().open(file);
            } else if (file.isDirectory()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ouverture du fichier ou du dossier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void reserveRoom() {
        ReservationDialog ReservationDialog = new ReservationDialog(this);
        ReservationDialog.setVisible(true);    }

    private void addRoom() {
        AddRoomDialog addRoomDialog = new AddRoomDialog(this);
        addRoomDialog.setVisible(true);
    }

    private void editRoom() {
        EditRoomDialog editRoomDialog = new EditRoomDialog(this);
        editRoomDialog.setVisible(true);
    }

    private void deleteRoom() {
        DeleteRoomDialog deleteRoomDialog = new DeleteRoomDialog(this);
        deleteRoomDialog.setVisible(true);
    }
    private void  deleteReservation() {
        DeleteRervationDialog DeleteRervationDialog = new DeleteRervationDialog(this);
        DeleteRervationDialog.setVisible(true);
    }

    private void  editProfil() {
        EditAdmin EditAdmin = new EditAdmin(this);
        EditAdmin.setVisible(true);
    }
   
    
    private String folderPath (String text) {
        return "<span style='color: #3C5772; font-size: 13px; font-weight: bold;'>" + text + "</span>";
    }
   

    

    private void displayReservedRoomDetails() {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
            String reservedRoomsSql = "SELECT c.NUMEROCHAMBRE, c.TYPECHAMBRE, c.CAPACITE, c.ETAT, c.FOLDERPATH, c.PRIX, c.DESCRIPTION, c.ETAGE, " +
                    "r.IDRESERVATION, r.NOMCLIENT, r.PRENOMCLIENT, r.DATENAISSANCE, " +
                    "r.CIN, r.ADRESSE, r.EMAILCLIENT, r.TELEPHONECLIENT, r.DUREE, r.DATEARRIVEE, r.DATEDEPART, " +
                    "r.PRIXCHAMBRE, r.PRIXTOTAL " +
                    "FROM Chambres c " +
                    "INNER JOIN Reservations r ON c.NUMEROCHAMBRE = r.NUMEROCHAMBRE";
    
            try (PreparedStatement reservedRoomsStatement = connection.prepareStatement(reservedRoomsSql);
                 ResultSet reservedRoomsResultSet = reservedRoomsStatement.executeQuery()) {
                StringBuilder reservedRoomsDetails = new StringBuilder("<div style='font-size: 20px; color: #8AAEE0; text-align: center;'><br>Détails des Réservations :  <br><br></div>");
                while (reservedRoomsResultSet.next()) {
                    int roomNumber = reservedRoomsResultSet.getInt("NUMEROCHAMBRE");
                    String roomType = reservedRoomsResultSet.getString("TYPECHAMBRE");
                    int capacity = reservedRoomsResultSet.getInt("CAPACITE");
                    String status = reservedRoomsResultSet.getString("ETAT");
                    String folderPath = reservedRoomsResultSet.getString("FOLDERPATH");
                    double price = reservedRoomsResultSet.getDouble("PRIX");
                    String description = reservedRoomsResultSet.getString("DESCRIPTION");
                    int floor = reservedRoomsResultSet.getInt("ETAGE");
    
                    
                    reservedRoomsDetails.append(folderPath ("Numéro de Réservation : ")).append(reservedRoomsResultSet.getInt("IDRESERVATION")).append("<br>");
                    reservedRoomsDetails.append(folderPath ("Nom du Client : ")).append(reservedRoomsResultSet.getString("NOMCLIENT")).append("<br>");     
                    reservedRoomsDetails.append(folderPath ("Prénom du Client : ")).append(reservedRoomsResultSet.getString("PRENOMCLIENT")).append("<br>");              
                    reservedRoomsDetails.append(folderPath ("Date de Naissance du Client : ")).append(reservedRoomsResultSet.getString("DATENAISSANCE")).append("<br>");
                    reservedRoomsDetails.append(folderPath ("CIN du Client :")).append(reservedRoomsResultSet.getString("CIN")).append("<br>");
                    reservedRoomsDetails.append(folderPath ("Adresse du Client : ")).append(reservedRoomsResultSet.getString("ADRESSE")).append("<br>");
                    reservedRoomsDetails.append(folderPath ("Email du Client : ")).append(reservedRoomsResultSet.getString("EMAILCLIENT")).append("<br>");
                    reservedRoomsDetails.append(folderPath ("Téléphone du Client : ")).append(reservedRoomsResultSet.getString("TELEPHONECLIENT")).append("<br>");
                    reservedRoomsDetails.append(folderPath ("Durée de la Réservation : ")).append(reservedRoomsResultSet.getInt("DUREE")).append(" jours").append("<br>");
                    reservedRoomsDetails.append(folderPath ("Date d'Arrivée : ")).append(reservedRoomsResultSet.getString("DATEARRIVEE")).append("<br>");
                    reservedRoomsDetails.append(folderPath ("Date de Départ : ")).append(reservedRoomsResultSet.getString("DATEDEPART")).append("<br>");
                    reservedRoomsDetails.append(folderPath ("Numéro de Chambre : ")).append(roomNumber).append("<br>");
                    reservedRoomsDetails.append(folderPath ("Type : ")).append(roomType).append("<br>");
                    reservedRoomsDetails.append(folderPath ("Capacité : ")).append(capacity).append("<br>");
                    reservedRoomsDetails.append(folderPath ("État : ")).append(status).append("<br>");
                    reservedRoomsDetails.append(folderPath ("Prix de la chambres pour un jour: ")).append(price).append("  dh").append("<br>");
                    reservedRoomsDetails.append(folderPath ("Description : ")).append(description).append("<br>");
                    reservedRoomsDetails.append(folderPath ("Étage : ")).append(floor).append("<br>");
                    reservedRoomsDetails.append(folderPath ("Prix Total : ")).append(reservedRoomsResultSet.getDouble("PRIXTOTAL")).append("  dh").append("<br>");
                    
                    String folderLink = folderPath != null && !folderPath.isEmpty() ? "<a href='" + folderPath + "'> voir les images </a>" : "Aucun dossier disponible";
                    reservedRoomsDetails.append(folderPath (folderLink)).append("<br>");
                    reservedRoomsDetails.append("<br>");
                    reservedRoomsDetails.append("<br>");

                }
    
                // Afficher les détails dans votre interface utilisateur (remplacez ceci par votre logique d'affichage)
                roomDetailsTextArea.setText(reservedRoomsDetails.toString());
    
                roomDetailsTextArea.addHyperlinkListener(e -> {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        // Lorsque le lien est activé, ouvrez le fichier ou le dossier
                        String clickedText = e.getDescription();
                        if (isValidFilePath(clickedText)) {
                            openFileOrFolder(clickedText);
                        }
                    }
                });

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Ajoutez également un message pour afficher l'erreur dans la console
            System.err.println("Erreur SQL : " + ex.getMessage());
        }
    }
    
    
    
    
                                    
//afficher les statistique
private void showStatistics() {
    try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "aya__aatfaoui", "2005")) {
        String totalRoomsQuery = "SELECT COUNT(*) FROM Chambres";
        String reservedRoomsQuery = "SELECT COUNT(*) FROM Chambres WHERE Etat = 'Reserve'";

        try (PreparedStatement totalRoomsStatement = connection.prepareStatement(totalRoomsQuery);
             PreparedStatement reservedRoomsStatement = connection.prepareStatement(reservedRoomsQuery);
             ResultSet totalRoomsResultSet = totalRoomsStatement.executeQuery();
             ResultSet reservedRoomsResultSet = reservedRoomsStatement.executeQuery()) {

            if (totalRoomsResultSet.next() && reservedRoomsResultSet.next()) {
                int totalRooms = totalRoomsResultSet.getInt(1);
                int reservedRooms = reservedRoomsResultSet.getInt(1);

                double reservationPercentage = (double) reservedRooms / totalRooms * 100;

                String statisticsMessage = String.format("Statistiques :\n\nTotal des Chambres : %d\nChambres Reservées : %d\nPourcentage de Réservation : %.2f%%",
                        totalRooms, reservedRooms, reservationPercentage);

                JOptionPane.showMessageDialog(this, statisticsMessage, "Statistiques", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des statistiques.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionChambres().setVisible(true));
    }
}
            


