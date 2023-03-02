package org.cos7els.storage.service.impl;

import org.cos7els.storage.model.Album;
import org.cos7els.storage.repository.AlbumRepository;
import org.cos7els.storage.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumServiceImpl(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Optional<Album> getAlbum(Long id) {
        return albumRepository.findById(id);
    }

    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    public Album saveAlbum(Album album) {
        return albumRepository.save(album);
    }

    public void deleteAlbum(Long id) {
        albumRepository.deleteById(id);
    }

    public void deleteAllAlbums() {
        albumRepository.deleteAll();
    }

    public boolean isAlbumExists(Long id) {
        return albumRepository.existsById(id);
    }
}
