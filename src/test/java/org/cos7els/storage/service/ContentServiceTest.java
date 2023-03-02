//package org.cos7els.storage.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class ContentServiceTest {
//    @Mock
//    private ContentRepository contentRepository;
//    @InjectMocks
//    private ContentService contentService;
//    private final Content content = new Content(1L, 1L, 1L);
//    private final Long id = 1L;
//
//    @Test
//    public void getContentTest() {
//        when(contentRepository.findById(id)).thenReturn(Optional.of(content));
//        Content returnedContent = contentService.getContent(id).orElse(new Content());
//        verify(contentRepository).findById(id);
//        assertEquals(content, returnedContent);
//    }
//
//    @Test
//    public void getContentsTest() {
//        Iterable<Content> list = List.of(new Content(), new Content(), new Content());
//        when(contentRepository.findAll()).thenReturn(list);
//        Iterable<Content> contents = contentService.getContents();
//        verify(contentRepository).findAll();
//        assertEquals(list, contents);
//    }
//
//    @Test
//    public void createContentTest() {
//        when(contentService.createContent(content)).thenReturn(content);
//        Content createdContent = contentService.createContent(content);
//        verify(contentRepository).save(content);
//        assertEquals(content, createdContent);
//    }
//
//    @Test
//    public void deleteContentTest() {
//        contentService.deleteContent(id);
//        verify(contentRepository).deleteById(id);
//    }
//
//    @Test
//    public void deleteContentsTest() {
//        contentService.deleteContents();
//        verify(contentRepository).deleteAll();
//    }
//}
