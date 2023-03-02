//package org.cos7els.storage.service;
//
//import org.cos7els.storage.model.Photo;
//import org.cos7els.storage.model.User;
//import org.cos7els.storage.repository.PhotoRepository;
//import org.cos7els.storage.service.impl.PhotoServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class PhotoServiceTest {
//    @Mock
//    private PhotoRepository photoRepository;
//    @InjectMocks
//    private PhotoServiceImpl photoService;
//    private final long id = 1L;
//    private final Photo photo = new Photo(0L, 1L, "test.jpg", "image/jpg", 0.0D, new byte[0]);
//
//    @Test
//    public void getPhotoTest() {
//        when(photoRepository.findById(id)).thenReturn(Optional.of(photo));
//        Photo returnedPhoto = photoService.getPhoto(id).orElse(new Photo());
//        verify(photoRepository).findById(id);
//        assertEquals(photo, returnedPhoto);
//    }
//
//    @Test
//    public void getPhotosTest() {
//        Iterable<Photo> list = List.of(new Photo(), new Photo(), new Photo());
//        when(photoRepository.findAll()).thenReturn(list);
//        Iterable<Photo> photos = photoService.getPhotos();
//        verify(photoRepository).findAll();
//        assertEquals(list, photos);
//    }
//
//    @Test
//    public void createPhotoTest() {
//        when(photoRepository.save(photo)).thenReturn(photo);
//        Photo createdPhoto = photoService.createPhoto(new User(), photo.getFileName(), photo.getContentType(), 0L, photo.getData());
//        verify(photoRepository).save(photo);
//        assertEquals(photo, createdPhoto);
//    }
//
//    @Test
//    public void deletePhotoTest() {
//        photoService.deletePhoto(id);
//        verify(photoRepository).deleteById(id);
//    }
//
//    @Test
//    public void deletePhotosTest() {
//        photoService.deletePhotos();
//        verify(photoRepository).deleteAll();
//    }
//}
