package org.cos7els.storage.service;

import org.cos7els.storage.model.Sharing;
import org.cos7els.storage.repository.SharingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SharingService {
    private final SharingRepository sharingRepository;

    @Autowired
    public SharingService(SharingRepository sharingRepository) {
        this.sharingRepository = sharingRepository;
    }

    public Optional<Sharing> getSharing(long id) {
        return sharingRepository.findById(id);
    }

    public Iterable<Sharing> getSharings() {
        return sharingRepository.findAll();
    }

    public Sharing createSharing(Sharing sharing) {
        return sharingRepository.save(sharing);
    }

    public void deleteSharing(long id) {
        sharingRepository.deleteById(id);
    }

    public void deleteSharings() {
        sharingRepository.deleteAll();
    }
}
