package org.cos7els.storage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateAlbumRequest {
    private String title;
    private List<Long> photoIds;
}
