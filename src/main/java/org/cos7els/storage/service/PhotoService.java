package org.cos7els.storage.service;

import org.cos7els.storage.model.domain.Photo;
import org.cos7els.storage.model.request.SelectedPhotoRequest;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {

    List<ThumbnailResponse> getThumbnails(Long userId);

    PhotoResponse getPhoto(Long photoId, Long userId);

    void uploadPhoto(List<MultipartFile> files, Long userId);

    void deletePhotos(SelectedPhotoRequest request, Long userId);

    void deletePhoto(Long photoId, Long userId);

    byte[] downloadPhotos(SelectedPhotoRequest selectedPhotoRequest, Long userId);

    byte[] downloadPhotos(List<Photo> photos);

    List<Photo> getPhotosByIds(List<Long> photoIds, Long userId);
}