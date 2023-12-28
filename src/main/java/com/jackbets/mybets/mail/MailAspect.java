package com.jackbets.mybets.mail;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
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

    private JavaMailSender mailSender;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    // Our pointcut just says, ‘Apply this advice to any method annotated with @ConfirmRegister
    @AfterReturning("@annotation(ConfirmRegister)")
    public void SendRegistrationEmail() throws Throwable {
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
                System.out.println(preparator.toString());
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
