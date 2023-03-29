package org.cos7els.storage.service;

import org.cos7els.storage.model.Album;
import org.cos7els.storage.model.request.UpdateAlbumRequest;
import org.cos7els.storage.model.request.CreateAlbumRequest;
import org.cos7els.storage.model.response.AlbumResponse;

import java.util.List;
import java.util.Optional;

public interface AlbumService {
    Optional<List<Album>> getAllAlbums(Long userId);

    Optional<Album> getAlbum(Long albumId, Long userId);

    Optional<Album> saveAlbum(CreateAlbumRequest request, Long userId);

    Optional<Album> updateAlbum(Long albumId, UpdateAlbumRequest request, Long userId);

    Integer deleteAlbum(Long albumId, Long userId);

    AlbumResponse albumToResponse(Album album);

    List<AlbumResponse> albumsToResponses(List<Album> albums);

    Optional<List<Album>> getAllAlbums();

    Optional<Album> getAlbum(Long id);

    Optional<Album> saveAlbum(Album album);

    Integer deleteAlbum(Long id);
}