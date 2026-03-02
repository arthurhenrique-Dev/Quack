package com.quack.quack_app.Infra.Adapters.Output.Gateways.TwoFAGateway;

import com.quack.quack_app.Application.Ports.Output.Services.TwoFAService;
import com.quack.quack_app.Domain.ValueObjects.Email;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TwoFAServiceAdapter implements TwoFAService {

    private final GoogleAuthenticator authenticator;

    public TwoFAServiceAdapter() {
        this.authenticator = new GoogleAuthenticator();
    }

    @Override
    public String setupTwoFa() {
        final GoogleAuthenticatorKey key = authenticator.createCredentials();

        return key.getKey();
    }

    @Override
    public boolean checkCode(String secret, String code) {
        if (secret == null || code == null || code.length() != 6) {
            return false;
        }

        try {
            return authenticator.authorize(secret, Integer.parseInt(code));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String getQrCodeUrl(String secret, UUID id) {
        GoogleAuthenticatorKey key = new GoogleAuthenticatorKey.Builder(secret).build();
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL("QuackApp", id.toString(), key);
    }
}