package org.cos7els.storage.service;

import org.cos7els.storage.model.Album;
import org.cos7els.storage.model.request.CreateAlbumRequest;

import java.util.List;
import java.util.Optional;

public interface AlbumService {
    Optional<Album> getAlbum(Long albumId, Long userId);

    List<Album> getAllAlbums(Long userId);

    Album saveAlbum(CreateAlbumRequest request, Long userId);

    void deleteAlbum(Long albumId, Long userId);

    void deleteAllAlbums(Long userId);

    boolean isAlbumExists(Long id);
}
