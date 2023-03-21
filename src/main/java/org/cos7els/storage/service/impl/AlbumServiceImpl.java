package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.Album;
import org.cos7els.storage.model.request.CreateAlbumRequest;
import org.cos7els.storage.repository.AlbumRepository;
import org.cos7els.storage.service.AlbumService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;

    public Optional<Album> getAlbum(Long albumId, Long userId) {
        return albumRepository.findAlbumByIdAndUserId(albumId, userId);
    }

    public List<Album> getAllAlbums(Long userId) {
        return albumRepository.findAlbumsByUserId(userId);
    }

    public Album saveAlbum(CreateAlbumRequest request, Long userId) {
        Album album = new Album();
        album.setUserId(userId);
        album.setTitle(request.getTitle());
        return albumRepository.save(album);
    }

    public void deleteAlbum(Long albumId, Long userId) {
        albumRepository.deleteAlbumByIdAndUserId(albumId, userId);
    }

    public void deleteAllAlbums(Long userId) {
        albumRepository.deleteAlbumsByUserId(userId);
    }

    public boolean isAlbumExists(Long id) {
        return albumRepository.existsById(id);
    }
}
