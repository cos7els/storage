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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/album")
public class AlbumController {
    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable Long id) {
        Optional<Album> album = albumService.getAlbum(id);
        return album.isPresent() ?
                new ResponseEntity<>(album.get(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albums = albumService.getAllAlbums();
        return albums.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        return new ResponseEntity<>(albumService.saveAlbum(album), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable Long id, @RequestBody Album album) {
        return albumService.isAlbumExists(id) ?
                new ResponseEntity<>(albumService.saveAlbum(album), HttpStatus.OK) :
                new ResponseEntity<>(albumService.saveAlbum(album), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<HttpStatus> deleteAllAlbums() {
        albumService.deleteAllAlbums();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}