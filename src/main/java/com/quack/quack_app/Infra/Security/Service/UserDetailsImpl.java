package com.quack.quack_app.Infra.Security.Service;

import com.quack.quack_app.Domain.Users.BaseUser;
import com.quack.quack_app.Domain.Users.Status;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserDetailsImpl implements UserDetails {

    private final BaseUser baseUser;

    public UserDetailsImpl(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + baseUser.getRole().name()));
    }

    @Override
    public @Nullable String getPassword() {
        return baseUser.getPassword().password();
    }

    @Override
    public String getUsername() {
        return baseUser.getEmail().email();
    }

    @Override
    public boolean isEnabled() {

        return baseUser.getStatus() == Status.ACTIVE;
    }

    public UUID getId() {
        return baseUser.getId();
    }
    public BaseUser getBaseUser() {
        return baseUser;
    }
}
