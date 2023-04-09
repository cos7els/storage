package org.cos7els.storage.service;

import org.cos7els.storage.mapper.AlbumToAlbumResponseMapper;
import org.cos7els.storage.model.domain.Album;
import org.cos7els.storage.model.domain.Photo;
import org.cos7els.storage.model.request.CreateUpdateAlbumRequest;
import org.cos7els.storage.model.response.AlbumResponse;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.cos7els.storage.repository.AlbumRepository;
import org.cos7els.storage.service.impl.AlbumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlbumServiceTest {
    private List<Long> ids;
    @Mock
    private AlbumToAlbumResponseMapper albumToAlbumResponseMapper;
    @Mock
    private AlbumRepository albumRepository;
    @Mock
    private PhotoService photoService;
    @InjectMocks
    private AlbumServiceImpl albumService;
    private CreateUpdateAlbumRequest createUpdateAlbumRequest;
    private List<AlbumResponse> albumResponses;
    private AlbumResponse albumResponse;
    private List<Photo> photos;
    private Album album;
    private byte[] bytes;
    private List<Album> albums;
    private Long id;

    @BeforeEach
    public void init() {
        id = 1L;
        ids = List.of(id);
        String string = "test";
        Photo photo = new Photo(id, id, string, string, id, id.intValue(), id.intValue(), Timestamp.valueOf(LocalDateTime.now()), id.doubleValue(), id.doubleValue(), string);
        photos = List.of(photo);
        album = new Album(null, id, string, photos);
        albums = List.of(album);
        bytes = new byte[id.intValue()];
        ThumbnailResponse thumbnail = new ThumbnailResponse(id, bytes);
        List<ThumbnailResponse> thumbnails = List.of(thumbnail);
        albumResponse = new AlbumResponse(id, string, thumbnails);
        albumResponses = List.of(albumResponse);
        createUpdateAlbumRequest = new CreateUpdateAlbumRequest(string, ids);
    }

    @Test
    public void getAlbumsTest() {
        when(albumRepository.findAlbumsByUserId(id)).thenReturn(albums);
        when(albumToAlbumResponseMapper.albumsToResponses(albums)).thenReturn(albumResponses);
        List<AlbumResponse> returnedAlbums = albumService.getAlbums(id);
        verify(albumRepository).findAlbumsByUserId(id);
        verify(albumToAlbumResponseMapper).albumsToResponses(albums);
        assertEquals(albumResponses, returnedAlbums);
    }

    @Test
    public void getAlbumTest() {
        when(albumRepository.findAlbumByIdAndUserId(id, id)).thenReturn(Optional.of(album));
        when(albumToAlbumResponseMapper.albumToResponse(album)).thenReturn(albumResponse);
        AlbumResponse returnedAlbum = albumService.getAlbum(id, id);
        verify(albumRepository).findAlbumByIdAndUserId(id, id);
        verify(albumToAlbumResponseMapper).albumToResponse(album);
        assertEquals(albumResponse, returnedAlbum);
    }

    @Test
    public void downloadAlbumTest() {
        when(albumRepository.findAlbumByIdAndUserId(id, id)).thenReturn(Optional.of(album));
        when(photoService.downloadPhotos(album.getPhotos())).thenReturn(bytes);
        byte[] returnedBytes = albumService.downloadAlbum(id, id);
        verify(albumRepository).findAlbumByIdAndUserId(id, id);
        verify(photoService).downloadPhotos(album.getPhotos());
        assertEquals(bytes, returnedBytes);
    }

    @Test
    public void createAlbumTest() {
        when(photoService.getPhotosByIds(ids, id)).thenReturn(photos);
        when(albumRepository.save(album)).thenReturn(album);
        when(albumToAlbumResponseMapper.albumToResponse(album)).thenReturn(albumResponse);
        AlbumResponse returnedAlbumResponse = albumService.createAlbum(createUpdateAlbumRequest, id);
        verify(photoService).getPhotosByIds(ids, id);
        verify(albumRepository).save(album);
        verify(albumToAlbumResponseMapper).albumToResponse(album);
        assertEquals(returnedAlbumResponse, albumResponse);
    }

    @Test
    public void updateAlbumTest() {
        when(albumRepository.findAlbumByIdAndUserId(id, id)).thenReturn(Optional.of(album));
        when(photoService.getPhotosByIds(ids, id)).thenReturn(photos);
        when(albumRepository.save(album)).thenReturn(album);
        when(albumToAlbumResponseMapper.albumToResponse(album)).thenReturn(albumResponse);
        AlbumResponse returnedAlbumResponse = albumService.updateAlbum(id, createUpdateAlbumRequest, id);
        verify(photoService).getPhotosByIds(ids, id);
        verify(albumRepository).save(album);
        verify(albumToAlbumResponseMapper).albumToResponse(album);
        assertEquals(returnedAlbumResponse, albumResponse);
    }

    @Test
    public void deleteAlbumTest() {
        when(albumRepository.deleteAlbumByIdAndUserId(id, id)).thenReturn(id.intValue());
        albumService.deleteAlbum(id, id);
        verify(albumRepository).deleteAlbumByIdAndUserId(id, id);
    }
}