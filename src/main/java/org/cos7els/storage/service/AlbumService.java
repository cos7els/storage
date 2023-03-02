package org.cos7els.storage.service;

import org.cos7els.storage.model.Album;

import java.util.List;
import java.util.Optional;

public interface AlbumService {
    Optional<Album> getAlbum(Long id);

    List<Album> getAllAlbums();

    Album saveAlbum(Album album);

    void deleteAlbum(Long id);

    void deleteAllAlbums();

    boolean isAlbumExists(Long id);
}
