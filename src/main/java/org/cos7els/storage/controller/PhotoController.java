package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.request.SelectPhotoRequest;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.cos7els.storage.security.UserDetailsImpl;
import org.cos7els.storage.service.PhotoService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("/photos")
    public ResponseEntity<List<ThumbnailResponse>> getAllPhotos(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return new ResponseEntity<>(
                photoService.getPhotos(userDetails.getId()),
                HttpStatus.OK
        );
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<PhotoResponse> getPhoto(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        PhotoResponse response = photoService.getPhoto(id, userDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/photos/download")
    public ResponseEntity<byte[]> downloadPhotos(
            @RequestBody SelectPhotoRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (request.getIds().size() == 1) {
            return downloadPhoto(request.getIds().get(0), userDetails);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/zip"));
//        headers.setContentDisposition(ContentDisposition.attachment().build());
        headers.setContentDisposition(ContentDisposition.parse("attachment; filename=photos.zip"));
        return new ResponseEntity<>(
                photoService.downloadPhotos(request, userDetails.getId()),
                headers,
                HttpStatus.OK
        );
    }

    @GetMapping("/photo/{id}/download")
    public ResponseEntity<byte[]> downloadPhoto(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        PhotoResponse response = photoService.getPhoto(id, userDetails.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(response.getContentType()));
        headers.setContentDisposition(
                ContentDisposition
                        .parse("attachment; filename=" + response.getFileName())
        );
        return new ResponseEntity<>(response.getData(), headers, HttpStatus.OK);
    }

    @PostMapping("/photos/upload")
    public ResponseEntity<HttpStatus> uploadPhoto(
            @RequestPart("files") MultipartFile[] files,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        photoService.uploadPhoto(files, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/photo")
    public ResponseEntity<HttpStatus> deletePhoto(
            @RequestBody SelectPhotoRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        photoService.deletePhotos(request, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/photos")
    public ResponseEntity<HttpStatus> deletePhotos(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        photoService.deleteAllUsersPhotos(userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}