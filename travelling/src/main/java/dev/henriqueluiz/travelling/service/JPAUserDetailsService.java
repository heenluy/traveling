package dev.henriqueluiz.travelling.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.henriqueluiz.travelling.model.AppUserDetails;
import dev.henriqueluiz.travelling.repository.UserRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JPAUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email)
            .map(AppUserDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found: '%s'", email)));
    }   
}
