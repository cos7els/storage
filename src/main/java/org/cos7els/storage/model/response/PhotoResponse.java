package org.cos7els.storage.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponse {
    private Long id;
    private String fileName;
    private Long size;
    private String path;
}