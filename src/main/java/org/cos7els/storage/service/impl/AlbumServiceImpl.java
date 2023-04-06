package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.Album;
import org.cos7els.storage.model.request.UpdateAlbumRequest;
import org.cos7els.storage.model.request.CreateAlbumRequest;
import org.cos7els.storage.model.response.AlbumResponse;
import org.cos7els.storage.repository.AlbumRepository;
import org.cos7els.storage.service.AlbumService;
import org.cos7els.storage.service.PhotoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.cos7els.storage.util.ExceptionMessage.ALBUM_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.PHOTO_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final PhotoService photoService;

    @Override
    public Optional<List<Album>> getAllAlbums(Long userId) {
        return Optional.of(albumRepository.findAlbumsByUserIdOrderById(userId));
    }

    @Override
    public Optional<Album> getAlbum(Long albumId, Long userId) {
        return albumRepository.findAlbumByIdAndUserId(albumId, userId);
    }

    @Override
    public Optional<Album> saveAlbum(CreateAlbumRequest request, Long userId) {
        Album album = new Album();
        album.setUserId(userId);
        album.setTitle(request.getTitle());
        return Optional.of(albumRepository.save(album));
    }

//    public Optional<Album> updateAlbum(Long albumId, UpdateAlbumRequest request, Long userId) {
//        Album album = albumRepository.findAlbumByIdAndUserId(albumId, userId)
//                .orElseThrow(() -> new NotFoundException(ALBUM_NOT_FOUND));
//        album.setTitle(request.getTitle());
//        album.setPhotos(
//                request.getPhotos().stream()
//                        .map(p -> photoService.getPhoto(p.getId(), userId))
//                        .collect(Collectors.toList())
//        );
//        return Optional.of(albumRepository.save(album));
//    }

    @Override
    public Integer deleteAlbum(Long albumId, Long userId) {
        return albumRepository.deleteAlbumByIdAndUserId(albumId, userId);
    }

    @Override
    public AlbumResponse albumToResponse(Album album) {
        return new AlbumResponse(
                album.getTitle(),
                photoService.photosToThumbnails(album.getPhotos())
        );
    }

    @Override
    public List<AlbumResponse> albumsToResponses(List<Album> albums) {
        return albums.stream()
                .map(this::albumToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<List<Album>> getAllAlbums() {
        return Optional.of(albumRepository.findAll());
    }

    @Override
    public Optional<Album> getAlbum(Long id) {
        return albumRepository.findById(id);
    }

    @Override
    public Optional<Album> saveAlbum(Album album) {
        return Optional.of(albumRepository.save(album));
    }

    @Override
    public Integer deleteAlbum(Long id) {
        return albumRepository.deleteAlbumById(id);
    }
}