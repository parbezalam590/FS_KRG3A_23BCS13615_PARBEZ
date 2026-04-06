package com.ecotrack.livepoll.service;

import com.ecotrack.livepoll.auth.AppUserPrincipal;
import com.ecotrack.livepoll.model.AppUser;
import com.ecotrack.livepoll.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new AppUserPrincipal(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword() == null ? "" : user.getPassword(),
                user.getRole()
        );
    }
}
