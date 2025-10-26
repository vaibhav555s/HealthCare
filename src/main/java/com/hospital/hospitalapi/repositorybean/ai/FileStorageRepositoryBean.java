package com.hospital.hospitalapi.repositorybean.ai;

import com.hospital.hospitalapi.exception.ai.InvalidFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageRepositoryBean {
    
    @Value("${file.upload.dir:uploads/prescriptions}")
    private String uploadDir;
    
    public String storeFile(MultipartFile file) {
        log.info("Storing file: {}", file.getOriginalFilename());
        
        try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate unique filename
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            
            // Save file
            Files.copy(file.getInputStream(), filePath);
            
            String fileUrl = uploadDir + File.separator + fileName;
            log.info("File stored successfully: {}", fileUrl);
            return fileUrl;
            
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new InvalidFileException("Failed to store file: " + e.getMessage());
        }
    }
    
    public byte[] loadFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error("Failed to load file: {}", fileName, e);
            throw new InvalidFileException("Failed to load file: " + fileName);
        }
    }
    
    public void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Files.deleteIfExists(filePath);
            log.info("File deleted: {}", fileName);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", fileName, e);
        }
    }
}
