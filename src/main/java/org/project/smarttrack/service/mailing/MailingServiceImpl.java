package org.project.smarttrack.service.mailing;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailingServiceImpl implements IMailingService {

    private final JavaMailSender emailSender;

    public MailingServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public String sendActivateAccountEmail(String to, String subject, String text) {
        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        // Send Message!
        this.emailSender.send(message);

        return "Email Sent!";
    }
}
