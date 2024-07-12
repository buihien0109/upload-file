package com.example.demos3upload.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.demos3upload.model.HSBB;
import com.example.demos3upload.model.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HsbbService {
    private final ObjectMapper objectMapper;
    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile() {
        // Create List 3 Hsbb
        List<HSBB> dsHSBB = List.of(
                HSBB.builder().stt("1").fileName("1").soHoSo("240711001809").filePath("https://qlkn.viettelpost.vn/2024-07-11/16_47_40-c762ec60-fa1b-44f0-bd54-14a43003b255.PNG").build(),
                HSBB.builder().stt("2").fileName("2").soHoSo("240711001809").filePath("https://qlkn.viettelpost.vn/2024-07-11/16_47_40-dee8cb97-749f-49f2-b41d-13982a3671cc.PNG").build(),
                HSBB.builder().stt("1").fileName("1").soHoSo("240711001810").filePath("https://qlkn.viettelpost.vn/2024-07-11/16_47_40-c762ec60-fa1b-44f0-bd54-14a43003b255.PNG").build(),
                HSBB.builder().stt("2").fileName("2").soHoSo("240711001810").filePath("https://qlkn.viettelpost.vn/2024-07-11/16_47_40-dee8cb97-749f-49f2-b41d-13982a3671cc.PNG").build()
        );

        // Tạo dữ liệu CSV từ list
        byte[] csvData = writeDataToCsv(dsHSBB);
        if (csvData == null) {
            return "Failed to create CSV data";
        }


        // Lưu dữ liệu CSV vào file tạm thời
        File csvFile;
        String fileName = UUID.randomUUID() + ".csv";
        try {
            csvFile = convert(csvData, fileName);
        } catch (IOException e) {
            log.error("Error creating temporary file", e);
            return "Error creating temporary file";
        }

        String filePath = formatDateByPattern(new Date(), "yyyy_MM_dd_hh_mm_ss");
        String keyName = bucketName + "/" + filePath + "/" + fileName;
        try {
            String response = uploadFile(csvFile, bucketName, keyName);
            log.info("Upload response: {}", response);
            return response;
        } catch (JsonProcessingException e) {
            log.error("Error uploading file", e);
            return "Error uploading file";
        } finally {
            // Xóa file tạm sau khi upload xong
            if (csvFile.exists() && !csvFile.delete()) {
                log.warn("Failed to delete temporary file: {}", csvFile.getAbsolutePath());
            }
        }
    }

    public String uploadFile(File file, String bucketName, String keyName) throws JsonProcessingException {
        PutObjectRequest request = new PutObjectRequest(bucketName, keyName, file);
        PutObjectResult putObjectResult = s3Client.putObject(
                request.withCannedAcl(CannedAccessControlList.PublicRead));
        log.info("PutObjectResult Cloud: {}", objectMapper.writeValueAsString(putObjectResult));
        if (putObjectResult != null) {
            // Get url file
            return s3Client.getUrl(bucketName, keyName).toString();
        }
        return null;
    }

    public String formatDateByPattern(Date date, String s) {
        SimpleDateFormat sdf = new SimpleDateFormat(s);
        return sdf.format(date);
    }

    public byte[] writeDataToCsv(List<HSBB> data) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
             CSVWriter writer = new CSVWriter(outputStreamWriter, '|', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {

            // Write data without header
            for (HSBB hsbb : data) {
                String[] csvRow = {hsbb.getSoHoSo(), hsbb.getStt(), hsbb.getFileName(), hsbb.getFilePath()};
                writer.writeNext(csvRow);
            }

            writer.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public File convert(byte[] fileContent, String fileName) throws IOException {
        File convFile = new File("hubsub_file", fileName);
        if (!convFile.getParentFile().exists()) {
            convFile.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(fileContent);
        }
        return convFile;
    }

}

