package org.cos7els.storage.service;

import org.cos7els.storage.model.Content;
import org.cos7els.storage.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContentService {
    private final ContentRepository contentRepository;

    @Autowired
    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    public Optional<Content> getContent(long id) {
        return contentRepository.findById(id);
    }

    public Iterable<Content> getContents() {
        return contentRepository.findAll();
    }

    public Content createContent(Content content) {
        return contentRepository.save(content);
    }

    public void deleteContent(long id) {
        contentRepository.deleteById(id);
    }

    public void deleteContents() {
        contentRepository.deleteAll();
    }
}
