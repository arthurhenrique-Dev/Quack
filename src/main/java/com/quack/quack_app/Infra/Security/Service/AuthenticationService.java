package com.quack.quack_app.Infra.Security.Service;

import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Mappers.SQLMapper;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Repositories.JpaUserRepository;
import com.quack.quack_app.Infra.Adapters.Output.Persistence.SQL.Repositories.JpaModeratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private JpaModeratorRepository moderatorRepository;

    @Autowired
    private SQLMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            var userDomain = mapper.toDomain(userEntity.get());
            return new UserDetailsImpl(userDomain);
        }

        var modEntity = moderatorRepository.findByEmail(email);
        if (modEntity.isPresent()) {
            var modDomain = mapper.toDomain(modEntity.get());
            return new UserDetailsImpl(modDomain);
        }

        throw new UsernameNotFoundException("User not found: " + email);
    }
}