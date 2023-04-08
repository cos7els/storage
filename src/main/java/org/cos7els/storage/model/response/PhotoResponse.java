package org.cos7els.storage.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponse {
    private Timestamp creationDate;
    private String fileName;
    private String contentType;
    private String resolution;
    private Long size;
    private Double latitude;
    private Double longitude;
    private byte[] data;
}