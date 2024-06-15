package com.rihab.interventions.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.rihab.interventions.entities.Image;
import com.rihab.interventions.repos.ImageRepository;
import java.io.IOException;
import org.springframework.http.MediaType;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    ImageRepository imageRepository;

    @Override
    public Image uploadImage(MultipartFile file) throws IOException {
        try {
            return imageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(file.getBytes()).build());
        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new IOException("Error uploading image", e);
        }
    }

    @Override
    public Image getImageDetails(Long id) throws IOException {
        Optional<Image> dbImage = imageRepository.findById(id);
        if (dbImage.isPresent()) {
            return dbImage.get();
        } else {
            throw new IOException("Image not found");
        }
    }

    @Override
    public ResponseEntity<byte[]> getImage(Long id) throws IOException {
        Optional<Image> dbImage = imageRepository.findById(id);
        if (dbImage.isPresent()) {
            return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(dbImage.get().getImage());
        } else {
            throw new IOException("Image not found");
        }
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
}
