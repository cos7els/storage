package org.cos7els.storageservice.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.cos7els.storageservice.model.File;
import org.cos7els.storageservice.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileService {

    private final MinioClient minioClient;
    private final FileRepository fileRepository;

    @Value("${minio.bucket.photo}")
    private String bucketName;

    public FileService(MinioClient minioClient, FileRepository fileRepository) {
        this.minioClient = minioClient;
        this.fileRepository = fileRepository;
    }

    public File uploadFile(MultipartFile multipartFile, Long uploadedBy) throws Exception {
        // Генерируем уникальное имя файла
        String fileName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

        // Загружаем файл в MinIO
        try (InputStream inputStream = multipartFile.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, multipartFile.getSize(), -1)
                            .contentType(multipartFile.getContentType())
                            .build()
            );
        }

        // Сохраняем информацию о файле в базе данных
        File file = File.builder()
                .originalName(multipartFile.getOriginalFilename())
                .filePath(fileName)
                .fileSize(multipartFile.getSize())
                .contentType(multipartFile.getContentType())
                .uploadedBy(uploadedBy)
                .build();

        return fileRepository.save(file);
    }

    public InputStream downloadFile(String fileName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );
    }

    public void deleteFile(String fileName) throws Exception {
        // Удаляем файл из MinIO
        minioClient.removeObject(
                io.minio.RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );

        // Удаляем запись из базы данных
        fileRepository.findByFilePath(fileName).ifPresent(fileRepository::delete);
    }
}