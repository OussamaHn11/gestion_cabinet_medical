package com.example.pfe.gestion_cabinet_medical.repository;

import com.example.pfe.gestion_cabinet_medical.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByUsername(String username); // Retourne Optional
}

