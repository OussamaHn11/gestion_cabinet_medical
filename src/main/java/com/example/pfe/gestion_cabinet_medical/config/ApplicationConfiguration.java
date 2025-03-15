package com.example.pfe.gestion_cabinet_medical.config;

import com.example.pfe.gestion_cabinet_medical.entity.Médecin;
import com.example.pfe.gestion_cabinet_medical.entity.Secrétaire;
import com.example.pfe.gestion_cabinet_medical.repository.MédecinRepository;
import com.example.pfe.gestion_cabinet_medical.repository.SecrétaireRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class ApplicationConfiguration {

    private final MédecinRepository médecinRepository;
    private final SecrétaireRepository secrétaireRepository;

    public ApplicationConfiguration(MédecinRepository médecinRepository, SecrétaireRepository secrétaireRepository) {
        this.médecinRepository = médecinRepository;
        this.secrétaireRepository = secrétaireRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<Médecin> médecinOpt = médecinRepository.findByUsername(username);
            if (médecinOpt.isPresent()) {
                Médecin médecin = médecinOpt.get();
                return org.springframework.security.core.userdetails.User
                        .withUsername(médecin.getUsername())
                        .password(médecin.getPassword())
                        .roles("MEDECIN") // Assurez-vous que le rôle est "MEDECIN"
                        .build();
            }

            Optional<Secrétaire> secrétaireOpt = secrétaireRepository.findByUsername(username);
            if (secrétaireOpt.isPresent()) {
                Secrétaire secrétaire = secrétaireOpt.get();
                return org.springframework.security.core.userdetails.User
                        .withUsername(secrétaire.getUsername())
                        .password(secrétaire.getPassword())
                        .roles("SECRETAIRE") // Assurez-vous que le rôle est "SECRETAIRE"
                        .build();
            }

            throw new UsernameNotFoundException("Utilisateur non trouvé : " + username);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}