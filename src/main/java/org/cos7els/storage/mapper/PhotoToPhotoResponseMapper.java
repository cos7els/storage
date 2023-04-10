package org.cos7els.storage.mapper;

import org.cos7els.storage.model.domain.Photo;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.cos7els.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PhotoToPhotoResponseMapper {
    private final StorageService storageService;

    @Autowired
    public PhotoToPhotoResponseMapper(StorageService storageService) {
        this.storageService = storageService;
    }

    public List<ThumbnailResponse> photosToThumbnails(List<Photo> photos) {
        List<ThumbnailResponse> thumbnails = new ArrayList<>();
        try {
            thumbnails = photos.stream().map(this::photoToThumbnail).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbnails;
    }

    public ThumbnailResponse photoToThumbnail(Photo photo) {
        return new ThumbnailResponse(photo.getId(), storageService.getThumbnail(photo));
    }

    public List<PhotoResponse> photosToResponses(List<Photo> photos) {
        return photos.stream().map(this::photoToResponse).collect(Collectors.toList());
    }

    public PhotoResponse photoToResponse(Photo photo) {
        return new PhotoResponse(
                photo.getCreationDate(),
                photo.getFileName(),
                photo.getContentType(),
                String.format("%s x %s",
                        photo.getHeight(),
                        photo.getWidth()),
                photo.getSize(),
                photo.getLatitude(),
                photo.getLongitude(),
                storageService.getPhoto(photo)
        );
    }
}