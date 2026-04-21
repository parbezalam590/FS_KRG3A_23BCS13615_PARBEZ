package com.ecotrack.livepoll.service;

import com.ecotrack.livepoll.model.AppUser;
import com.ecotrack.livepoll.model.AuthProvider;
import com.ecotrack.livepoll.model.Role;
import com.ecotrack.livepoll.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException("Email not found from OAuth provider");
        }

        AppUser user = userRepository.findByEmail(email).orElseGet(() -> {
            AppUser newUser = new AppUser();
            newUser.setEmail(email);
            newUser.setName(name == null ? "Google User" : name);
            newUser.setRole(Role.ROLE_USER);
            newUser.setProvider(AuthProvider.GOOGLE);
            return userRepository.save(newUser);
        });

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return new DefaultOAuth2User(List.of(authority), oAuth2User.getAttributes(), "email");
    }
}
