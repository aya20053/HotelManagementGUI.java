import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date; 
import com.itextpdf.text.BaseColor;
import java.awt.image.BufferedImage;

public class ReservationDialog extends JDialog {

    private JTextField roomNumberField;
    private JTextField guestNameField;
    private JTextField emailField;
    private JTextField phoneNumberField;
    private JTextField firstNameField;
    private JTextField birthDateField;
    private JTextField addressField;
    private JTextField durationField;
    private JTextField arrivalDateField;
    private JTextField departureDateField;
    private JTextField roomPriceField;
    private JTextField remainingAmountField;
    private JButton reserveButton;
    private JTextField cashGivenField;
    private JTextField changeField;
    private JTextField cinField;
    private String reservationGuestName;
    private int reservationRoomNumber;
    public ReservationDialog(Frame parent) {
        super(parent, "Réserver une Chambre", true);
  try {
            File iconFile = new File("C:/Users/hp/OneDrive/Documents/pfe_2/k.jpeg"); // Remplacez par le chemin réel de votre icône
            BufferedImage iconImage = ImageIO.read(iconFile);

            // Changer l'icône de la JFrame
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel(new GridLayout(20, 6, 4, 3));
        panel.setBorder(new EmptyBorder(20, 20,20,20));

     
        JLabel clientTitleLabel = new JLabel("Infos sur le Client");
        clientTitleLabel.setFont(new Font("Arial", Font.BOLD, 19));
        panel.add(clientTitleLabel);
        panel.add(new JLabel());

        panel.add(new JLabel("Nom du Client:"));
        guestNameField = new JTextField();
        panel.add(guestNameField);

        panel.add(new JLabel("Prénom du Client:"));
        firstNameField = new JTextField();
        panel.add(firstNameField);

        panel.add(new JLabel("Date de Naissance:"));
        birthDateField = new JTextField();
        panel.add(birthDateField);

        panel.add(new JLabel("CIN du Client:"));
        cinField = new JTextField();
        panel.add(cinField);

        panel.add(new JLabel("Adresse:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("Email du Client:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Numéro de Téléphone:"));
        phoneNumberField = new JTextField();
        panel.add(phoneNumberField);

        JLabel reservationTitleLabel2 = new JLabel("Infos sur la Réservation");
        reservationTitleLabel2.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(reservationTitleLabel2);
        panel.add(new JLabel(""));
        
        panel.add(new JLabel("Numéro de Chambre:"));
        roomNumberField = new JTextField();
        panel.add(roomNumberField);

        panel.add(new JLabel("Durée du Séjour (en jours):"));
        durationField = new JTextField();
        panel.add(durationField);

        panel.add(new JLabel("Date d'Arrivée (YYYY-MM-DD):"));
        arrivalDateField = new JTextField();
        panel.add(arrivalDateField);

        panel.add(new JLabel("Date de Départ (YYYY-MM-DD):"));
        departureDateField = new JTextField();
        panel.add(departureDateField);

        panel.add(new JLabel("Prix de la Chambre:"));
        roomPriceField = new JTextField();
        panel.add(roomPriceField);

        panel.add(new JLabel("Prix total:"));
        remainingAmountField = new JTextField();
        remainingAmountField.setEditable(false);
        panel.add(remainingAmountField);

        panel.add(new JLabel("Espèces Données:"));
        cashGivenField = new JTextField();
        panel.add(cashGivenField);

        panel.add(new JLabel("Montant Rendu:"));
        changeField = new JTextField();
        panel.add(changeField);

        Color buttonColor = Color.decode("#8AAEE0");

        reserveButton = new JButton("Réserver");
        reserveButton.setBackground(buttonColor);
        panel.add(reserveButton);
        reserveButton.addActionListener(e -> reserveRoom());

        JButton cancelButton = new JButton("Annuler");
        cancelButton.setBackground(buttonColor);
    
        cancelButton.addActionListener(e -> dispose());
        panel.add(cancelButton);

        roomPriceField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTotalPrice();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTotalPrice();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTotalPrice();
            }
        });

        add(panel, BorderLayout.CENTER);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setSize(700, 700);

        setLocationRelativeTo(parent);
    }

    private void updateTotalPrice() {
        try {
            double roomPrice = Double.parseDouble(roomPriceField.getText());
            int duration = Integer.parseInt(durationField.getText());

            double totalPrice = roomPrice * duration;
            remainingAmountField.setText(String.valueOf(totalPrice));
        } catch (NumberFormatException ex) {
            remainingAmountField.setText("");
        }
    }

