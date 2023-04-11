package org.cos7els.storage.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import net.coobird.thumbnailator.Thumbnailator;
import org.cos7els.storage.exception.InternalException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.domain.Photo;
import org.cos7els.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.cos7els.storage.util.ExceptionMessage.GENERATE_THUMBNAIL_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.GET_OBJECT_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.OBJECT_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.PUT_OBJECT_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.REMOVE_OBJECT_EXCEPTION;

@Service
@PropertySource("classpath:storage.properties")
public class StorageServiceImpl implements StorageService {
    private final MinioClient minioClient;
    @Value("${storage_photo_bucket}")
    private String photosBucket;
    @Value("${storage_thumbnail_bucket}")
    private String thumbnailsBucket;

    @Autowired
    public StorageServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public void putPhoto(MultipartFile file, String hashedPath) {
        String contentType = file.getContentType();
        InputStream thumbnail = generateThumbnail(file);
        putObject(photosBucket, hashedPath, file, contentType);
        putObject(thumbnailsBucket, hashedPath, thumbnail, contentType);
    }

    public byte[] getPhoto(Photo photo) {
        return getObject(photosBucket, photo);
    }

    public byte[] getThumbnail(Photo photo) {
        return getObject(thumbnailsBucket, photo);
    }

    private void putObject(String bucketName, String hashedPath, MultipartFile file, String contentType) {
        try {
            putObject(bucketName, hashedPath, file.getInputStream(), contentType);
        } catch (IOException e) {
            throw new InternalException(PUT_OBJECT_EXCEPTION);
        }
    }

    private void putObject(String bucketName, String hashedPath, InputStream in, String contentType) {
        try (in) {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(hashedPath)
                            .stream(in, in.available(), -1L)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            throw new InternalException(PUT_OBJECT_EXCEPTION);
        }
    }

    private byte[] getObject(String bucketName, Photo photo) {
        byte[] object;
        try (InputStream in = minioClient.getObject(
                GetObjectArgs
                        .builder()
                        .bucket(bucketName)
                        .object(photo.getPath())
                        .build())
        ) {
            object = in.readAllBytes();
        } catch (Exception e) {
            throw new InternalException(GET_OBJECT_EXCEPTION);
        }
        if (object == null) {
            throw new NotFoundException(OBJECT_NOT_FOUND);
        }
        return object;
    }

    public void removePhotos(List<Photo> photos) {
        removeObjects(photosBucket, photos);
        removeObjects(thumbnailsBucket, photos);
    }

    private void removeObjects(String bucketName, List<Photo> photos) {
        List<DeleteObject> objects = photos.stream().map(p -> new DeleteObject(p.getPath())).collect(Collectors.toList());
        try {
            Iterable<Result<DeleteError>> results =
                    minioClient.
                            removeObjects(
                                    RemoveObjectsArgs.builder()
                                            .bucket(bucketName)
                                            .objects(objects)
                                            .build()
                            );
            results.forEach(System.out::println);
        } catch (Exception e) {
            throw new InternalException(REMOVE_OBJECT_EXCEPTION);
        }
    }

    private InputStream generateThumbnail(MultipartFile file) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = file.getInputStream()) {
            Thumbnailator.createThumbnail(inputStream, byteArrayOutputStream, 300, 300);
        } catch (Exception e) {
            throw new InternalException(GENERATE_THUMBNAIL_EXCEPTION);
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}