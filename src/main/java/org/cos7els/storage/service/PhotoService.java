package org.cos7els.storage.service;

import org.cos7els.storage.model.Photo;
import org.cos7els.storage.model.User;
import org.cos7els.storage.model.response.PhotoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PhotoService {

    Optional<Photo> getPhoto(Long photoId, Long userId);

    Optional<List<Photo>> getAllPhotos(Long userId);

    Photo uploadPhoto(MultipartFile file, Long userId) throws IOException;

    Optional<Photo> deletePhoto(Long photoId, Long userId);

    List<Photo> deleteAllPhotos(Long userId);

    PhotoResponse photoToResponse(Photo photo);

    List<PhotoResponse> photosToResponses(List<Photo> photos);
}
