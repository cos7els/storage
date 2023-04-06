package org.cos7els.storage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cos7els.storage.model.response.PhotoResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAlbumRequest {
    private String title;
}
