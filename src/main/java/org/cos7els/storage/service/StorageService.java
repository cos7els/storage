package org.cos7els.storage.service;

import org.cos7els.storage.model.domain.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    void putPhoto(MultipartFile file, String hashedPath);

    byte[] getPhoto(Photo photo);

    byte[] getThumbnail(Photo photo);

    void removePhotos(List<Photo> photos);

    void removePhotos(String username);
}