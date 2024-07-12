package com.example.demos3upload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

//@Configuration
//@EnableIntegration
//@IntegrationComponentScan
//public class SftpConfig {
//
//    @Value("${spring.sftp.host}")
//    private String sftpHost;
//
//    @Value("${spring.sftp.port}")
//    private int sftpPort;
//
//    @Value("${spring.sftp.user}")
//    private String sftpUser;
//
//    @Value("${spring.sftp.password}")
//    private String sftpPassword;
//
//    @Value("${spring.sftp.remote-directory}")
//    private String remoteDirectory;
//
//    @Bean
//    public DefaultSftpSessionFactory sftpSessionFactory() {
//        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
//        factory.setHost(sftpHost);
//        factory.setPort(sftpPort);
//        factory.setUser(sftpUser);
//        factory.setPassword(sftpPassword);
//        factory.setAllowUnknownKeys(true);
//        return factory;
//    }
//
//    @Bean
//    public SftpRemoteFileTemplate sftpRemoteFileTemplate() {
//        return new SftpRemoteFileTemplate(sftpSessionFactory());
//    }
//
//    @Bean
//    public String remoteDirectory() {
//        return remoteDirectory;
//    }
//}
