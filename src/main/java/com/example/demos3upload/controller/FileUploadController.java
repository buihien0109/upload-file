package com.example.demos3upload.controller;

import com.example.demos3upload.service.JSchSftpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@RestController
public class FileUploadController {

    @Autowired
    private JSchSftpService sftpService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Lưu file tạm thời
            File tempFile = File.createTempFile("temp", file.getOriginalFilename());
            file.transferTo(tempFile);

            // Upload file lên SFTP
            sftpService.uploadFile(tempFile);

            // Xóa file tạm thời sau khi upload
            tempFile.delete();

            return "File uploaded successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed!";
        }
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

//    @PostMapping("/upload")
//    public String uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            // Lưu file tạm thời
//            File tempFile = File.createTempFile("temp", file.getOriginalFilename());
//            file.transferTo(tempFile);
//
//            // Upload file lên SFTP
//            sftpUploadService.uploadFile(tempFile);
//
//            // Xóa file tạm thời sau khi upload
//            tempFile.delete();
//
//            // Kiểm tra xem file có tồn tại trên SFTP
//            boolean isFileExists = sftpUploadService.isFileExists(file.getOriginalFilename());
//            if (isFileExists) {
//                return "File uploaded successfully and it exists on SFTP!";
//            } else {
//                return "File uploaded but it does not exist on SFTP!";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "File upload failed!";
//        }
//    }
//
//    @GetMapping("/read-file")
//    public String readFile(@RequestParam("filename") String filename) {
//        try {
//            return sftpUploadService.readFile(filename);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Failed to read file!";
//        }
//    }
}

