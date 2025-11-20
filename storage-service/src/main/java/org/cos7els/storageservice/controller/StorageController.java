package org.cos7els.storageservice.controller;

import org.cos7els.storageservice.model.File;
import org.cos7els.storageservice.service.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

    private final FileService fileService;

    public StorageController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<File> uploadFile(@RequestParam("file") MultipartFile file,
                                          @RequestParam("userId") Long userId) throws Exception {
        File savedFile = fileService.uploadFile(file, userId);
        return ResponseEntity.ok(savedFile);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) throws Exception {
        InputStream fileStream = fileService.downloadFile(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(new InputStreamResource(fileStream));
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileName) throws Exception {
        fileService.deleteFile(fileName);
        return ResponseEntity.ok().build();
    }
}