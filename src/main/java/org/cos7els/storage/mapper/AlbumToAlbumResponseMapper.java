package org.cos7els.storage.mapper;

import org.cos7els.storage.model.domain.Album;
import org.cos7els.storage.model.response.AlbumResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AlbumToAlbumResponseMapper {
    private final PhotoToPhotoResponseMapper photoToPhotoResponseMapper;

    @Autowired
    public AlbumToAlbumResponseMapper(PhotoToPhotoResponseMapper photoToPhotoResponseMapper) {
        this.photoToPhotoResponseMapper = photoToPhotoResponseMapper;
    }

    public AlbumResponse albumToResponse(Album album) {
        return new AlbumResponse(album.getId(), album.getTitle(), photoToPhotoResponseMapper.photosToThumbnails(album.getPhotos()));
    }

    public List<AlbumResponse> albumsToResponses(List<Album> albums) {
        return albums.stream().map(this::albumToResponse).collect(Collectors.toList());
    }
}