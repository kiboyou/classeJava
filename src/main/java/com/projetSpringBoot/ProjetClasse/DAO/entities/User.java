package com.projetSpringBoot.ProjetClasse.DAO.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(nullable = false)
    private boolean oauth2; // Indique si l'utilisateur s'est enregistré via Google OAuth2

    public User (String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

    }
}
