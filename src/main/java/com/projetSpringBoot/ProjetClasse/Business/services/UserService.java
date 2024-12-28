package com.projetSpringBoot.ProjetClasse.Business.services;

import com.projetSpringBoot.ProjetClasse.DAO.entities.User;

import java.util.Optional;

public interface UserService {
    public User registerUser(User user);
    public Optional<User> findByEmail(String email);
}
