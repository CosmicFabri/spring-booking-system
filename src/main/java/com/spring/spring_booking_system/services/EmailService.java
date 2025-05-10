package com.spring.spring_booking_system.services;

import com.spring.spring_booking_system.entities.Booking;
import com.spring.spring_booking_system.entities.EmailDetails;

import java.io.File;

import com.spring.spring_booking_system.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import com.spring.spring_booking_system.exceptions.EmailSendingException;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendConfirmationEmail(User user, Booking booking, String spaceName) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(user.getEmail());
            helper.setSubject("Confirmación de reserva");

            String htmlMsg = String.format("""
            <html>
                <body>
                    <h3>Hola, %s. Te confirmamos tu reservación</h3>
                    <p>Detalles de la reserva:</p>
                    <ul>
                        <li><strong>Espacio:</strong> %s</li>
                        <li><strong>Fecha:</strong> %s</li>
                        <li><strong>Hora de inicio:</strong> %s</li>
                        <li><strong>Hora de fin:</strong> %s</li>
                    </ul>
                </body>
            </html>
            """,
                    user.getFullName(),
                    spaceName,
                    booking.getDate(),
                    booking.getStartTime(),
                    booking.getEndTime()
            );

            helper.setText(htmlMsg, true);

            // Send the email
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailSendingException(e);
        }
    }

    @Async
    public void sendSimpleMail(EmailDetails details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            // Configure the message
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Send the email
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            throw new EmailSendingException(e);
        }
    }

    @Async
    public void sendMailWithAttachment(EmailDetails details) {
        // Creating a mime message
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            // Setting multipart as true for attachments to be send
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            // Adding the attachment
            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(file.getFilename(), file);

            // Send the email
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailSendingException(e);
        }
    }
}
