package com.quack.quack_app.Infra.Adapters.Output.Gateways.EmailGateway;

import com.quack.quack_app.Application.Ports.Output.Services.EmailService;
import com.quack.quack_app.Domain.ValueObjects.Email;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailServiceAdapter implements EmailService {

    private final JavaMailSender mailSender;


    public EmailServiceAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendToken(String token, Email email, String subject) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("quack@verycoolemail.com");
        message.setTo(email.email());
        message.setSubject(subject);
        message.setText(token);

        mailSender.send(message);
    }
}
