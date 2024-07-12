package com.example.demos3upload.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HSBB {
    String soHoSo;
    String stt;
    String fileName;
    String filePath;
}
