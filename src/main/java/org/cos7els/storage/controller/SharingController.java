package org.cos7els.storage.controller;

import org.cos7els.storage.model.Sharing;
import org.cos7els.storage.service.SharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/sharing")
public class SharingController {
    private final SharingService sharingService;

    @Autowired
    public SharingController(SharingService sharingService) {
        this.sharingService = sharingService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sharing> getSharing(@PathVariable long id) {
        Optional<Sharing> sharing = sharingService.getSharing(id);
        return sharing.isPresent() ?
                new ResponseEntity<>(sharing.get(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<Iterable<Sharing>> getSharings() {
        return new ResponseEntity<>(sharingService.getSharings(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Sharing> createSharing(@RequestBody Sharing sharing) {
        return new ResponseEntity<>(sharingService.createSharing(sharing), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteSharing(@PathVariable long id) {
        sharingService.deleteSharing(id);
    }

    @DeleteMapping
    public void deleteSharings() {
        sharingService.deleteSharings();
    }
}
