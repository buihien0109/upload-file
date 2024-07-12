package com.example.demos3upload.service;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Slf4j
@Service
public class JSchSftpService {

    @Value("${spring.sftp.host}")
    private String sftpHost;

    @Value("${spring.sftp.port}")
    private int sftpPort;

    @Value("${spring.sftp.user}")
    private String sftpUser;

    @Value("${spring.sftp.password}")
    private String sftpPassword;

    @Value("${spring.sftp.remote-directory}")
    private String remoteDirectory;

    public void uploadFile(File file) {
        log.info("Uploading file: {}", file.getName());
        log.info("Data: {}", file.getAbsolutePath());
        log.info("To remote directory: {}", remoteDirectory);
        log.info("To remote host: {}", sftpHost);
        log.info("To remote port: {}", sftpPort);
        log.info("With user: {}", sftpUser);
        log.info("With password: {}", sftpPassword);

        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sftpUser, sftpHost, sftpPort);
            session.setPassword(sftpPassword);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            log.info("Connected to SFTP server");
            log.info("Uploading to: {}", remoteDirectory + "/" + file.getName());

            try (InputStream inputStream = new FileInputStream(file)) {
                channelSftp.put(inputStream, remoteDirectory + "/" + file.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file: " + file.getName());
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    private Session setupJsch() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sftpUser, sftpHost, sftpPort);
        session.setPassword(sftpPassword);

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        return session;
    }

    public String readFile(String remoteFilePath) {
        Session session = null;
        ChannelSftp channelSftp = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            session = setupJsch();
            session.connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            try (InputStream inputStream = channelSftp.get(remoteDirectory + "/" + remoteFilePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            return outputStream.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    public boolean isFileExists(String fileName) {
        Session session = null;
        ChannelSftp channelSftp = null;
        boolean exists = false;

        try {
            session = setupJsch();
            session.connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            try {
                channelSftp.stat(remoteDirectory + "/" + fileName);
                exists = true;
            } catch (SftpException e) {
                if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                    exists = false;
                } else {
                    throw e;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channelSftp != null) {
                channelSftp.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }

        return exists;
    }
}
