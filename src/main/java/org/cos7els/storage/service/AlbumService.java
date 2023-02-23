package org.cos7els.storage.service;

import org.cos7els.storage.model.Album;
import org.cos7els.storage.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Optional<Album> getAlbum(long id) {
        return albumRepository.findById(id);
    }

    public Iterable<Album> getAlbums() {
        return albumRepository.findAll();
    }

    public Album createAlbum(Album album) {
        return albumRepository.save(album);
    }

    public void deleteAlbum(long id) {
        albumRepository.deleteById(id);
    }

    public void deleteAlbums() {
        albumRepository.deleteAll();
    }
}
