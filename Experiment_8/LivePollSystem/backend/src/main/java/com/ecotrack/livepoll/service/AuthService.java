package com.ecotrack.livepoll.service;

import com.ecotrack.livepoll.auth.AppUserPrincipal;
import com.ecotrack.livepoll.auth.JwtService;
import com.ecotrack.livepoll.dto.AuthDtos;
import com.ecotrack.livepoll.model.AppUser;
import com.ecotrack.livepoll.model.AuthProvider;
import com.ecotrack.livepoll.model.Role;
import com.ecotrack.livepoll.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email is already registered");
        }

        AppUser user = new AppUser();
        user.setEmail(request.email());
        user.setName(request.name());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);
        user.setProvider(AuthProvider.LOCAL);

        AppUser saved = userRepository.save(user);
        AppUserPrincipal principal = new AppUserPrincipal(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPassword(),
                saved.getRole()
        );
        String token = jwtService.generateToken(principal, saved.getRole().name());

        return new AuthDtos.AuthResponse(token, saved.getEmail(), saved.getName(), saved.getRole().name());
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AppUser user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        AppUserPrincipal principal = new AppUserPrincipal(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
        String token = jwtService.generateToken(principal, user.getRole().name());

        return new AuthDtos.AuthResponse(token, user.getEmail(), user.getName(), user.getRole().name());
    }
}
