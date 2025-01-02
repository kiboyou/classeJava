package com.projetSpringBoot.ProjetClasse.Business.services;

import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class SteganographyService {
    public String hideMessage(String imagePath, String message) throws IOException {
            // Lire l'image depuis le chemin spécifié
            BufferedImage image = ImageIO.read(new File(imagePath));
            System.out.println("Image: " + image);

            // Convertir le message en tableau de bytes
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

            // Vérifiez si l'image a assez de pixels pour contenir tous les bytes du message
            int totalPixels = image.getWidth() * image.getHeight();
            if (messageBytes.length > totalPixels) {
                throw new IllegalArgumentException("Message trop long pour être caché dans cette image.");
            }

            // Récupérer les pixels de l'image
            int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

            // Cacher les bytes du message dans les pixels
            for (int i = 0; i < messageBytes.length; i++) {
                // Modifier le dernier octet de chaque pixel avec un byte du message
                pixels[i] = (pixels[i] & 0xFFFFFF00) | (messageBytes[i] & 0xFF);
            }

            // Mettre à jour l'image avec les pixels modifiés
            image.setRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

            // Définir le chemin de sortie pour l'image modifiée
            String outputPath = "static/output/hidden_message.png";

            // Vérifier si le répertoire existe, sinon le créer
            File outputDir = new File("static/output");
            if (!outputDir.exists()) {
                boolean created = outputDir.mkdirs(); // Créer les répertoires nécessaires
                if (!created) {
                    throw new IOException("Impossible de créer le répertoire de sortie.");
                }
            }

            // Sauvegarder l'image modifiée
            File outputFile = new File(outputPath);
            ImageIO.write(image, "png", outputFile);

            // Vérifier si le fichier a bien été créé
            if (outputFile.exists()) {
                System.out.println("Image sauvegardée à : " + outputPath);
            } else {
                throw new IOException("Erreur lors de la sauvegarde de l'image.");
            }
            // Retourner le chemin de l'image modifiée
            return outputPath;
    }

    public String extractMessage(String imagePath) throws IOException {
        // Lire l'image depuis le chemin spécifié
        BufferedImage image = ImageIO.read(new File(imagePath));
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

        // Définir la longueur du message extrait
        int messageLength = 100;  // Limite ici à 100 caractères pour l'extraction
        byte[] extractedBytes = new byte[messageLength];

        // Extraire les octets du message caché dans les pixels
        for (int i = 0; i < messageLength; i++) {
            // Extraire les 8 bits du pixel et les stocker dans le tableau
            extractedBytes[i] = (byte) (pixels[i] & 0xFF);
        }

        // Convertir les bytes extraits en texte et ignorer les caractères non imprimables
        StringBuilder message = new StringBuilder();
        for (byte b : extractedBytes) {
            char c = (char) b;
            // Ajouter seulement les caractères imprimables (ASCII 32 à 126)
            if (c >= 32 && c <= 126) {
                message.append(c);
            }
        }
        // Retourner le message extrait
        return message.toString();
    }

}

