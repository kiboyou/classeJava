package com.projetSpringBoot.ProjetClasse.Business.services;

import com.projetSpringBoot.ProjetClasse.DAO.entities.Image;
import com.projetSpringBoot.ProjetClasse.DAO.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    private String uploadDir = "/Users/pro/OKM/BS/ProjetClasse/src/main/resources/static/uploads/";

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image saveImage(MultipartFile file) throws IOException {
        // Créer le répertoire si nécessaire
        Path uploadPath = Paths.get(uploadDir);

        // Enregistrer le fichier dans le système local
        String filePath = uploadPath.resolve(file.getOriginalFilename()).toString();
        System.out.println("File path: " + filePath);
        file.transferTo(new File(uploadDir));
        System.out.println("File: " + file.getOriginalFilename());

        // Enregistrer le chemin dans la base de données
        Image image = new Image();
        image.setFilePath(filePath);
        System.out.println("Image path: " + filePath);
        return imageRepository.save(image);
    }
}