package com.example.pfe.gestion_cabinet_medical.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String nom;
    private String username;
    private String téléphone;
    private String email;
    private String password;
    private String prénom ;
}
