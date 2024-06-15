package com.rihab.interventions.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.rihab.interventions.entities.Image;
import java.io.IOException;

public interface ImageService {
    Image uploadImage(MultipartFile file) throws IOException;
    Image getImageDetails(Long id) throws IOException;
    ResponseEntity<byte[]> getImage(Long id) throws IOException;
    void deleteImage(Long id);
}