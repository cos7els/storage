package org.cos7els.storage.service.impl;

import org.cos7els.storage.exception.InternalException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.mapper.AlbumToAlbumResponseMapper;
import org.cos7els.storage.model.domain.Album;
import org.cos7els.storage.model.domain.Photo;
import org.cos7els.storage.model.request.CreateUpdateAlbumRequest;
import org.cos7els.storage.model.response.AlbumResponse;
import org.cos7els.storage.repository.AlbumRepository;
import org.cos7els.storage.service.AlbumService;
import org.cos7els.storage.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.cos7els.storage.util.ExceptionMessage.ALBUM_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.DELETE_ALBUM_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.INSERT_ALBUM_EXCEPTION;

@Service
public class AlbumServiceImpl implements AlbumService {
    private final AlbumToAlbumResponseMapper albumToAlbumResponseMapper;
    private final AlbumRepository albumRepository;
    private final PhotoService photoService;

    @Autowired
    public AlbumServiceImpl(AlbumToAlbumResponseMapper albumToAlbumResponseMapper, AlbumRepository albumRepository, PhotoService photoService) {
        this.albumToAlbumResponseMapper = albumToAlbumResponseMapper;
        this.albumRepository = albumRepository;
        this.photoService = photoService;
    }

    public List<AlbumResponse> getAlbums(Long userId) {
        List<Album> albums = albumRepository.findAlbumsByUserId(userId);
        return albumToAlbumResponseMapper.albumsToResponses(albums);
    }

    public AlbumResponse getAlbum(Long albumId, Long userId) {
        return albumToAlbumResponseMapper.albumToResponse(selectAlbum(albumId, userId));
    }

    public byte[] downloadAlbum(Long albumId, Long userId) {
        List<Photo> photos = selectAlbum(albumId, userId).getPhotos();
        return photoService.downloadPhotos(photos);
    }

    public AlbumResponse createAlbum(CreateUpdateAlbumRequest createUpdateAlbumRequest, Long userId) {
        Album album = new Album();
        album.setUserId(userId);
        return createUpdateAlbum(album, createUpdateAlbumRequest, userId);
    }

    public AlbumResponse updateAlbum(Long albumId, CreateUpdateAlbumRequest createUpdateAlbumRequest, Long userId) {
        return createUpdateAlbum(selectAlbum(albumId, userId), createUpdateAlbumRequest, userId);
    }

    private AlbumResponse createUpdateAlbum(Album album, CreateUpdateAlbumRequest createUpdateAlbumRequest, Long userId) {
        List<Photo> photos = photoService.getPhotosByIds(createUpdateAlbumRequest.getPhotoIds(), userId);
        album.setTitle(createUpdateAlbumRequest.getTitle());
        album.setPhotos(photos);
        return albumToAlbumResponseMapper.albumToResponse(insertAlbum(album));
    }

    public void deleteAlbum(Long albumId, Long userId) {
        int result = albumRepository.deleteAlbumByIdAndUserId(albumId, userId);
        if (result == 0) {
            throw new InternalException(DELETE_ALBUM_EXCEPTION);
        }
    }

    private Album selectAlbum(Long albumId, Long userId) {
        return albumRepository.findAlbumByIdAndUserId(albumId, userId)
                .orElseThrow(() -> new NotFoundException(ALBUM_NOT_FOUND));
    }

    private Album insertAlbum(Album album) {
        Album savedAlbum = albumRepository.save(album);
        if (savedAlbum == null) {
            throw new InternalException(INSERT_ALBUM_EXCEPTION);
        }
        return savedAlbum;
    }
}