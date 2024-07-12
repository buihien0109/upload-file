package com.example.demos3upload.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
public class FileService {
    private final String uploadDir = "file_uploads";

    public FileService() {
        createDirectory(uploadDir);
    }

    public void createDirectory(String name) {
        Path path = Paths.get(name);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                log.error("Cannot create directory: " + path);
                log.error(e.getMessage());
                throw new RuntimeException("Cannot create directory: " + path);
            }
        }
    }

    public File uploadImage(MultipartFile file) {
        String fileName = UUID.randomUUID() + "." + getExtensionFile(file);
        Path rootPath = Paths.get(uploadDir);
        Path filePath = rootPath.resolve(fileName);

        try {
            Files.copy(file.getInputStream(), filePath);
            return filePath.toFile();
        } catch (IOException e) {
            log.error("Cannot upload file: {}", filePath);
            log.error(e.getMessage());
            throw new RuntimeException("Cannot upload file: " + filePath);
        }
    }

    private String getExtensionFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
