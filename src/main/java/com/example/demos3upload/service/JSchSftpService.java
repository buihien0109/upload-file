package com.example.demos3upload.service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

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

    private Session setupJsch() throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sftpUser, sftpHost, sftpPort);
        session.setPassword(sftpPassword);

        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
        session.setConfig(properties);
        session.setTimeout(60000); // Set timeout to 60 seconds

        return session;
    }

    public void uploadFile(File file) {
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = setupJsch();
            session.connect();
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            try (InputStream inputStream = new FileInputStream(file)) {
                channelSftp.put(inputStream, remoteDirectory + "/" + file.getName());
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
}
