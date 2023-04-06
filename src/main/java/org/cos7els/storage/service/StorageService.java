package org.cos7els.storage.service;

import org.cos7els.storage.model.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface StorageService {
    String putPhoto(MultipartFile file, Long userId);

    byte[] getPhoto(Photo photo);

    byte[] getThumbnail(Photo photo);

    void removePhoto(Photo photo);

    void removePhotos(List<Photo> photos);

}