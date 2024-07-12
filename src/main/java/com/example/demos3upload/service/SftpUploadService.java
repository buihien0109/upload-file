//package com.example.demos3upload.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.integration.file.remote.session.Session;
//import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;
//import org.springframework.integration.sftp.session.SftpSession;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//@Service
//@RequiredArgsConstructor
//public class SftpUploadService {
//    private final SftpRemoteFileTemplate sftpRemoteFileTemplate;
//
//    @Value("${spring.sftp.remote-directory}")
//    private String remoteDirectory;
//
//    public void uploadFile(File file) throws IOException {
//        Session<?> session = null;
//        try {
//            session = sftpRemoteFileTemplate.getSessionFactory().getSession();
//            if (session instanceof SftpSession) {
//                ((SftpSession) session).write(new FileInputStream(file), remoteDirectory + "/" + file.getName());
//            }
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//    }
//
//    public boolean isFileExists(String remoteFilePath) {
//        Session<?> session = null;
//        try {
//            session = sftpRemoteFileTemplate.getSessionFactory().getSession();
//            if (session instanceof SftpSession) {
//                return ((SftpSession) session).exists(remoteDirectory + "/" + remoteFilePath);
//            }
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//        return false;
//    }
//
//    public String readFile(String remoteFilePath) throws IOException {
//        Session<?> session = null;
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        try {
//            session = sftpRemoteFileTemplate.getSessionFactory().getSession();
//            if (session instanceof SftpSession) {
//                ((SftpSession) session).read(remoteDirectory + "/" + remoteFilePath, outputStream);
//                return outputStream.toString();
//            }
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//        return null;
//    }
//}
