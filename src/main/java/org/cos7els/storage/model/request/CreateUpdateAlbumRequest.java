package org.cos7els.storage.model.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateUpdateAlbumRequest {
    private String title;
    private List<Long> photoIds;
}
