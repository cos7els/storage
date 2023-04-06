package org.cos7els.storage.service;

import org.cos7els.storage.model.request.CreateUpdateAlbumRequest;
import org.cos7els.storage.model.response.AlbumResponse;

import java.util.List;

public interface AlbumService {
    List<AlbumResponse> getAlbums(Long userId);

    AlbumResponse getAlbum(Long albumId, Long userId);

    byte[] downloadAlbum(Long albumId, Long userId);

    AlbumResponse createAlbum(CreateUpdateAlbumRequest createUpdateAlbumRequest, Long userId);

    AlbumResponse updateAlbum(Long albumId, CreateUpdateAlbumRequest createUpdateAlbumRequest, Long userId);

    void deleteAlbum(Long albumId, Long userId);
}