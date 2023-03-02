//package org.cos7els.storage.controller;
//
//import org.cos7els.storage.model.Photo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/photo")
//public class PhotoController {
//    private final PhotoServiceImpl photoService;
//
//    @Autowired
//    public PhotoController(PhotoServiceImpl service) {
//        this.photoService = service;
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Photo> getPhoto(@PathVariable long id) {
//        Optional<Photo> photo = photoService.getPhoto(id);
//        return photo.isPresent() ?
//                new ResponseEntity<>(photo.get(), HttpStatus.OK) :
//                new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @GetMapping
//    public Iterable<Photo> getPhotos() {
//        return photoService.getPhotos();
//    }
//
////    @GetMapping("/download/{id}")
////    public ResponseEntity<byte[]> download(@PathVariable Long id, @AuthenticationPrincipal User user) {
////        Optional<Photo> photo = photoService.getPhoto(id);
////        if (photo.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////        if (photo.get().getUserId() != user.getId()) return new ResponseEntity<>(HttpStatus.CONFLICT);
////        byte[] data = photo.get().getData();
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.valueOf(photo.get().getContentType()));
////        ContentDisposition build = ContentDisposition
////                .builder("attachment")
////                .filename(photo.get().getFileName())
////                .build();
////        headers.setContentDisposition(build);
////        return new ResponseEntity<>(data, headers, HttpStatus.OK);
////    }
//
////    @PostMapping
////    public Photo create(@RequestPart("data") MultipartFile file, @AuthenticationPrincipal User user) throws IOException {
////        return photoService.createPhoto(user, file.getOriginalFilename(), file.getContentType(), file.getSize(), file.getBytes());
////    }
//
//    @DeleteMapping("/{id}")
//    public void deletePhoto(@PathVariable long id) {
//        photoService.deletePhoto(id);
//    }
//
//    @DeleteMapping
//    public void deletePhotos() {
//        photoService.deletePhotos();
//    }
//}
