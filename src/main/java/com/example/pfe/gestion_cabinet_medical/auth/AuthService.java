package com.example.pfe.gestion_cabinet_medical.auth;

import com.example.pfe.gestion_cabinet_medical.repository.UtilisateurRepository;
import com.example.pfe.gestion_cabinet_medical.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UtilisateurRepository repository;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public com.example.pfe.gestion_cabinet_medical.AUTH.LoginResponse register(com.example.pfe.gestion_cabinet_medical.auth.RegisterRequest request) {
        User user = null; var jwtToken = jwtService.generateToken(user);
        log.info("JWT Token generated: {}", jwtToken);
        return com.example.pfe.gestion_cabinet_medical.AUTH.LoginResponse.builder()
                .token(jwtToken)
                .build();
    }

    public com.example.pfe.gestion_cabinet_medical.AUTH.LoginResponse authenticate(com.example.pfe.gestion_cabinet_medical.AUTH.LoginRequest request) {
        try {
            // Tenter d'authentifier l'utilisateur
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            // Si les informations d'identification sont invalide
            // Si les informations d'identification sont invalides, renvoyer un message d'erreur approprié
            throw new RuntimeException("Adresse mail ou Mot de passe Incorrect");  // Message d'erreur générique
        } catch (AuthenticationException e) {
            // Gestion des autres erreurs d'authentification
            throw new RuntimeException("Authentication failed"); // Message d'erreur générique
        }

        // Récupérer l'utilisateur après une authentification réussie
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Adresse mail ou Mot de passe Incorrect")); // Utiliser le même message
        UserDetails userDetails = User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Assurez-vous que le password est déjà encodé
                .build();

        var jwtToken = jwtService.generateToken(userDetails);
        log.info("JWT Token generated: {}", jwtToken);
        return com.example.pfe.gestion_cabinet_medical.AUTH.LoginResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .build();
    }

        }



