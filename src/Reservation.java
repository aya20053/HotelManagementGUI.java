/*import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Reservation {
    // ... (code précédent)

    private static final String SENDER_EMAIL = "votre_adresse_email@gmail.com";
    private static final String SENDER_PASSWORD = "votre_mot_de_passe";

    public void sendConfirmationEmail() {
        String recipientEmail = this.email;
        String subject = "Confirmation de Réservation";
        String messageText = "Merci, " + this.guestName + ", de choisir notre hôtel. Nous espérons vous revoir bientôt!";

        try {
            // Configuration des propriétés pour le serveur SMTP
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            // Création d'une session avec l'authentification
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });

            // Création d'un objet MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setText(messageText);

            // Envoi du message
            Transport.send(message);

            System.out.println("Email envoyé avec succès.");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'envoi de l'email.");
        }
    }
}
*/