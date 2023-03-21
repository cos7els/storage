package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.Photo;
import org.cos7els.storage.security.UserDetailsImpl;
import org.cos7els.storage.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("/photos")
    public ResponseEntity<List<Photo>> getAllPhotos(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<Photo> photos = photoService.getAllPhotos(userDetails.getId());
        return photos.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(photos, HttpStatus.OK);
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<Photo> getPhoto(@PathVariable Long id,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<Photo> photo = photoService.getPhoto(id, userDetails.getId());
        return photo.isPresent() ?
                new ResponseEntity<>(photo.get(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/upload")
    public ResponseEntity<Photo> uploadPhoto(@RequestPart("data") MultipartFile file,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        Photo photo = photoService.uploadPhoto(file, userDetails.getId());
        return new ResponseEntity<>(photo, HttpStatus.CREATED);
    }


//    @GetMapping
//    public ResponseEntity<List<Photo>> getPhoto(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        List<Photo> photo = photoRepository.findAllByUser(userRepository.findById(userDetails.getId()));
//        return new ResponseEntity<>(photo, HttpStatus.OK);
//    }

//    @GetMapping("/download/{id}")
//    public ResponseEntity<byte[]> download(@PathVariable Long id, @AuthenticationPrincipal User user) {
//        Optional<Photo> photo = photoService.getPhoto(id);
//        if (photo.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        if (photo.get().getUserId() != user.getId()) return new ResponseEntity<>(HttpStatus.CONFLICT);
//        byte[] data = photo.get().getData();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.valueOf(photo.get().getContentType()));
//        ContentDisposition build = ContentDisposition
//                .builder("attachment")
//                .filename(photo.get().getFileName())
//                .build();
//        headers.setContentDisposition(build);
//        return new ResponseEntity<>(data, headers, HttpStatus.OK);
//    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Photo> deletePhoto(@PathVariable Long id,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<Photo> photo = photoService.deletePhoto(id, userDetails.getId());
        return photo.isPresent() ?
                new ResponseEntity<>(photo.get(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<List<Photo>> deleteAllPhotos(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<Photo> photos = photoService.deleteAllPhotos(userDetails.getId());
        return photos.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(photos, HttpStatus.OK);
    }
}
