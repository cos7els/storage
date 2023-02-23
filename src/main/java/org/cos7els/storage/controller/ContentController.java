package org.cos7els.storage.controller;

import org.cos7els.storage.model.Content;
import org.cos7els.storage.service.ContentService;
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
@RequestMapping("/content")
public class ContentController {
    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> getContent(@PathVariable long id) {
        Optional<Content> content = contentService.getContent(id);
        return content.isPresent() ?
                new ResponseEntity<>(content.get(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<Iterable<Content>> getContents() {
        return new ResponseEntity<>(contentService.getContents(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Content> createContent(@RequestBody Content content) {
        return new ResponseEntity<>(contentService.createContent(content), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteContent(@PathVariable long id) {
        contentService.deleteContent(id);
    }

    @DeleteMapping
    public void deleteContents() {
        contentService.deleteContents();
    }
}
