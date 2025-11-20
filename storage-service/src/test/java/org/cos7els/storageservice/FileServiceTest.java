package org.cos7els.storageservice;

import org.cos7els.storageservice.model.File;
import org.cos7els.storageservice.repository.FileRepository;
import org.cos7els.storageservice.service.FileService;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    @Test
    void testUploadFile() throws Exception {
        // Подготовка
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", 
                "test.jpg", 
                "image/jpeg", 
                "test image content".getBytes()
        );
        
        org.cos7els.storageservice.model.File expectedFile = org.cos7els.storageservice.model.File.builder()
                .id(1L)
                .originalName("test.jpg")
                .filePath("some-uuid_test.jpg")
                .fileSize(18L)
                .contentType("image/jpeg")
                .uploadedBy(1L)
                .build();

        when(fileRepository.save(any(org.cos7els.storageservice.model.File.class)))
                .thenReturn(expectedFile);

        // Выполнение
        File result = fileService.uploadFile(multipartFile, 1L);

        // Проверка
        assertNotNull(result);
        assertEquals(expectedFile.getOriginalName(), result.getOriginalName());
        assertEquals(expectedFile.getFilePath(), result.getFilePath());
        verify(fileRepository, times(1)).save(any(org.cos7els.storageservice.model.File.class));
    }
}