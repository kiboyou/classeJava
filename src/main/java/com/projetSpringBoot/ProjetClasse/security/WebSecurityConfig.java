package com.projetSpringBoot.ProjetClasse.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final BCryptPasswordEncoder passwordEncoder;

    // Injection du BCryptPasswordEncoder via constructeur
    public WebSecurityConfig(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour une API REST
                .authorizeRequests(auth -> auth
                        .requestMatchers( "/css/**", "/uploads/**", "/output/**").permitAll() // Autoriser l'accès à ces pages
                        .requestMatchers("/steganography/**").permitAll() // Autoriser ces endpoints
                        .requestMatchers("/inscription", "/login**", "/oauth2/authorization/google").permitAll() // Permettre l'accès aux pages de login et OAuth2
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Accès restreint pour le rôle ADMIN
                        .anyRequest().authenticated() // Autres demandes doivent être authentifiées
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/", true) // Rediriger vers la page d'accueil après une connexion réussie
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // Redirige vers la page de login pour OAuth
                        .defaultSuccessUrl("/", true) // Rediriger vers la page d'accueil après une connexion OAuth réussie
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL de déconnexion
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
//                        .logoutSuccessUrl("/login") // Rediriger après déconnexion
                        .permitAll()
                )

                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'self';")) // Définition de la politique de sécurité de contenu
                )
                .build();
    }
}