package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.request.SelectPhotoRequest;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.PhotoService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("/photos")
    public ResponseEntity<List<ThumbnailResponse>> getThumbnails(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return new ResponseEntity<>(
                photoService.getThumbnails(userDetails.getId()),
                HttpStatus.OK
        );
    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<PhotoResponse> getPhoto(
            @PathVariable Long photoId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        PhotoResponse response = photoService.getPhoto(photoId, userDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/photos/download")
    public ResponseEntity<byte[]> downloadPhotos(
            @RequestBody SelectPhotoRequest selectPhotoRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        if (selectPhotoRequest.getIds().size() == 1) {
            return downloadPhoto(selectPhotoRequest.getIds().get(0), userDetails);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/zip"));
        headers.setContentDisposition(ContentDisposition.parse("attachment; filename=photos.zip"));
        return new ResponseEntity<>(
                photoService.downloadPhotos(selectPhotoRequest, userDetails.getId()),
                headers,
                HttpStatus.OK
        );
    }

    @GetMapping("/photo/{photoId}/download")
    public ResponseEntity<byte[]> downloadPhoto(
            @PathVariable Long photoId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        PhotoResponse photoResponse = photoService.getPhoto(photoId, userDetails.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(photoResponse.getContentType()));
        headers.setContentDisposition(ContentDisposition.parse("attachment; filename=" + photoResponse.getFileName()));
        return new ResponseEntity<>(photoResponse.getData(), headers, HttpStatus.OK);
    }

    @PostMapping("/photos/upload")
    public ResponseEntity<HttpStatus> uploadPhotos(
            @RequestPart("files") List<MultipartFile> files,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        photoService.uploadPhoto(files, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/photo")
    public ResponseEntity<HttpStatus> deletePhotos(
            @RequestBody SelectPhotoRequest selectPhotoRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        photoService.deletePhotos(selectPhotoRequest, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/photo/{photoId}")
    public ResponseEntity<HttpStatus> deletePhoto(
            @PathVariable Long photoId, @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        photoService.deletePhoto(photoId, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/photos")
    public ResponseEntity<HttpStatus> deletePhotos(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        photoService.deletePhotos(userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}