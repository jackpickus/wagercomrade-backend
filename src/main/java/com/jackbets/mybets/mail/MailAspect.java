package com.jackbets.mybets.mail;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import jakarta.mail.Message;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


@Aspect
@Component
public class MailAspect {

    @Autowired
    private JavaMailSender mailSender;
    
    // Our pointcut just says, apply this advice to any method annotated with @SendEmailConfirmation
    @AfterReturning("@annotation(SendEmailConfirmation)")
    public void sendConfirmRegistrationEmail() throws Throwable {
        var emailArgs = "email@email.com";
        try {
            var emailAddress = new InternetAddress(emailArgs.toString());
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {
                    mimeMessage.setRecipient(Message.RecipientType.TO, emailAddress);
                    mimeMessage.setFrom(new InternetAddress("mail@pizzaland.com"));
                    mimeMessage.setText("Confirm your sign up");
                }
            };
            try {
                System.out.println("\n\nSend the sign up email\n\n");
                this.mailSender.send(preparator);
            } catch (MailException mailEx) {
                // TODO: handle exception
                System.err.println(mailEx.getMessage());
            }
        } catch (AddressException ex) {
            // TODO: handle exception
            System.err.println(ex.getMessage());
        }
    }

}
