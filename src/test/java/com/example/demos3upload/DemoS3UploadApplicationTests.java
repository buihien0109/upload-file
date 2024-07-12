package com.example.demos3upload;

import com.example.demos3upload.service.HsbbService;
import com.example.demos3upload.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class DemoS3UploadApplicationTests {
    @Autowired
    private StudentService studentService;

    @Autowired
    private HsbbService hsbbService;

    @Test
    void test_upload() {
        String rs = studentService.uploadFile();
        log.info("File uploaded: {}", rs);
    }

    @Test
    void test_upload_2() {
        String rs = hsbbService.uploadFile();
        log.info("File uploaded: {}", rs);
    }
}
