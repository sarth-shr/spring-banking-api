package com.example.foneproject.service.impl;

import com.example.foneproject.entity.UserCredentials;
import com.example.foneproject.repository.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserCredentialsRepository userCredentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<UserCredentials> userCredentialsOptional = userCredentialsRepository.findByEmail(email);
        if (userCredentialsOptional.isEmpty()) {
            throw new UsernameNotFoundException("Bad credentials");
        }
        UserCredentials userCredentials = userCredentialsOptional.get();
        return new User(
                userCredentials.getEmail(),
                userCredentials.getPassword(),
                userCredentials.isEnabled(),
                true,
                true,
                true,
                userCredentials.getAuthorities());
    }
}
