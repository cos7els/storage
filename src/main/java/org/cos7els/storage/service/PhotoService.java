package org.cos7els.storage.service;

import org.cos7els.storage.model.Photo;
import org.cos7els.storage.model.request.SelectPhotoRequest;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {

    List<ThumbnailResponse> getPhotos(Long userId);

    PhotoResponse getPhoto(Long photoId, Long userId);

    void uploadPhoto(MultipartFile[] file, Long userId);

    void deletePhotos(SelectPhotoRequest request, Long userId);

    void deletePhoto(Long photoId, Long userId);

    void deletePhotos(Long userId);

    List<ThumbnailResponse> photosToThumbnails(List<Photo> photos);

    ThumbnailResponse photoToThumbnail(Photo photo);

    List<PhotoResponse> photosToResponses(List<Photo> photos);

    PhotoResponse photoToResponse(Photo photo);

    byte[] downloadPhotos(SelectPhotoRequest selectPhotoRequest, Long userId);

    byte[] dowloadPhotos(List<Photo> photos);

    List<Photo> getPhotosByIds(List<Long> photoIds, Long userId);
}
