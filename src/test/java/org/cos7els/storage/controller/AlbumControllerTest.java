package org.cos7els.storage.controller;

import org.cos7els.storage.model.request.CreateUpdateAlbumRequest;
import org.cos7els.storage.model.response.AlbumResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.AlbumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AlbumControllerTest {
    @Mock
    private AlbumService albumService;
    @InjectMocks
    private AlbumController albumController;
    private Long id;
    private byte[] bytes;
    private UserDetailsImpl userDetails;
    private AlbumResponse albumResponse;
    private List<AlbumResponse> albumResponses;
    private CreateUpdateAlbumRequest createUpdateAlbumRequest;

    @BeforeEach
    public void init() {
        id = 1L;
        userDetails = new UserDetailsImpl(id, null, null, null);
        albumResponse = new AlbumResponse();
        albumResponses = List.of(albumResponse);
        bytes = new byte[id.intValue()];
        createUpdateAlbumRequest = new CreateUpdateAlbumRequest();
    }

    @Test
    public void getAlbumsTest() {
        when(albumService.getAlbums(id)).thenReturn(albumResponses);
        ResponseEntity<List<AlbumResponse>> albums = albumController.getAlbums(userDetails);
        verify(albumService).getAlbums(id);
        assertEquals(albumResponses, albums.getBody());
    }

    @Test
    public void getAlbumTest() {
        when(albumService.getAlbum(id, id)).thenReturn(albumResponse);
        ResponseEntity<AlbumResponse> album = albumController.getAlbum(id, userDetails);
        verify(albumService).getAlbum(id, id);
        assertEquals(albumResponse, album.getBody());
    }

    @Test
    public void downloadAlbumTest() {
        when(albumService.downloadAlbum(id, id)).thenReturn(bytes);
        ResponseEntity<byte[]> response = albumController.downloadAlbum(id, userDetails);
        verify(albumService).downloadAlbum(id, id);
        assertEquals(bytes, response.getBody());
    }

    @Test
    public void createAlbumTest() {
        when(albumService.createAlbum(createUpdateAlbumRequest, id)).thenReturn(albumResponse);
        ResponseEntity<AlbumResponse> album = albumController.createAlbum(createUpdateAlbumRequest, userDetails);
        verify(albumService).createAlbum(createUpdateAlbumRequest, id);
        assertEquals(albumResponse, album.getBody());
    }

    @Test
    public void updateAlbumTest() {
        when(albumService.updateAlbum(id, createUpdateAlbumRequest, id)).thenReturn(albumResponse);
        ResponseEntity<AlbumResponse> album = albumController.updateAlbum(id, createUpdateAlbumRequest, userDetails);
        verify(albumService).updateAlbum(id, createUpdateAlbumRequest, id);
        assertEquals(albumResponse, album.getBody());
    }

    @Test
    public void deleteAlbumTest() {
        albumController.deleteAlbum(id, userDetails);
        verify(albumService).deleteAlbum(id, id);
    }
}
