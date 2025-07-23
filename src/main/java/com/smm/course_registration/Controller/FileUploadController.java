package com.smm.course_registration.Controller;

import com.smm.course_registration.Services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/uploads") // Base path for upload operations
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')") // Allow admins and students to upload images
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed.");
            }

            String fileName = fileStorageService.storeFile(file);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/") // This is the path where static files will be served
                    .path(fileName)
                    .toUriString();

            logger.info("File uploaded successfully. Access URL: {}", fileDownloadUri);
            return ResponseEntity.ok("{\"fileName\": \"" + fileName + "\", \"fileUrl\": \"" + fileDownloadUri + "\"}");

        } catch (IOException ex) {
            logger.error("Could not upload file: {}", ex.getMessage());
            return ResponseEntity.status(500).body("Could not upload file: " + ex.getMessage());
        } catch (Exception ex) {
            logger.error("An unexpected error occurred during file upload: {}", ex.getMessage());
            return ResponseEntity.status(500).body("An unexpected error occurred: " + ex.getMessage());
        }
    }


}

