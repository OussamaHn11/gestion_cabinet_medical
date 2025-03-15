package com.example.pfe.gestion_cabinet_medical.repository;

import com.example.pfe.gestion_cabinet_medical.entity.Médecin;
import com.example.pfe.gestion_cabinet_medical.entity.Secrétaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecrétaireRepository extends JpaRepository<Secrétaire, Long> {
    Optional<Secrétaire> findByUsername(String username);
}
