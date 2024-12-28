package com.projetSpringBoot.ProjetClasse.Business.services;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User user = super.loadUser(userRequest);
        // Vous pouvez enregistrer ou mettre à jour les informations utilisateur ici.
        System.out.println("User Info: " + user.getAttributes());
        return user;
    }
}

