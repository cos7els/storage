package org.cos7els.storage.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.CustomException;
import org.cos7els.storage.exception.NoContentException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.Photo;
import org.cos7els.storage.model.request.SelectPhotoRequest;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.cos7els.storage.repository.PhotoRepository;
import org.cos7els.storage.service.PhotoService;
import org.cos7els.storage.service.StorageService;
import org.cos7els.storage.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.cos7els.storage.util.ExceptionMessage.INSERT_PHOTO_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.NOT_FOUND;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:variables.properties")
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    private final StorageService storageService;
    private final UserService userService;
    @Value("${ZIP_MAX_SIZE}")
    private Double ZIP_MAX_SIZE;

    public byte[] downloadPhotos(SelectPhotoRequest request, Long userId) {
        List<PhotoResponse> photos = photosToResponses(getPhotosByRequest(request.getIds(), userId));
        int parts = calculateParts(photos.stream().mapToLong(PhotoResponse::getSize).sum());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            for (PhotoResponse p : photos) {
                ZipEntry entry = new ZipEntry(p.getFileName());
                zos.putNextEntry(entry);
                zos.write(p.getData());
                zos.closeEntry();
            }
            zos.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    private int calculateParts(long size) {
        return (int) Math.ceil(
                size / ZIP_MAX_SIZE
        );
    }

    public List<ThumbnailResponse> getPhotos(Long userId) {
        List<Photo> photos = selectPhotos(userId);
        return photosToThumbnails(photos);
    }

    public PhotoResponse getPhoto(Long photoId, Long userId) {
        return photoToResponse(selectPhoto(photoId, userId));
    }

    public void uploadPhoto(MultipartFile[] file, Long userId) {
        Arrays.asList(file).forEach(f -> {
            userService.updateUsedSpace(userId, f.getSize());
            String path = storageService.putPhoto(f, userId);
            createUploadedPhoto(f, path, userId);
        });
    }

    private void createUploadedPhoto(MultipartFile file, String path, Long userId) {
        Photo photo = new Photo();
        photo.setUserId(userId);
        photo.setFileName(file.getOriginalFilename());
        photo.setContentType(file.getContentType());
        photo.setSize(file.getSize());
        photo.setPath(path);
        extractExif(file, photo);
        Photo savedPhoto = photoRepository.save(photo);
        if (savedPhoto == null) {
            throw new CustomException(INSERT_PHOTO_EXCEPTION);
        }
    }

    private void extractExif(MultipartFile file, Photo photo) {
        try (InputStream in = file.getInputStream()) {
            Metadata metadata = ImageMetadataReader.readMetadata(in);
            JpegDirectory jpegDir = metadata.getFirstDirectoryOfType(JpegDirectory.class);
            if (jpegDir != null) {
                photo.setHeight(jpegDir.getImageHeight());
                photo.setWidth(jpegDir.getImageWidth());
            } else {
                photo.setHeight(0);
                photo.setWidth(0);
            }
            ExifSubIFDDirectory exifSubIFDDir = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            Date dateOriginal;
            if (exifSubIFDDir != null && (dateOriginal = exifSubIFDDir.getDateOriginal()) != null) {
                photo.setCreationDate(new Timestamp(dateOriginal.getTime()));
            } else {
                photo.setCreationDate(new Timestamp(new Date().getTime()));
            }
            GpsDirectory gpsDir = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            GeoLocation geoLocation;
            if (gpsDir != null && (geoLocation = gpsDir.getGeoLocation()) != null) {
                photo.setLatitude(geoLocation.getLatitude());
                photo.setLongitude(geoLocation.getLongitude());
            } else {
                photo.setLatitude(0D);
                photo.setLongitude(0D);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ThumbnailResponse> photosToThumbnails(List<Photo> photos) {
        List<ThumbnailResponse> thumbnails = new ArrayList<>();
        try {
            thumbnails = photos.stream()
                    .map(this::photoToThumbnail)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbnails;
    }

    public ThumbnailResponse photoToThumbnail(Photo photo) {
        return new ThumbnailResponse(
                photo.getId(), storageService.getThumbnail(photo)
        );
    }

    public List<PhotoResponse> photosToResponses(List<Photo> photos) {
        return photos.stream()
                .map(this::photoToResponse)
                .collect(Collectors.toList());
    }

    public PhotoResponse photoToResponse(Photo photo) {
        return new PhotoResponse(
                photo.getCreationDate(),
                photo.getFileName(),
                photo.getContentType(),
                String.format("%s x %s", photo.getHeight(), photo.getWidth()),
                photo.getSize(),
                photo.getLatitude(),
                photo.getLongitude(),
                storageService.getPhoto(photo)
        );
    }

    private List<Photo> selectPhotos(Long userId) {
        List<Photo> photos = photoRepository.findPhotosByUserIdOrderByCreationDate(userId);
        if (photos.isEmpty()) {
            throw new NoContentException();
        }
        return photos;
    }

    private Photo selectPhoto(Long photoId, Long userId) {
        return photoRepository.findPhotoByIdAndUserId(photoId, userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Transactional
    public void deletePhotos(SelectPhotoRequest request, Long userId) {
        deletePhotos(
                getPhotosByRequest(request.getIds(), userId),
                userId
        );
    }

    @Transactional
    public void deleteAllUsersPhotos(Long userId) {
        deletePhotos(selectPhotos(userId), userId);
    }


    private void deletePhotos(List<Photo> photos, Long userId) {
        long count = photos.stream().mapToLong(Photo::getSize).sum();
        photoRepository.deleteAllInBatch(photos);
        storageService.removePhotos(photos);
        userService.updateUsedSpace(userId, count * -1L);
    }

    private List<Photo> getPhotosByRequest(List<Long> ids, Long userId) {
        return photoRepository.findAllById(ids)
                .stream()
                .filter(p -> p.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}