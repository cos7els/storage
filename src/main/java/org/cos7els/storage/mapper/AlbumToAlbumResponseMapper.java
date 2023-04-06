package org.cos7els.storage.mapper;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.Album;
import org.cos7els.storage.model.response.AlbumResponse;
import org.cos7els.storage.service.PhotoService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AlbumToAlbumResponseMapper {
    private final PhotoService photoService;

    public AlbumResponse albumToResponse(Album album) {
        return new AlbumResponse(
                album.getId(),
                album.getTitle(),
                photoService.photosToThumbnails(album.getPhotos())
        );
    }

    public List<AlbumResponse> albumsToResponses(List<Album> albums) {
        return albums.stream()
                .map(this::albumToResponse)
                .collect(Collectors.toList());
    }
}
