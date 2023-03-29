package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.Photo;
import org.cos7els.storage.model.User;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.repository.PhotoRepository;
import org.cos7els.storage.repository.UserRepository;
import org.cos7els.storage.service.PhotoService;
import org.jetbrains.annotations.NotNull;
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
import java.util.stream.Collectors;

import static org.cos7els.storage.util.ExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final Path ROOT = Paths.get("data");
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    public Optional<Photo> getPhoto(Long photoId, Long userId) {
        return photoRepository.findPhotoByIdAndUserId(photoId, userId);
    }

    public Optional<List<Photo>> getAllPhotos(Long userId) {
        return photoRepository.findPhotosByUserIdOrderById(userId);
    }

    public Photo uploadPhoto(MultipartFile file, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
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

    public Optional<Photo> deletePhoto(Long photoId, Long userId) {
        Optional<Photo> photo = photoRepository.findPhotoByIdAndUserId(photoId, userId);
        if (photo.isPresent()) {
            deleteFile(photo.get().getPath());
        }
        photoRepository.deletePhotoByIdAndUserId(photoId, userId);
        return photo;
    }

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
        photo.setFileName(file.getOriginalFilename());
        photo.setContentType(file.getContentType());
        photo.setSize(file.getSize());
        photo.setPath(ROOT.resolve(user.getUsername()).resolve(file.getOriginalFilename()).toString());
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

    public PhotoResponse photoToResponse(@NotNull Photo photo) {
        return new PhotoResponse(
                photo.getId(),
                photo.getFileName(),
                photo.getSize(),
                photo.getPath()
        );
    }

    public List<PhotoResponse> photosToResponses(@NotNull List<Photo> photos) {
        return photos.stream()
                .map(this::photoToResponse)
                .collect(Collectors.toList());
    }
}