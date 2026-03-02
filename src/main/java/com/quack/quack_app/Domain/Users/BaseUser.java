package com.quack.quack_app.Domain.Users;

import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import com.quack.quack_app.Domain.ValueObjects.TokenUpdater;
import com.quack.quack_app.Domain.ValueObjects.Email;
import com.quack.quack_app.Domain.ValueObjects.Password;
import com.quack.quack_app.Domain.ValueObjects.TwoFA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public abstract class BaseUser {

    private static final Logger log = LoggerFactory.getLogger(BaseUser.class);

    private final UUID id;
    private Password password;
    private Email email;
    protected Role role;
    private Status status;
    private TokenUpdater passwordUpdater;
    private TokenUpdater emailUpdater;
    private TwoFA twoFA;

    public BaseUser(UUID id, Password password, Email email, Role role, Status status, TokenUpdater passwordUpdater, TokenUpdater emailUpdater, TwoFA twoFA) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
        this.passwordUpdater = passwordUpdater;
        this.emailUpdater = emailUpdater;
        this.twoFA = twoFA;
    }

    public BaseUser(Password password, Email email) {
        this.id = UUID.randomUUID();
        this.password = password;
        this.email = email;
        this.passwordUpdater = null;
        this.emailUpdater = null;
        this.status = Status.PENDING;
        this.twoFA = TwoFA.disabled();
    }

    public void checkAndChangePassword(UUID token, Password password) {
        passwordUpdater.Check(token.toString());
        this.password = password;
    }

    public void prepareTwoFA(String secret){
        this.twoFA = new TwoFA(false, secret);
    }

    public void enableTwoFA() {
        if (this.twoFA.secret() == null) {
            var e = new ValidationFailedException("Cannot enable 2FA without a generated secret.");
            log.warn(e.getMessage(), e);
            throw e;
        }
        this.twoFA = new TwoFA(true, this.twoFA.secret());
    }

    public void checkAndChangeEmail(UUID token, Email email) {
        passwordUpdater.Check(token.toString());
        this.email = email;
    }

    public void sendUpdateEmailToken(){
        this.emailUpdater.Start();
    }
    public void sendUpdatePasswordToken() {
        if (this.twoFA != null && this.twoFA.enabled()) {
            this.passwordUpdater = TokenUpdater.Start();
        } else {
            var e = new ProcessingErrorException("2FA is required for this action.");
            log.warn("Security failure for User [{}]: 2FA is not enabled.", id, e);
            throw e;
        }
    }

    public void ban() {
        this.status = this.status.ban();
    }

    public void activate() {
        this.status = this.status.activate();
    }

    public UUID getId() { return id; }
    public Password getPassword() { return password; }
    public Email getEmail() { return email; }
    public TwoFA getTwoFA() { return twoFA; }
    public TokenUpdater getEmailUpdater() { return emailUpdater; }
    public Role getRole() { return role; }
    public Status getStatus() { return status; }
    public TokenUpdater getPasswordUpdater() { return passwordUpdater; }
}