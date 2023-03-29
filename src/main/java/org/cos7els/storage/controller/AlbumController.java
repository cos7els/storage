package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.CustomException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.Album;
import org.cos7els.storage.model.request.CreateAlbumRequest;
import org.cos7els.storage.model.request.UpdateAlbumRequest;
import org.cos7els.storage.model.response.AlbumResponse;
import org.cos7els.storage.security.UserDetailsImpl;
import org.cos7els.storage.service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.cos7els.storage.util.ExceptionMessage.ALBUMS_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.ALBUM_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.CREATE_ALBUM_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.UPDATE_ALBUM_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.DELETE_ALBUM_EXCEPTION;

@RestController
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping("/albums")
    public ResponseEntity<List<AlbumResponse>> getAllAlbums(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<Album> albums = albumService.getAllAlbums(userDetails.getId())
                .orElseThrow(() -> new NotFoundException(ALBUMS_NOT_FOUND));
        return new ResponseEntity<>(
                albumService.albumsToResponses(albums), HttpStatus.OK
        );
    }

    @GetMapping("/album/{id}")
    public ResponseEntity<AlbumResponse> getAlbum(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Album album = albumService.getAlbum(id, userDetails.getId())
                .orElseThrow(() -> new NotFoundException(ALBUM_NOT_FOUND));
        return new ResponseEntity<>(
                albumService.albumToResponse(album), HttpStatus.OK
        );
    }

    @PostMapping("/album")
    public ResponseEntity<AlbumResponse> createAlbum(
            @RequestBody CreateAlbumRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Album album = albumService.saveAlbum(request, userDetails.getId())
                .orElseThrow(() -> new CustomException(CREATE_ALBUM_EXCEPTION));
        return new ResponseEntity<>(
                albumService.albumToResponse(album), HttpStatus.CREATED
        );
    }

    @PutMapping("/album/{id}")
    public ResponseEntity<AlbumResponse> updateAlbum(
            @PathVariable Long id,
            @RequestBody UpdateAlbumRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Album album = albumService.updateAlbum(id, request, userDetails.getId())
                .orElseThrow(() -> new CustomException(UPDATE_ALBUM_EXCEPTION));
        return new ResponseEntity<>(
                albumService.albumToResponse(album), HttpStatus.OK
        );
    }

    @DeleteMapping("/album/{id}")
    public ResponseEntity<HttpStatus> deleteAlbum(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Integer result = albumService.deleteAlbum(id, userDetails.getId());
        if (result == 0) {
            throw new CustomException(DELETE_ALBUM_EXCEPTION);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/admin/albums")
    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albums = albumService.getAllAlbums()
                .orElseThrow(() -> new NotFoundException(ALBUMS_NOT_FOUND));
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/admin/album/{id}")
    public ResponseEntity<Album> getAlbum(@PathVariable Long id) {
        Album album = albumService.getAlbum(id)
                .orElseThrow(() -> new NotFoundException(ALBUM_NOT_FOUND));
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @PostMapping("/admin/album")
    public ResponseEntity<Album> createAlbum(@RequestBody Album request) {
        Album album = albumService.saveAlbum(request)
                .orElseThrow(() -> new CustomException(CREATE_ALBUM_EXCEPTION));
        return new ResponseEntity<>(album, HttpStatus.CREATED);
    }

    @PutMapping("/admin/album")
    public ResponseEntity<Album> updateAlbum(@RequestBody Album request) {
        Album album = albumService.saveAlbum(request)
                .orElseThrow(() -> new CustomException(UPDATE_ALBUM_EXCEPTION));
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @DeleteMapping("/admin/album/{id}")
    public ResponseEntity<HttpStatus> deleteAlbum(@PathVariable Long id) {
        Integer result = albumService.deleteAlbum(id);
        if (result == 0) {
            throw new CustomException(DELETE_ALBUM_EXCEPTION);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}