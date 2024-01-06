package com.jackbets.mybets.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailConfig {

    public JavaMailSender getJavaMailSender() {
        return new JavaMailSenderImpl();
    }
    
}
