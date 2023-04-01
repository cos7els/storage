package org.cos7els.storage.service;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.cos7els.storage.model.Photo;
import org.cos7els.storage.model.User;
import org.cos7els.storage.model.response.PhotoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public interface PhotoService {

    Optional<Photo> getPhoto(Long photoId, Long userId);

    Optional<List<Photo>> getAllPhotos(Long userId);

    Photo uploadPhoto(MultipartFile file, Long userId) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    Optional<Photo> deletePhoto(Long photoId, Long userId);

    List<Photo> deleteAllPhotos(Long userId);

    PhotoResponse photoToResponse(Photo photo);

    List<PhotoResponse> photosToResponses(List<Photo> photos);
}
