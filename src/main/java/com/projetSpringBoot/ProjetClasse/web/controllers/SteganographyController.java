package com.projetSpringBoot.ProjetClasse.web.controllers;

import com.projetSpringBoot.ProjetClasse.Business.services.ImageService;
import com.projetSpringBoot.ProjetClasse.Business.services.SteganographyService;
import com.projetSpringBoot.ProjetClasse.DAO.entities.Image;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@AllArgsConstructor
@RequestMapping("/steganography")
public class SteganographyController {

    private final SteganographyService steganographyService;
    private final ImageService imageService;


    @PostMapping("/hide")
    public ResponseEntity<Resource> hideMessage(@RequestParam("image") MultipartFile image,
                                                @RequestParam("message") String message, Model model) throws IOException {

        String imagePath = saveUploadedFile(image);
        String outputPath = steganographyService.hideMessage(imagePath, message);

        File outputFile = new File(outputPath);
        Resource resource = new UrlResource(outputFile.toURI());

         // Retourner l'image avec les en-têtes HTTP pour le téléchargement
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFile.getName() + "\"")
            .contentType(MediaType.IMAGE_PNG)  // Assurez-vous de définir le type MIME approprié
            .body(resource);
    }

    @PostMapping("/extract")
    @ResponseBody
    public ResponseEntity<String> extractMessage(@RequestParam("image") MultipartFile image, Model model) throws IOException {
        String imagePath = saveUploadedFile(image);
        String extractedMessage = steganographyService.extractMessage(imagePath);
        System.out.println("Message extrait: " + extractedMessage);
        return ResponseEntity.ok("{\"message\": \"" + extractedMessage + "\"}");
    }

    private String saveUploadedFile(MultipartFile image) throws IOException {
        // Sauvegarder l'image dans un dossier spécifique
        String filename = image.getOriginalFilename();
        Path filePath = Paths.get("uploads", filename);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();

    }
}

