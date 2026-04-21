package com.ecotrack.livepoll.config;

import com.ecotrack.livepoll.model.AppUser;
import com.ecotrack.livepoll.model.AuthProvider;
import com.ecotrack.livepoll.model.Role;
import com.ecotrack.livepoll.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@livepoll.com").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setEmail("admin@livepoll.com");
                admin.setName("LivePoll Admin");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setRole(Role.ROLE_ADMIN);
                admin.setProvider(AuthProvider.LOCAL);
                userRepository.save(admin);
            }

            if (userRepository.findByEmail("user@livepoll.com").isEmpty()) {
                AppUser user = new AppUser();
                user.setEmail("user@livepoll.com");
                user.setName("LivePoll User");
                user.setPassword(passwordEncoder.encode("User@123"));
                user.setRole(Role.ROLE_USER);
                user.setProvider(AuthProvider.LOCAL);
                userRepository.save(user);
            }
        };
    }
}
