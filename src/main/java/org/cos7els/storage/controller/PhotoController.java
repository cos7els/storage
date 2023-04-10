package org.cos7els.storage.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.cos7els.storage.model.request.SelectedPhotoRequest;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.PhotoService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PhotoController {
    private final PhotoService photoService;

    @Autowired
    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @GetMapping("/photos")
    public ResponseEntity<List<ThumbnailResponse>> getThumbnails(
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails
    ) {
        return new ResponseEntity<>(photoService.getThumbnails(userDetails.getId()), HttpStatus.OK);
    }

    @GetMapping("/photo/{photoId}")
    public ResponseEntity<PhotoResponse> getPhoto(
            @PathVariable Long photoId,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails
    ) {
        PhotoResponse response = photoService.getPhoto(photoId, userDetails.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/photos/download")
    public ResponseEntity<byte[]> downloadPhotos(
            @RequestBody @Valid SelectedPhotoRequest selectedPhotoRequest,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (selectedPhotoRequest.getIds().size() == 1) {
            return downloadPhoto(selectedPhotoRequest.getIds().get(0), userDetails);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/zip"));
        headers.setContentDisposition(ContentDisposition.parse("attachment; filename=photos.zip"));
        return new ResponseEntity<>(
                photoService.downloadPhotos(selectedPhotoRequest, userDetails.getId()), headers, HttpStatus.OK
        );
    }

    @GetMapping("/photo/{photoId}/download")
    public ResponseEntity<byte[]> downloadPhoto(
            @PathVariable Long photoId,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails
    ) {
        PhotoResponse photoResponse = photoService.getPhoto(photoId, userDetails.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(photoResponse.getContentType()));
        headers.setContentDisposition(
                ContentDisposition.parse("attachment; filename=" + photoResponse.getFileName())
        );
        return new ResponseEntity<>(photoResponse.getData(), headers, HttpStatus.OK);
    }

    @PostMapping(path = "/photos/upload", consumes = "multipart/form-data")
    public ResponseEntity<HttpStatus> uploadPhotos(
            @RequestPart("files") List<MultipartFile> files,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails
    ) {
        photoService.uploadPhoto(files, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/photos")
    public ResponseEntity<HttpStatus> deletePhotos(
            @RequestBody @Valid SelectedPhotoRequest selectedPhotoRequest,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        photoService.deletePhotos(selectedPhotoRequest, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/photo/{photoId}/delete")
    public ResponseEntity<HttpStatus> deletePhoto(
            @PathVariable Long photoId,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails
    ) {
        photoService.deletePhoto(photoId, userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}