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
import lombok.extern.slf4j.Slf4j;


@Aspect
@Component
@Slf4j
public class MailAspect {

    @Autowired
    private JavaMailSender mailSender;
    
    // Our pointcut just says, apply this advice to any method annotated with @SendEmailConfirmation
    @AfterReturning(pointcut = "@annotation(SendEmailConfirmation)", returning = "emailInfo")
    public void sendConfirmRegistrationEmail(MailInfo emailInfo) throws Throwable {
        var userEmail = emailInfo.email();
        var token = emailInfo.token();
        try {
            var emailAddress = new InternetAddress(userEmail);
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {
                    mimeMessage.setSubject("Confirm Account");
                    mimeMessage.setRecipient(Message.RecipientType.TO, emailAddress);
                    mimeMessage.setFrom(new InternetAddress("admin@wagercomrade.com"));
                    mimeMessage.setText(createEmail(token), "utf-8");
                }
            };
            try {
                mailSender.send(preparator);
            } catch (MailException mailEx) {
                log.error("Failed to send confirmation sign up email", mailEx);
            }
        } catch (AddressException addressEx) {
            log.error("Failed to create email address. Format is bad.", addressEx);
        }
    }

    private String createEmail(String token) {
        var message = 
            "\n\nPlease use the code to confirm your account: " + token 
            + "\n\nThis code will expire in 15 minutes"
            + "\n\nIf the code has expired please visit the login page and login using your username\nand password"
            + " and a new code will be sent."
            + "\n\n- the Wager Comrade team";

        return message;
    }

}
