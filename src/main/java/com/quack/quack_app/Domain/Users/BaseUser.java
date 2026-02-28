package com.quack.quack_app.Domain.Users;

import com.quack.quack_app.Domain.Exceptions.ProcessingErrorException;
import com.quack.quack_app.Domain.ValueObjects.PasswordUpdater;
import com.quack.quack_app.Domain.ValueObjects.Email;
import com.quack.quack_app.Domain.ValueObjects.Password;
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
    private PasswordUpdater passwordUpdater;

    public BaseUser(UUID id, Password password, Email email, Role role, Status status, PasswordUpdater passwordUpdater, boolean twoFactorAuthEnabled) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
        this.passwordUpdater = passwordUpdater;
        this.twoFactorAuthEnabled = twoFactorAuthEnabled;
    }

    public BaseUser(Password password, Email email) {
        this.id = UUID.randomUUID();
        this.password = password;
        this.email = email;
        this.passwordUpdater = null;
    }

    public void checkAndChangePassword(UUID token, Password password) {
        passwordUpdater.Check(token.toString());
        this.password = password;
    }

    public void changeEmail(Email email) {
        this.email = email;
    }

    public void sendUpdatePasswordToken() {
        try {
            if (twoFactorAuthEnabled) {
                this.passwordUpdater = PasswordUpdater.Start();
            } else {
                throw new IllegalStateException("Two-Factor Authentication Required");
            }
        } catch (Exception e) {
            log.error("Security failure for User [{}]: 2FA is not enabled for password reset.", id, e);
            throw new ProcessingErrorException("Password reset is prohibited because two-factor Authentication is not active for this account.");
        }
    }

    public void twoFactorAuthEnabled() {
        this.twoFactorAuthEnabled = true;
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
    public Role getRole() { return role; }
    public Status getStatus() { return status; }
    public PasswordUpdater getPasswordUpdater() { return passwordUpdater; }
    public boolean isTwoFactorAuthEnabled() { return twoFactorAuthEnabled; }
}