    private boolean isRoomAvailable(int roomNumber) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT Etat FROM Chambres WHERE NumeroChambre = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, roomNumber);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String etat = resultSet.getString("Etat");
                    return !etat.equals("Reserve");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "aya__aatfaoui", "2005");
    }

    private void reserveRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            if (!isRoomAvailable(roomNumber)) {
                showError("La chambre spécifiée n'existe pas ou est déjà réservée.");
                return;
            }
            if (!validateFields()) {
                return;
            }

            String guestName = guestNameField.getText();
            String firstName = firstNameField.getText();
            String birthDate = birthDateField.getText();
            String address = addressField.getText();
            String cin = cinField.getText();
            String email = emailField.getText();
            String phoneNumber = phoneNumberField.getText();
            int duration = Integer.parseInt(durationField.getText());
            String arrivalDate = arrivalDateField.getText();
            String departureDate = departureDateField.getText();
            double roomPrice = Double.parseDouble(roomPriceField.getText());

            double totalPrice = roomPrice * duration;

            reservationGuestName = guestName;
            reservationRoomNumber = roomNumber;

            try (Connection connection = getConnection()) {
                connection.setAutoCommit(false); // Début de la transaction

                insertReservation(connection, roomNumber, guestName, firstName, birthDate, cin, address, email, phoneNumber, duration, arrivalDate, departureDate, roomPrice, totalPrice);
                updateRoomState(connection, roomNumber);

                connection.commit(); 
                showMessage("Chambre réservée avec succès.", "Réservation Réussie");
                generateInvoice(guestName, roomNumber, duration, arrivalDate, departureDate, roomPrice, totalPrice);
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                showError("Erreur lors de la réservation de la chambre.");
            }
        } catch (NumberFormatException e) {
            showError("Veuillez entrer des valeurs valides pour le numéro de chambre, la durée et le prix de la chambre.");
        }
    }

    private void insertReservation(Connection connection, int roomNumber, String guestName, String firstName, String birthDate, String cin, String address, String email, String phoneNumber, int duration, String arrivalDate, String departureDate, double roomPrice, double totalPrice) throws SQLException {
        String sql = "INSERT INTO Reservations (NumeroChambre, NomClient, PrenomClient, DateNaissance, CIN, Adresse, EmailClient, TelephoneClient, Duree, DateArrivee, DateDepart, PrixChambre, PrixTotal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomNumber);
            statement.setString(2, guestName);
            statement.setString(3, firstName);
            statement.setString(4, birthDate);
            statement.setString(5, cin);
            statement.setString(6, address);
            statement.setString(7, email);
            statement.setString(8, phoneNumber);
            statement.setInt(9, duration);
            statement.setString(10, arrivalDate);
            statement.setString(11, departureDate);
            statement.setDouble(12, roomPrice);
            statement.setDouble(13, totalPrice);

            statement.executeUpdate();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private boolean validateFields() {
        // Ajoutez ici les validations nécessaires
        return true;
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateRoomState(Connection connection, int roomNumber) {
        String updateSql = "UPDATE Chambres SET Etat = 'Reserve' WHERE NumeroChambre = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            updateStatement.setInt(1, roomNumber);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateInvoice(String guestName, int roomNumber, int duration, String arrivalDate, String departureDate, double roomPrice, double totalPrice) {
        try (Connection connection = getConnection()) {
             LocalDate reservationDate = LocalDate.now();
        
        // Obtenez l'heure actuelle
        LocalTime reservationTime = LocalTime.now();
            String invoiceContent = createInvoiceContent(guestName, roomNumber, duration, arrivalDate, departureDate, roomPrice, totalPrice, reservationDate, reservationTime);
            generatePdfInvoice(invoiceContent, guestName);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur lors de la génération de la facture.");
        }
    }

   
    
    
    private String addInvoiceLine(String label, String value) {
        return label + ": " + value + "\n \n";
    }
  
    
    


    private String createInvoiceContent(String guestName, int roomNumber, int duration, String arrivalDate, String departureDate, double roomPrice, double totalPrice , LocalDate reservationDate, LocalTime reservationTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = dateFormat.format(new Date());
    
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
        String invoiceContent = "Votre réservation est confirmée le " + currentDate + " à " + " " + reservationTime.format(timeFormatter) + "\n\n";
    
        invoiceContent += addInvoiceLine("Nom  ", guestName );
        invoiceContent += addInvoiceLine(" Prénom",  firstNameField.getText() );
        invoiceContent += addInvoiceLine("Numéro de Chambre", String.valueOf(roomNumber));
        invoiceContent += addInvoiceLine("Date de Naissance", birthDateField.getText()); // Ajoutez la date de naissance
        invoiceContent += addInvoiceLine("CIN", cinField.getText()); // Ajoutez le numéro de CIN
        invoiceContent += addInvoiceLine("Numéro de Téléphone", phoneNumberField.getText()); // Ajoutez le numéro de téléphone
        invoiceContent += addInvoiceLine("Adresse", addressField.getText()); // Ajoutez l'adresse
        invoiceContent += addInvoiceLine("Durée du Séjour", duration + " jours");
        invoiceContent += addInvoiceLine("Date d'Arrivée", arrivalDate);
        invoiceContent += addInvoiceLine("Date de Départ", departureDate);
        invoiceContent += addInvoiceLine("Prix de la Chambre", String.valueOf(roomPrice));
        invoiceContent += addInvoiceLine("Montant Total", String.valueOf(totalPrice));
        invoiceContent += addInvoiceLine("Réglé par", "ESPECE");

        return invoiceContent;
    }
    
    
    private void generatePdfInvoice(String invoiceContent, String guestName) {
        Document document = new Document();
    
        try {
            String pdfPath = "C:/Users/hp/OneDrive/Documents/pfe_2/" + guestName + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
    
            document.open();
    
            // Ajoutez le contenu de la facture
            Paragraph invoiceParagraph = new Paragraph(invoiceContent);
            document.add(invoiceParagraph);
    
            // Ajoutez un paragraphe de remerciement
 
           com.itextpdf.text.Font thankYouFont = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLUE);
           
           Paragraph regler=new Paragraph(" \n");
           Paragraph thankYouParagraph = new Paragraph("Merci, " + guestName + ", de choisir notre hôtel. Nous espérons vous revoir bientôt!", thankYouFont);
        thankYouParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(regler);
        document.add(thankYouParagraph);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
            JOptionPane.showMessageDialog(this, "Facture générée avec succès au format PDF.", "Facture", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
  

    
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test ReservationDialog");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton openDialogButton = new JButton("Open Reservation Dialog");
            openDialogButton.addActionListener(e -> {
                ReservationDialog reservationDialog = new ReservationDialog(frame);
                reservationDialog.setVisible(true);
            });

            frame.getContentPane().add(openDialogButton);
            frame.setSize(300, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
