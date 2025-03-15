package com.example.pfe.gestion_cabinet_medical.config;

import com.example.pfe.gestion_cabinet_medical.service.JwtService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Injection via le constructeur pour suivre les bonnes pratiques de Spring
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Récupérer le header Authorization
        final String authHeader = request.getHeader("Authorization");

        // Vérifier que le header est valide
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7); // Extraire le token JWT
        String username = null;

        try {
            // Vérifier que le JWT est valide et extraire le nom d'utilisateur
            if (jwtService.isValidJwt(jwt)) {
                username = jwtService.extractUsername(jwt); // Extraire le nom d'utilisateur
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'extraction ou de la validation du JWT", e);
            filterChain.doFilter(request, response);
            return;
        }

        // Si le nom d'utilisateur est trouvé et aucun utilisateur n'est encore authentifié
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Vérifier la validité du token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Créer un token d'authentification
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Ajouter les détails d'authentification dans le contexte de sécurité
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                logger.info("Utilisateur authentifié : {}", username);
            }
        }

        // Passer au prochain filtre
        filterChain.doFilter(request, response);
    }
}
