package org.cos7els.storage.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.InternalException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.mapper.PhotoToPhotoResponseMapper;
import org.cos7els.storage.model.domain.Photo;
import org.cos7els.storage.model.request.SelectPhotoRequest;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.cos7els.storage.repository.PhotoRepository;
import org.cos7els.storage.service.PhotoService;
import org.cos7els.storage.service.StorageService;
import org.cos7els.storage.service.UserService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.cos7els.storage.util.ExceptionMessage.INSERT_PHOTO_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.NOT_FOUND;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:variables.properties")
public class PhotoServiceImpl implements PhotoService {
    private final PhotoToPhotoResponseMapper photoToPhotoResponseMapper;
    private final PhotoRepository photoRepository;
    private final StorageService storageService;
    private final UserService userService;

    public byte[] downloadPhotos(SelectPhotoRequest selectPhotoRequest, Long userId) {
        return writeZipArchive(photoToPhotoResponseMapper.photosToResponses(photoRepository.getPhotosByIds(selectPhotoRequest.getIds())));
    }

    public byte[] downloadPhotos(List<Photo> photos) {
        return writeZipArchive(photoToPhotoResponseMapper.photosToResponses(photos));
    }

    private byte[] writeZipArchive(List<PhotoResponse> photoResponses) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(out)) {
            for (PhotoResponse p : photoResponses) {
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

    public List<ThumbnailResponse> getThumbnails(Long userId) {
        return photoToPhotoResponseMapper.photosToThumbnails(selectPhotos(userId));
    }

    public PhotoResponse getPhoto(Long photoId, Long userId) {
        return photoToPhotoResponseMapper.photoToResponse(selectPhoto(photoId, userId));
    }

    @Transactional
    public void uploadPhoto(List<MultipartFile> files, Long userId) {
        for (MultipartFile file : files) {
            if (isFileValid(file)) {
                String path = storageService.putPhoto(file, userId);
                if (photoRepository.existsPhotoByPath(path)) {
                    continue;
                }
                userService.updateUsedSpace(userId, file.getSize());
                createUploadedPhoto(file, path, userId);
            }
        }
    }

    private boolean isFileValid(MultipartFile file) {
        return !file.isEmpty() && (file.getOriginalFilename() != null) && Objects.equals(file.getContentType(), "image/jpeg");
    }

    private void createUploadedPhoto(MultipartFile file, String path, Long userId) {
        Photo photo = new Photo();
        photo.setUserId(userId);
        photo.setFileName(checkFileName(file));
        photo.setContentType(file.getContentType());
        photo.setSize(file.getSize());
        photo.setPath(path);
        extractExif(file, photo);
        Photo savedPhoto = photoRepository.save(photo);
        if (savedPhoto == null) {
            throw new InternalException(INSERT_PHOTO_EXCEPTION);
        }
    }

    private String checkFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        for (int i = 1; photoRepository.existsPhotoByFileName(fileName); i++) {
            fileName = file.getOriginalFilename();
            fileName = String.format("%s (%d)%s",
                    fileName.substring(0, fileName.lastIndexOf('.')),
                    i,
                    fileName.substring(fileName.lastIndexOf('.')));
        }
        return fileName;
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

    private List<Photo> selectPhotos(Long userId) {
        return photoRepository.findPhotosByUserIdOrderByCreationDate(userId);
//        if (photos.isEmpty()) {
//            throw new NoContentException();
//        }
//        return photos;
    }

    private Photo selectPhoto(Long photoId, Long userId) {
        return photoRepository.getPhotoByIdAndUserId(photoId, userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Transactional
    public void deletePhotos(SelectPhotoRequest request, Long userId) {
        deletePhotos(
                getPhotosByIds(request.getIds(), userId),
                userId
        );
    }

    public void deletePhoto(Long photoId, Long userId) {
        deletePhotos(List.of(selectPhoto(photoId, userId)), userId);
    }

    @Transactional
    public void deletePhotos(Long userId) {
        deletePhotos(selectPhotos(userId), userId);
    }

    private void deletePhotos(List<Photo> photos, Long userId) {
        long count = photos.stream().mapToLong(Photo::getSize).sum();
        photoRepository.deleteAllInBatch(photos);
        storageService.removePhotos(photos);
        userService.updateUsedSpace(userId, count * -1L);
    }

    public List<Photo> getPhotosByIds(List<Long> photoIds, Long userId) {
        return photoRepository.findAllById(photoIds)
                .stream()
                .filter(p -> p.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}