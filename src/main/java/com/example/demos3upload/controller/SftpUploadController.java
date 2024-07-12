package com.example.demos3upload.controller;

import com.example.demos3upload.service.FileService;
import com.example.demos3upload.service.JSchSftpService;
import com.example.demos3upload.service.SftpUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@RestController
public class SftpUploadController {
    @Autowired
    private JSchSftpService sftpService;

    @Autowired
    private SftpUploadService sftpUploadService;

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        File savedFile = fileService.uploadImage(file);
        sftpService.uploadFile(savedFile);
        return "File uploaded successfully!";
    }

    @GetMapping("/read-file")
    public String readFile(@RequestParam("filename") String filename) {
        try {
            return sftpService.readFile(filename);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to read file!";
        }
    }

    @GetMapping("/file-exists")
    public String checkFileExists(@RequestParam("filename") String fileName) {
        boolean exists = sftpService.isFileExists(fileName);
        if (exists) {
            return "File exists on the SFTP server.";
        } else {
            return "File does not exist on the SFTP server.";
        }
    }

    @PostMapping("/upload2")
    public String uploadFile2(@RequestParam("file") MultipartFile file) {
        try {
            File savedFile = fileService.uploadImage(file);
            sftpUploadService.uploadFile(savedFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed!";
        }
        return "File uploaded successfully!";
    }

    @GetMapping("/read-file2")
    public String readFile2(@RequestParam("filename") String filename) {
        try {
            return sftpUploadService.readFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to read file!";
        }
    }
}

