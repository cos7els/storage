package org.cos7els.storage.service;

import org.cos7els.storage.model.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoService {
    Optional<Photo> getPhoto(Long id);

    List<Photo> getAllPhotos();

    Photo savePhoto(Photo photo);

    void deletePhoto(Long id);

    void deleteAllPhotos();

    boolean isPhotoExists(Long id);
}
