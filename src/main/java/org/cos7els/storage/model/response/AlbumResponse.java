package org.cos7els.storage.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cos7els.storage.model.Photo;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResponse {
    private String title;
    private List<PhotoResponse> photos;
}