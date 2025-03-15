package com.example.pfe.gestion_cabinet_medical.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name="medecin")
public class Médecin extends Utilisateur{
    private String spécialité;
}

