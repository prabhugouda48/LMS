package com.lms.service;

import com.lms.config.FileStorageProperties;
import com.lms.exception.BadRequestException;
import com.lms.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final FileStorageProperties fileStorageProperties;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file, String submissionId) {
        // Validate file size
        if (file.getSize() > fileStorageProperties.getMaxFileSize()) {
            throw new BadRequestException("File size exceeds maximum limit");
        }

        // Validate file type
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (!isValidFileType(fileExtension)) {
            throw new BadRequestException("Invalid file type. Allowed types: " + 
                String.join(", ", fileStorageProperties.getAllowedFileTypes()));
        }

        // Generate unique filename
        String fileName = UUID.randomUUID().toString() + "." + fileExtension;
        
        try {
            // Create submission directory if it doesn't exist
            Path submissionDir = this.fileStorageLocation.resolve(submissionId);
            Files.createDirectories(submissionDir);

            // Copy file to the target location
            Path targetLocation = submissionDir.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName, ex);
        }
    }

    public Resource loadFileAsResource(String submissionId, String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(submissionId).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if(resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File not found: " + fileName);
        }
    }

    public void deleteFile(String submissionId, String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(submissionId).resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Error deleting file: " + fileName, ex);
        }
    }

    private boolean isValidFileType(String fileExtension) {
        if (fileExtension == null) {
            return false;
        }
        return Arrays.asList(fileStorageProperties.getAllowedFileTypes())
                .contains(fileExtension.toLowerCase());
    }
}
