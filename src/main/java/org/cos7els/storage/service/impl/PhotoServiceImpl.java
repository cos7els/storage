//package org.cos7els.storage.service.impl;
//
//import org.cos7els.storage.model.Photo;
//import org.cos7els.storage.repository.PhotoRepository;
//import org.cos7els.storage.service.PhotoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class PhotoServiceImpl implements PhotoService {
//    private final PhotoRepository photoRepository;
//
//    @Autowired
//    public PhotoServiceImpl(PhotoRepository photoRepository) {
//        this.photoRepository = photoRepository;
//    }
//
//    public Optional<Photo> getPhoto(long id) {
//        return photoRepository.findById(id);
//    }
//
//    public Iterable<Photo> getPhotos() {
//        return photoRepository.findAll();
//    }
//
////    public Photo createPhoto(User user, String fileName, String contentType, long size, byte[] data) {
////            Photo photo = new Photo();
////            photo.setUserId(user.getId());
////            photo.setFileName(fileName);
////            photo.setContentType(contentType);
////            double usedSpace = (double) size / 1000000;
////            photo.setSize(usedSpace);
////            photo.setData(data);
////            return photoRepository.save(photo);
////    }
//
//    public void deletePhoto(long id) {
//        photoRepository.deleteById(id);
//    }
//
//    public void deletePhotos() {
//        photoRepository.deleteAll();
//    }
//}
