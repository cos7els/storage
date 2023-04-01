package org.cos7els.storage.service.impl;

import io.github.rctcwyvrn.blake3.Blake3;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.Photo;
import org.cos7els.storage.model.User;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.repository.PhotoRepository;
import org.cos7els.storage.repository.UserRepository;
import org.cos7els.storage.service.PhotoService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.cos7els.storage.util.ExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:minio.properties")
public class PhotoServiceImpl implements PhotoService {
    @Value("${photo-bucket}")
    private String photoBucket;
    @Value("${thumbnail-bucket}")
    private String thumbnailBucket;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final MinioClient minioClient;
    private final Blake3 blake3;

    public Optional<Photo> getPhoto(Long photoId, Long userId) {
        return photoRepository.findPhotoByIdAndUserId(photoId, userId);
    }

    public Optional<List<Photo>> getAllPhotos(Long userId) {
        return photoRepository.findPhotosByUserIdOrderById(userId);
    }

    public Photo uploadPhoto(MultipartFile file, Long userId) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        System.out.println(file.getInputStream().available());
        String filename = file.getOriginalFilename();
        String hash = hash(user.getUsername(), filename);
        String contentType = file.getContentType();
        InputStream thumbnailInputStream = generateThumbnail(file.getInputStream());
        InputStream photoInputStream = file.getInputStream();
        putObject(photoBucket, hash, photoInputStream, contentType);
        putObject(thumbnailBucket, hash, thumbnailInputStream, contentType);
        Photo photo = createUploadedPhoto(file, user, hash);
        user.setUsedSpace(user.getUsedSpace() + file.getSize());
        userRepository.save(user);
        return photoRepository.save(photo);
    }

    public void putObject(
            String bucket,
            String hash,
            InputStream inputStream,
            String contentType
    ) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try (inputStream) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(hash)
                            .stream(inputStream, inputStream.available(), -1L)
                            .contentType(contentType)
                            .build()
            );
        }
    }

    public InputStream generateThumbnail(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (inputStream) {
            Thumbnailator.createThumbnail(inputStream, byteArrayOutputStream, 300, 300);
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private String hash(String username, String filename) {
        blake3.update(filename.getBytes());
        String hash = blake3.hexdigest();
        return String.format(
                "%s/%s/%s.%s",
                username,
                hash.substring(0, 2),
                hash.substring(2),
                filename.substring(filename.indexOf('.'))
        );
    }

    private void createBucketIfNotExists(String username) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(username)
                        .build());
        if (!bucketExists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(username)
                            .build()
            );
        }
    }

    public Optional<Photo> deletePhoto(Long photoId, Long userId) {
        Optional<Photo> photo = photoRepository.findPhotoByIdAndUserId(photoId, userId);
        if (photo.isPresent()) {
            deleteFile(photo.get().getPath());
        }
        photoRepository.deletePhotoByIdAndUserId(photoId, userId);
        return photo;
    }

    public List<Photo> deleteAllPhotos(Long userId) {
        List<Photo> photos = photoRepository.findPhotosByUserId(userId);
        for (Photo p : photos) {
            deleteFile(p.getPath());
        }
        photoRepository.deletePhotosByUserId(userId);
        return photos;
    }

    private Photo createUploadedPhoto(MultipartFile file, User user, String root) {
        Photo photo = new Photo();
        photo.setUserId(user.getId());
        photo.setFileName(file.getOriginalFilename());
        photo.setContentType(file.getContentType());
        photo.setSize(file.getSize());
        photo.setPath(root);
        return photo;
    }

    private void deleteFile(String path) {
        File file = Paths.get(path).toFile();
        file.delete();
    }


//    public Photo createPhoto(User user, String fileName, String contentType, Long size, byte[] data) {
//            Photo photo = new Photo();
//            photo.setUserId(user.getId());
//            photo.setFileName(fileName);
//            photo.setContentType(contentType);
//            double usedSpace = (double) size / 1000000;
//            photo.setSize(usedSpace);
//            photo.setData(data);
//            return photoRepository.save(photo);
//    }

    public PhotoResponse photoToResponse(@NotNull Photo photo) {
        return new PhotoResponse(
                photo.getId(),
                photo.getFileName(),
                photo.getSize(),
                photo.getPath()
        );
    }

    public List<PhotoResponse> photosToResponses(List<Photo> photos) {
        return photos.stream()
                .map(this::photoToResponse)
                .collect(Collectors.toList());
    }
}