package org.cos7els.storage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateAlbumRequest {
    @NotNull
    @NotEmpty
    @Size(min = 4, max = 50)
    private String title;
    @NotNull
    @NotEmpty
    private List<Long> photoIds;
}