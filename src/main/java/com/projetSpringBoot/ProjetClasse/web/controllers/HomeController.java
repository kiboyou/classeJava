package com.projetSpringBoot.ProjetClasse.web.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        // Récupérer le nom de l'utilisateur connecté
        Object username = authentication.getDetails();
        model.addAttribute("username", username);
         return "pages/home";
    }
}