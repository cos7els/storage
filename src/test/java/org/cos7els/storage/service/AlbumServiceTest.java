//package org.cos7els.storage.service;
//
//import org.cos7els.storage.model.Album;
//import org.cos7els.storage.repository.AlbumRepository;
//import org.cos7els.storage.service.impl.AlbumServiceImpl;
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
//public class AlbumServiceTest {
//    @Mock
//    private AlbumRepository albumRepository;
//    @InjectMocks
//    private AlbumServiceImpl albumService;
//    private final Album album = new Album(1L, 1L, "test");
//    private final Long id = 1L;
//
//    @Test
//    public void getAlbumTest() {
//        when(albumRepository.findById(id)).thenReturn(Optional.of(album));
//        Album returnedAlbum = albumService.getAlbum(id).orElse(new Album());
//        verify(albumRepository).findById(id);
//        assertEquals(album, returnedAlbum);
//    }
//
//    @Test
//    public void getAlbumsTest() {
//        Iterable<Album> list = List.of(new Album(), new Album(), new Album());
//        when(albumRepository.findAll()).thenReturn(list);
//        Iterable<Album> users = albumService.getAlbums();
//        verify(albumRepository).findAll();
//        assertEquals(list, users);
//    }
//
//    @Test
//    public void createAlbumTest() {
//        when(albumService.createAlbum(album)).thenReturn(album);
//        Album createdAlbum = albumService.createAlbum(album);
//        verify(albumRepository).save(album);
//        assertEquals(album, createdAlbum);
//    }
//
//    @Test
//    public void deleteAlbumTest() {
//        albumService.deleteAlbum(id);
//        verify(albumRepository).deleteById(id);
//    }
//
//    @Test
//    public void deleteAlbumsTest() {
//        albumService.deleteAlbums();
//        verify(albumRepository).deleteAll();
//    }
//}