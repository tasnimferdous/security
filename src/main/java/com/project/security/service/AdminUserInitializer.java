package com.project.security.service;

import com.project.security.entity.Rights;
import com.project.security.entity.Roles;
import com.project.security.entity.Users;
import com.project.security.repository.UserDetailsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminUserInitializer {
        @Bean
        public CommandLineRunner createAdminUser(UserDetailsRepository userRepository, PasswordEncoder passwordEncoder) {
            return args -> {
                if (userRepository.findByUsername("admin").isEmpty()) {
                    Users admin = new Users();
                    admin.setUsername("admin");
                    admin.setPassword(passwordEncoder.encode("admin1234"));
                    Rights rights = Rights.builder()
                            .name("ALL")
                            .build();
                    Roles role = Roles.builder()
                            .name("ADMIN")
                            .rights(Set.of(rights))
                            .build();
                    admin.setRole(Set.of(role));

                    userRepository.save(admin);
                }
            };
        }
}
