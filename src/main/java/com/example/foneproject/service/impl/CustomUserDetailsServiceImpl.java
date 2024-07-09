package com.example.foneproject.service.impl;

import com.example.foneproject.entity.UserCredentials;
import com.example.foneproject.repository.UserCredentialsRepository;
import com.example.foneproject.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private final UserCredentialsRepository userCredentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserCredentials> userCredentialsOptional = userCredentialsRepository.findByEmail(email);
        if (userCredentialsOptional.isEmpty()) {
            throw new UsernameNotFoundException("Email: " + email + " is invalid!");
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
