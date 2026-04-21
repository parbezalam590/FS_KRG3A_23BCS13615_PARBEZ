package com.ecotrack.livepoll.auth;

import com.ecotrack.livepoll.model.AppUser;
import com.ecotrack.livepoll.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public OAuth2SuccessHandler(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        org.springframework.security.core.Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("OAuth user not found after login"));

        String token = jwtService.generateToken(
                User.withUsername(appUser.getEmail())
                        .password("oauth2-user")
                        .authorities(appUser.getRole().name())
                        .build(),
                appUser.getRole().name()
        );

        String redirect = UriComponentsBuilder
                .fromUriString(frontendUrl + "/oauth-success")
                .queryParam("token", token)
                .queryParam("role", appUser.getRole().name())
                .queryParam("email", appUser.getEmail())
                .build()
                .toUriString();

        response.sendRedirect(redirect);
    }
}
