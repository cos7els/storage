package org.cos7els.storage.controller;

import org.cos7els.storage.model.request.SelectedPhotoRequest;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhotoControllerTest {
    @Mock
    private PhotoService photoService;
    @InjectMocks
    private PhotoController photoController;
    private Long id;
    private UserDetailsImpl userDetails;
    private List<ThumbnailResponse> thumbnailResponses;
    private PhotoResponse photoResponse;
    private SelectedPhotoRequest selectedPhotoRequest;
    private byte[] bytes;
    private List<MultipartFile> files;

    @BeforeEach
    public void init() {
        id = 1L;
        userDetails = new UserDetailsImpl(id, null, null, null);
        thumbnailResponses = List.of(new ThumbnailResponse());
        photoResponse = new PhotoResponse();
        selectedPhotoRequest = new SelectedPhotoRequest();
        bytes = new byte[id.intValue()];
        files = new ArrayList<>();
    }

    @Test
    public void getThumbnailsTest() {
        when(photoService.getThumbnails(userDetails.getId())).thenReturn(thumbnailResponses);
        ResponseEntity<List<ThumbnailResponse>> response = photoController.getThumbnails(userDetails);
        verify(photoService).getThumbnails(userDetails.getId());
        assertEquals(thumbnailResponses, response.getBody());
    }

    @Test
    public void getPhotoTest() {
        when(photoService.getPhoto(id, userDetails.getId())).thenReturn(photoResponse);
        ResponseEntity<PhotoResponse> response = photoController.getPhoto(id, userDetails);
        verify(photoService).getPhoto(id, userDetails.getId());
        assertEquals(photoResponse, response.getBody());
    }

    @Test
    public void downloadPhotosTest() {
        selectedPhotoRequest.setIds(List.of(id, id));
        when(photoService.downloadPhotos(selectedPhotoRequest, userDetails.getId())).thenReturn(bytes);
        ResponseEntity<byte[]> response = photoController.downloadPhotos(selectedPhotoRequest, userDetails);
        verify(photoService).downloadPhotos(selectedPhotoRequest, userDetails.getId());
        assertEquals(bytes, response.getBody());
    }

    @Test
    public void downloadPhotoTest() {
        photoResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        photoResponse.setData(bytes);
        photoResponse.setFileName("img.jpg");
        when(photoService.getPhoto(id, userDetails.getId())).thenReturn(photoResponse);
        ResponseEntity<byte[]> response = photoController.downloadPhoto(id, userDetails);
        verify(photoService).getPhoto(id, userDetails.getId());
        assertEquals(bytes, response.getBody());
    }

    @Test
    public void uploadPhotosTest() {
        photoController.uploadPhotos(files, userDetails);
        verify(photoService).uploadPhoto(files, userDetails.getId());
    }

    @Test
    public void deletePhotosTest() {
        photoController.deletePhotos(selectedPhotoRequest, userDetails);
        verify(photoService).deletePhotos(selectedPhotoRequest, userDetails.getId());
    }

    @Test
    public void deletePhotoTest() {
        photoController.deletePhoto(id, userDetails);
        verify(photoService).deletePhoto(id, userDetails.getId());
    }
}