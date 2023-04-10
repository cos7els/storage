package org.cos7els.storage.service.impl;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import io.github.rctcwyvrn.blake3.Blake3;
import org.cos7els.storage.exception.InternalException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.mapper.PhotoToPhotoResponseMapper;
import org.cos7els.storage.model.domain.Photo;
import org.cos7els.storage.model.request.SelectedPhotoRequest;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.cos7els.storage.repository.PhotoRepository;
import org.cos7els.storage.repository.UserRepository;
import org.cos7els.storage.service.PhotoService;
import org.cos7els.storage.service.StorageService;
import org.cos7els.storage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
@PropertySource("classpath:variables.properties")
public class PhotoServiceImpl implements PhotoService {
    private final PhotoToPhotoResponseMapper photoToPhotoResponseMapper;
    private final PhotoRepository photoRepository;
    private final StorageService storageService;
    private final UserService userService;
    private final UserRepository userRepository;
    @Value("${extension}")
    private String extension;

    @Autowired
    public PhotoServiceImpl(
            PhotoToPhotoResponseMapper photoToPhotoResponseMapper,
            PhotoRepository photoRepository,
            StorageService storageService,
            UserService userService,
            UserRepository userRepository
    ) {
        this.photoToPhotoResponseMapper = photoToPhotoResponseMapper;
        this.photoRepository = photoRepository;
        this.storageService = storageService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public List<ThumbnailResponse> getThumbnails(Long userId) {
        return photoToPhotoResponseMapper.photosToThumbnails(selectPhotos(userId));
    }

    public PhotoResponse getPhoto(Long photoId, Long userId) {
        return photoToPhotoResponseMapper.photoToResponse(selectPhoto(photoId, userId));
    }

    public byte[] downloadPhotos(SelectedPhotoRequest selectedPhotoRequest, Long userId) {
        return writeZipArchive(photoToPhotoResponseMapper.photosToResponses(
                photoRepository.findAllById(selectedPhotoRequest.getIds()))
        );
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

    @Transactional
    public void uploadPhoto(List<MultipartFile> files, Long userId) {
        userService.checkUsedSpace(userId);
        String username = userRepository.getUsernameById(userId);
        long size = 0;
        for (MultipartFile file : files) {
            String hashedPath = generateHashedPath(username, file);
            if (isFileValid(file) && !photoRepository.existsPhotoByPath(hashedPath)) {
                storageService.putPhoto(file, hashedPath);
                createUploadedPhoto(file, hashedPath, userId);
                size += file.getSize();
            }
        }
        userService.updateUsedSpace(userId, size);
    }

    private boolean isFileValid(MultipartFile file) {
        return !file.isEmpty() &&
                (file.getOriginalFilename() != null) &&
                Objects.equals(file.getContentType(), "image/jpeg");
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
            if (fileName != null) {
                fileName = String.format(
                        "%s (%d)%s",
                        fileName.substring(0, fileName.lastIndexOf('.')),
                        i, fileName.substring(fileName.lastIndexOf('.'))
                );
            }
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
        return photoRepository.getPhotosByUserIdOrderByCreationDate(userId);
    }

    private Photo selectPhoto(Long photoId, Long userId) {
        return photoRepository.getPhotoByIdAndUserId(photoId, userId).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Transactional
    public void deletePhotos(SelectedPhotoRequest request, Long userId) {
        deletePhotos(getPhotosByIds(request.getIds(), userId), userId);
    }

    public void deletePhoto(Long photoId, Long userId) {
        deletePhotos(List.of(selectPhoto(photoId, userId)), userId);
    }

    private void deletePhotos(List<Photo> photos, Long userId) {
        long size = photos.stream().mapToLong(Photo::getSize).sum();
        photoRepository.deleteAllInBatch(photos);
        storageService.removePhotos(photos);
        userService.updateUsedSpace(userId, size * -1L);
    }

    public List<Photo> getPhotosByIds(List<Long> photoIds, Long userId) {
        return photoRepository.findAllById(photoIds).stream().filter(
                p -> p.getUserId().equals(userId)).collect(Collectors.toList()
        );
    }

    private String generateHashedPath(String dir, MultipartFile file) {
        Blake3 blake3 = Blake3.newInstance();
        try {
            blake3.update(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String hash = blake3.hexdigest();
        String subDir = hash.substring(0, 2);
        String filename = hash.substring(2);
        return String.format("%s/%s/%s%s", dir, subDir, filename, extension);
    }
}