package com.projetSpringBoot.ProjetClasse.web.controllers;

import com.projetSpringBoot.ProjetClasse.Business.servicesImpl.UserServiceImpl;
import com.projetSpringBoot.ProjetClasse.DAO.entities.User;
import com.projetSpringBoot.ProjetClasse.security.JwtTokenUtil;
import com.projetSpringBoot.ProjetClasse.web.models.UserForm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.authentication.AuthenticationManager;

import java.security.Principal;


@Controller
public class AuthController {

//    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtService;
    private final UserServiceImpl userServiceImpl;

    public AuthController(JwtTokenUtil jwtService, AuthenticationManager authenticationManager, UserServiceImpl userServiceImpl) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userServiceImpl = userServiceImpl;
    }


    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }

    @GetMapping("/oauth2/callback")
    public String oauthCallback(Principal principal, HttpServletResponse response) {
        // Générer le token JWT
        String jwt = jwtService.generateToken(principal.getName());

        // Ajouter le token dans un cookie sécurisé
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Supprimer le cookie JWT
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        return "redirect:/login?logout";
    }


//    // Affiche le formulaire d'ajout d'utilisateur
//    @RequestMapping("/inscription")
//    public String showAddUserForm(Model model) {
//        model.addAttribute("userForm", new UserForm());
//        return "admin/pages/inscription";
//    }
//
//    // Gère l'envoi du formulaire
//    @RequestMapping(path = "/inscription", method = RequestMethod.POST)
//    public String addUser(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("userForm", userForm);
//            return "admin/pages/inscription";
//        }
//        // Ajouter l'utilisateur via le service
//        userServiceImpl.registerUser(new User(userForm.getName(), userForm.getEmail(), userForm.getPassword()));
//        return "redirect:/login";
//    }
}