package org.cos7els.storage.controller;

import org.cos7els.storage.model.Album;
import org.cos7els.storage.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/album")
public class AlbumController {
    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable long id) {
        Optional<Album> album = albumService.getAlbum(id);
        return album.isPresent() ?
                new ResponseEntity<>(album.get(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<Iterable<Album>> getAlbums() {
        return new ResponseEntity<>(albumService.getAlbums(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        return new ResponseEntity<>(albumService.createAlbum(album), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteAlbum(@PathVariable long id) {
        albumService.deleteAlbum(id);
    }

    @DeleteMapping
    public void deleteUsers() {
        albumService.deleteAlbums();
    }
}