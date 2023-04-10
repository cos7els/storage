package org.cos7els.storage.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.cos7els.storage.model.request.CreateUpdateAlbumRequest;
import org.cos7els.storage.model.response.AlbumResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AlbumController {
    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/albums")
    public ResponseEntity<List<AlbumResponse>> getAlbums(
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails
    ) {
        return new ResponseEntity<>(albumService.getAlbums(userDetails.getId()), HttpStatus.OK);
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<AlbumResponse> getAlbum(
            @PathVariable Long albumId,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails
    ) {
        return new ResponseEntity<>(albumService.getAlbum(albumId, userDetails.getId()), HttpStatus.OK);
    }

    @GetMapping("/album/{albumId}/download")
    public ResponseEntity<byte[]> downloadAlbum(
            @PathVariable Long albumId,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/zip"));
        headers.setContentDisposition(ContentDisposition.parse("attachment; filename=album.zip"));
        return new ResponseEntity<>(albumService.downloadAlbum(albumId, userDetails.getId()), headers, HttpStatus.OK);
    }

    @PostMapping("/album")
    public ResponseEntity<AlbumResponse> createAlbum(
            @RequestBody @Valid CreateUpdateAlbumRequest createUpdateAlbumRequest,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        return bindingResult.hasErrors() ?
                new ResponseEntity<>(HttpStatus.CONFLICT) :
                new ResponseEntity<>(
                        albumService.createAlbum(createUpdateAlbumRequest, userDetails.getId()),
                        HttpStatus.CREATED
                );
    }

    @PutMapping("/album/{albumId}")
    public ResponseEntity<AlbumResponse> updateAlbum(
            @PathVariable Long albumId,
            @RequestBody @Valid CreateUpdateAlbumRequest createUpdateAlbumRequest,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        return bindingResult.hasErrors() ?
                new ResponseEntity<>(HttpStatus.CONFLICT) :
                new ResponseEntity<>(albumService.updateAlbum(
                        albumId, createUpdateAlbumRequest, userDetails.getId()), HttpStatus.OK
                );
    }

    @DeleteMapping("/album/{albumId}")
    public ResponseEntity<HttpStatus> deleteAlbum(
            @PathVariable Long albumId,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails
    ) {
        albumService.deleteAlbum(albumId, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}