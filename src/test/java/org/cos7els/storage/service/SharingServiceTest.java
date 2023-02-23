package org.cos7els.storage.service;

import org.cos7els.storage.model.Sharing;
import org.cos7els.storage.repository.SharingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SharingServiceTest {
    @Mock
    private SharingRepository sharingRepository;
    @InjectMocks
    private SharingService sharingService;
    private final Sharing sharing = new Sharing(1L, 1L, 1L);
    private final Long id = 1L;

    @Test
    public void getSharingTest() {
        when(sharingRepository.findById(id)).thenReturn(Optional.of(sharing));
        Sharing returnedSharing = sharingService.getSharing(id).orElse(new Sharing());
        verify(sharingRepository).findById(id);
        assertEquals(sharing, returnedSharing);
    }

    @Test
    public void getSharingsTest() {
        Iterable<Sharing> list = List.of(new Sharing(), new Sharing(), new Sharing());
        when(sharingRepository.findAll()).thenReturn(list);
        Iterable<Sharing> sharings = sharingService.getSharings();
        verify(sharingRepository).findAll();
        assertEquals(list, sharings);
    }

    @Test
    public void createSharingTest() {
        when(sharingService.createSharing(sharing)).thenReturn(sharing);
        Sharing createdSharing = sharingService.createSharing(sharing);
        verify(sharingRepository).save(sharing);
        assertEquals(sharing, createdSharing);
    }

    @Test
    public void deleteSharingTest() {
        sharingService.deleteSharing(id);
        verify(sharingRepository).deleteById(id);
    }

    @Test
    public void deleteSharingsTest() {
        sharingService.deleteSharings();
        verify(sharingRepository).deleteAll();
    }
}
