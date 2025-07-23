package com.smm.course_registration.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID; // For generating unique file names

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    // Inject the upload directory from application.properties
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            // Create the directory if it doesn't exist
            Files.createDirectories(this.fileStorageLocation);
            logger.info("File upload directory created at: {}", this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * Stores a file and returns its unique filename.
     * @param file The MultipartFile to store.
     * @return The unique filename (e.g., UUID.extension).
     * @throws IOException If file storage fails.
     */
    public String storeFile(MultipartFile file) throws IOException {
        // Normalize file name to prevent directory traversal attacks
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..")) {
            throw new IOException("Invalid file name: " + originalFilename);
        }

        // Get file extension
        String fileExtension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
            fileExtension = originalFilename.substring(dotIndex);
        }

        // Generate a unique file name using UUID
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);

        logger.info("Attempting to store file: {} to {}", originalFilename, targetLocation);

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File {} stored successfully as {}", originalFilename, uniqueFileName);
        return uniqueFileName;
    }

    /**
     * Loads a file as a Resource (not directly used for URL, but good for internal access).
     * @param filename The unique filename of the stored file.
     * @return The Path to the file.
     */
    public Path loadFileAsPath(String filename) {
        return this.fileStorageLocation.resolve(filename).normalize();
    }

    /**
     * Deletes a file from storage.
     * @param filename The unique filename of the stored file.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteFile(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            logger.error("Could not delete file {}: {}", filename, ex.getMessage());
            return false;
        }
    }
}
