package com.example.pfe.gestion_cabinet_medical.service;

import com.example.pfe.gestion_cabinet_medical.entity.Médecin;
import com.example.pfe.gestion_cabinet_medical.entity.Secrétaire;
import com.example.pfe.gestion_cabinet_medical.entity.Utilisateur;
import com.example.pfe.gestion_cabinet_medical.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        // Déterminer le rôle en fonction du type de la classe
        String role;
        if (utilisateur instanceof Médecin) {
            role = "MEDECIN";
        } else if (utilisateur instanceof Secrétaire) {
            role = "SECRETAIRE";
        } else {
            throw new IllegalStateException("Type d'utilisateur inconnu : " + utilisateur.getClass().getName());
        }

        return User.withUsername(utilisateur.getUsername())
                .password(utilisateur.getPassword())
                .roles(role) // Utiliser le rôle calculé
                .build();
    }
}