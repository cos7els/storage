package org.cos7els.storage.service.impl;

import io.github.rctcwyvrn.blake3.Blake3;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.Photo;
import org.cos7els.storage.model.User;
import org.cos7els.storage.repository.UserRepository;
import org.cos7els.storage.service.StorageService;
import org.cos7els.storage.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.cos7els.storage.util.ExceptionMessage.OBJECT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:storage.properties")
public class StorageServiceImpl implements StorageService {
    @Value("${photo-bucket}")
    private String photoBucket;
    @Value("${thumbnail-bucket}")
    private String thumbnailBucket;
    private final UserService userService;
    private final MinioClient minioClient;
    private final Blake3 blake3;

    public String putPhoto(MultipartFile file, Long userId) {
        User user = userService.getUser(userId);
        String filename = file.getOriginalFilename();
        String path = generatePath(user.getUsername(), filename);
        String contentType = file.getContentType();
        InputStream thumbnail = generateThumbnail(file);
        putObject(photoBucket, path, file, contentType);
        putObject(thumbnailBucket, path, thumbnail, contentType);
        return path;
    }

    public byte[] getPhoto(Photo photo) {
        return getObject(photoBucket, photo);
    }

    public byte[] getThumbnail(Photo photo) {
        return getObject(thumbnailBucket, photo);
    }

    private void putObject(
            String bucketName,
            String hashedPath,
            MultipartFile file,
            String contentType
    ) {
        try {
            putObject(bucketName, hashedPath, file.getInputStream(), contentType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putObject(
            String bucketName,
            String hashedPath,
            InputStream in,
            String contentType
    ) {
        try (in) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(hashedPath)
                            .stream(in, in.available(), -1L)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getObject(String bucketName, Photo photo) {
        byte[] object = null;
        try (InputStream in = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(photo.getPath())
                        .build())) {
            object = in.readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (object == null) {
            throw new NotFoundException(OBJECT_NOT_FOUND);
        }
        return object;
    }

    public void removePhoto(Photo photo) {
        removeObject(photoBucket, photo);
        removeObject(thumbnailBucket, photo);
    }

    public void removePhotos(List<Photo> photos) {
        removeObjects(photoBucket, photos);
        removeObjects(thumbnailBucket, photos);
    }

    private void removeObject(String bucketName, Photo photo) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(photo.getPath())
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeObjects(String bucketName, List<Photo> photos) {
        List<DeleteObject> objects = photos.stream()
                .map(p -> new DeleteObject(p.getPath()))
                .collect(Collectors.toList());
        try {
            minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket(bucketName)
                            .objects(objects)
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generatePath(String username, String filename) {
        blake3.update(filename.getBytes());
        String hash = blake3.hexdigest();
        return String.format(
                "%s/%s/%s.%s",
                username,
                hash.substring(0, 2),
                hash.substring(2),
                filename.substring(filename.lastIndexOf('.'))
        );
    }

    private InputStream generateThumbnail(MultipartFile file) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream in = file.getInputStream()) {
            Thumbnailator.createThumbnail(in, byteArrayOutputStream, 300, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}