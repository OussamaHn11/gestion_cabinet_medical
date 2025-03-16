package com.example.pfe.gestion_cabinet_medical.repository;
import com.example.pfe.gestion_cabinet_medical.entity.Médecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MédecinRepository extends JpaRepository<Médecin, Long> {
    Optional<Médecin> findByUsername(String username);
}
