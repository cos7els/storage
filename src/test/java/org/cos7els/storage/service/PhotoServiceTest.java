package org.cos7els.storage.service;

import org.cos7els.storage.mapper.PhotoToPhotoResponseMapper;
import org.cos7els.storage.model.domain.Photo;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.cos7els.storage.repository.PhotoRepository;
import org.cos7els.storage.service.impl.PhotoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhotoServiceTest {
    @Mock
    private PhotoToPhotoResponseMapper photoToPhotoResponseMapper;
    @Mock
    private PhotoRepository photoRepository;
    @InjectMocks
    private PhotoServiceImpl photoService;
    private List<Photo> photos;
    private Long id;
    private List<ThumbnailResponse> thumbnails;

    public PhotoServiceTest() {
    }

    @BeforeEach
    public void init() {
        id = 1L;
        String string = "test";
        Photo photo = new Photo(id, id, string, string, id, id.intValue(), id.intValue(), Timestamp.valueOf(LocalDateTime.now()), id.doubleValue(), id.doubleValue(), string);
        photos = List.of(photo);
        thumbnails = List.of(new ThumbnailResponse());
    }

    @Test
    public void getThumbnailsTest() {
        when(photoRepository.getPhotosByUserIdOrderByCreationDate(id)).thenReturn(photos);
        when(photoToPhotoResponseMapper.photosToThumbnails(photos)).thenReturn(thumbnails);
        List<ThumbnailResponse> returned = photoService.getThumbnails(id);
        verify(photoRepository).getPhotosByUserIdOrderByCreationDate(id);
        verify(photoToPhotoResponseMapper).photosToThumbnails(photos);
        assertEquals(thumbnails, returned);
    }
}