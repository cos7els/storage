package org.cos7els.storage.service.impl;

import io.github.rctcwyvrn.blake3.Blake3;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.domain.Photo;
import org.cos7els.storage.model.domain.User;
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
    @Value("${STORAGE_PHOTO_BUCKET}")
    private String photosBucket;
    @Value("${STORAGE_THUMBNAILS_BUCKET}")
    private String thumbnailsBucket;
    private final UserService userService;
    private final MinioClient minioClient;

    public String putPhoto(MultipartFile file, Long userId) {
        User user = userService.getUser(userId);
        String path = hash(user.getUsername(), file);
        String contentType = file.getContentType();
        InputStream thumbnail = generateThumbnail(file);
        putObject(photosBucket, path, file, contentType);
        putObject(thumbnailsBucket, path, thumbnail, contentType);
        return path;
    }

    public byte[] getPhoto(Photo photo) {
        return getObject(photosBucket, photo);
    }

    public byte[] getThumbnail(Photo photo) {
        return getObject(thumbnailsBucket, photo);
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
        removeObject(photosBucket, photo);
        removeObject(thumbnailsBucket, photo);
    }

    public void removePhotos(List<Photo> photos) {
        removeObjects(photosBucket, photos);
        removeObjects(thumbnailsBucket, photos);
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

    private String hash(String username, MultipartFile file) {
        Blake3 blake3 = Blake3.newInstance();
//        String filename = file.getOriginalFilename();
        try {
            blake3.update(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String hash = blake3.hexdigest();
        return String.format(
                "%s/%s/%s",
                username,
                hash.substring(0, 2),
                hash.substring(2)
//                filename.substring(filename.lastIndexOf('.'))
        );
    }

    private InputStream generateThumbnail(MultipartFile file) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = file.getInputStream()) {
            Thumbnailator.createThumbnail(inputStream, byteArrayOutputStream, 300, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}