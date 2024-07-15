package project.springboot.template.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import project.springboot.template.entity.Email;
import project.springboot.template.repository.EmailRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;

@Service
@Transactional
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;

    public EmailService(JavaMailSender emailSender, EmailRepository emailRepository) {
        this.emailSender = emailSender;
        this.emailRepository = emailRepository;
    }

    public boolean sendSimpleMessage(String sender, String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom(sender);
            message.setSentDate(Date.from(Instant.now()));
            emailSender.send(message);
            saveEmail(sender, to, subject, text, false);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void saveEmail(String sender, String to, String subject, String text, boolean isHTML) {
        Email email = Email.builder()
                .sender(sender)
                .content(text)
                .subject(subject)
                .isHtml(isHTML)
                .sentAt(Instant.now()).recipient(to)
                .build();
        emailRepository.save(email);
    }

    public boolean sendHtmlMessage(String sender, String to, String subject, String htmlContent) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // Set to true to indicate the text is HTML
            helper.setFrom(sender);
            helper.setSentDate(Date.from(Instant.now()));
            emailSender.send(message);
            this.saveEmail(sender, to, subject, htmlContent, true);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }
}
