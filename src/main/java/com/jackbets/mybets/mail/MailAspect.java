package com.jackbets.mybets.mail;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


@Aspect
@Component
public class MailAspect {

    private JavaMailSender mailSender;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @After("@annotation(ConfirmRegister)")
    public Object SendRegistrationEmail() {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(null, null);
                mimeMessage.setFrom(new InternetAddress("mail@pizzaland.com"));
                mimeMessage.setText("Confirm your sign up");
            }
        };

        try {
            this.mailSender.send(preparator);
        } catch (MailException ex) {
            // TODO: handle exception
            System.err.println(ex.getMessage());
        }
        return null;
    }

}
