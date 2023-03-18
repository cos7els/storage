package org.cos7els.storage.service.impl;

import org.cos7els.storage.model.Photo;
import org.cos7els.storage.model.User;
import org.cos7els.storage.repository.PhotoRepository;
import org.cos7els.storage.repository.UserRepository;
import org.cos7els.storage.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoServiceImpl implements PhotoService {
    private final Path ROOT = Paths.get("data");
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository, UserRepository userRepository) {
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
    }

    public Optional<Photo> getPhoto(Long photoId, Long userId) {
        return photoRepository.findPhotoByIdAndUserId(photoId, userId);
    }

    public List<Photo> getAllPhotos(Long userId) {
        return photoRepository.findPhotosByUserId(userId);
    }

    public Photo uploadPhoto(MultipartFile file, Long userId) throws IOException {
        User user = userRepository.findById(userId).get();
        String filename = file.getOriginalFilename();
        File currentUserPath = ROOT.resolve(user.getUsername()).toFile();
        if (!currentUserPath.exists()) {
            currentUserPath.mkdir();
        }
        Files.copy(file.getInputStream(), currentUserPath.toPath().resolve(filename));
        Photo photo = createUploadedPhoto(file, user);
        user.setUsedSpace(user.getUsedSpace() + file.getSize());
        userRepository.save(user);
        return photoRepository.save(photo);
    }

    @Override
    public Optional<Photo> deletePhoto(Long photoId, Long userId) {
        Optional<Photo> photo = photoRepository.findPhotoByIdAndUserId(photoId, userId);
        if (photo.isPresent()) {
            deleteFile(photo.get().getPath());
        }
        photoRepository.deletePhotoByIdAndUserId(photoId, userId);
        return photo;
    }

    @Override
    public List<Photo> deleteAllPhotos(Long userId) {
        List<Photo> photos = photoRepository.findPhotosByUserId(userId);
        for (Photo p : photos) {
            deleteFile(p.getPath());
        }
        photoRepository.deletePhotosByUserId(userId);
        return photos;
    }

    private Photo createUploadedPhoto(MultipartFile file, User user) {
        Photo photo = new Photo();
        photo.setUserId(user.getId());
        photo.setPath(ROOT.resolve(user.getUsername()).resolve(file.getOriginalFilename()).toString());
        photo.setSize(file.getSize());
        photo.setFileName(file.getOriginalFilename());
        photo.setContentType(file.getContentType());
        return photo;
    }

    private void deleteFile(String path) {
        File file = Paths.get(path).toFile();
        file.delete();
    }


//    public Photo createPhoto(User user, String fileName, String contentType, Long size, byte[] data) {
//            Photo photo = new Photo();
//            photo.setUserId(user.getId());
//            photo.setFileName(fileName);
//            photo.setContentType(contentType);
//            double usedSpace = (double) size / 1000000;
//            photo.setSize(usedSpace);
//            photo.setData(data);
//            return photoRepository.save(photo);
//    }

}
