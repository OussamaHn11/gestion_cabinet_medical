package com.example.pfe.gestion_cabinet_medical.auth;

import com.example.pfe.gestion_cabinet_medical.config.JwtService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final AuthService service;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Ajouter l'utilisateur avec mot de passe encodé
            service.ajouterUtilisateur(request.getUsername(), request.getPassword(), request.getEmail(), request.getNom(), request.getPrénom(), request.getTéléphone());

            // Réponse réussie
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Utilisateur ajouté avec succès"));

        } catch (Exception e) {
            log.error("🚨 Erreur d'ajout utilisateur : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Erreur interne lors de l'ajout de l'utilisateur"));
        }
    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody com.example.pfe.gestion_cabinet_medical.auth.LoginRequest request) {
        try {
            com.example.pfe.gestion_cabinet_medical.auth.LoginResponse response = service.authenticate(request);

            // Vérifiez que le token est bien généré et transmis
            if (response.getToken() == null || response.getToken().isEmpty()) {
                log.error("🚨 Le token n'a pas été généré !");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Erreur interne : token non généré"));
            }

            log.info("✅ Token généré avec succès : {}", response.getToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("🚨 Erreur d'authentification : {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }





}