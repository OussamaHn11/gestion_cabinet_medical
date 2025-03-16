package com.example.pfe.gestion_cabinet_medical.auth;

import com.example.pfe.gestion_cabinet_medical.entity.Utilisateur;
import com.example.pfe.gestion_cabinet_medical.repository.UtilisateurRepository;
import com.example.pfe.gestion_cabinet_medical.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    public com.example.pfe.gestion_cabinet_medical.auth.LoginResponse register(com.example.pfe.gestion_cabinet_medical.auth.RegisterRequest request) {
        User user = null; var jwtToken = jwtService.generateToken(user);
        log.info("JWT Token generated: {}", jwtToken);
        return com.example.pfe.gestion_cabinet_medical.auth.LoginResponse.builder()
                .token(jwtToken)
                .build();
    }
    public void ajouterUtilisateur(String username, String password, String email, String nom, String prénom, String téléphone) {
        // Encoder le mot de passe
        String encodedPassword = passwordEncoder.encode(password);

        // Créer l'utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(username);
        utilisateur.setPassword(encodedPassword);  // Le mot de passe est encodé
        utilisateur.setEmail(email);
        utilisateur.setNom(nom);
        utilisateur.setPrénom(prénom);
        utilisateur.setTéléphone(téléphone);
        // Sauvegarder l'utilisateur dans la base de données
        repository.save(utilisateur); // Utilisez 'repository' ici pour sauvegarder dans la base de données
    }

    public com.example.pfe.gestion_cabinet_medical.auth.LoginResponse authenticate(com.example.pfe.gestion_cabinet_medical.auth.LoginRequest request) {
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Adresse mail ou Mot de passe Incorrect"));

        // Comparer le mot de passe fourni avec le mot de passe encodé dans la base de données
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Username ou password Incorrect");
        }

        // Génération du token après validation du mot de passe
        UserDetails userDetails = User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // L'utilisateur doit avoir le mot de passe encodé
                .build();

        var jwtToken = jwtService.generateToken(userDetails);
        return new com.example.pfe.gestion_cabinet_medical.auth.LoginResponse(jwtToken);
    }}



