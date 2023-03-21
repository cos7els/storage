package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.Album;
import org.cos7els.storage.model.request.CreateAlbumRequest;
import org.cos7els.storage.security.UserDetailsImpl;
import org.cos7els.storage.service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping("/albums")
    public ResponseEntity<List<Album>> getAllAlbums(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<Album> albums = albumService.getAllAlbums(userDetails.getId());
        return albums.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/album/{id}")
    public ResponseEntity<Album> getAlbum(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Optional<Album> album = albumService.getAlbum(id, userDetails.getId());
        return album.isPresent() ?
                new ResponseEntity<>(album.get(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/album")
    public ResponseEntity<Album> createAlbum(
            @RequestBody CreateAlbumRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return new ResponseEntity<>(albumService.saveAlbum(request, userDetails.getId()), HttpStatus.CREATED);
    }

    //    @PutMapping("/album/{id}")
//    public ResponseEntity<Album> updateAlbum(@PathVariable Long id, @RequestBody Album album) {
//        return albumService.isAlbumExists(id) ?
//                new ResponseEntity<>(albumService.saveAlbum(album), HttpStatus.OK) :
//                new ResponseEntity<>(albumService.saveAlbum(album), HttpStatus.CREATED);
//    }

    @DeleteMapping("/album/{id}")
    public ResponseEntity<HttpStatus> deleteAlbum(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        albumService.deleteAlbum(id, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/albums")
    public ResponseEntity<HttpStatus> deleteAllAlbums(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        albumService.deleteAllAlbums(userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}