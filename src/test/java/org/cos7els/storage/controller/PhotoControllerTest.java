package org.cos7els.storage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.cos7els.storage.model.request.SelectedPhotoRequest;
import org.cos7els.storage.model.response.PhotoResponse;
import org.cos7els.storage.model.response.ThumbnailResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.PhotoService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PhotoControllerTest {
    private MockMvc mockMvc;
    private ObjectWriter objectWriter;
    @Mock
    private PhotoService photoService;
    @InjectMocks
    private PhotoController photoController;
    private Long id;
    private UserDetailsImpl userDetails;
    private List<ThumbnailResponse> thumbnailResponses;
    private PhotoResponse photoResponse;
    private SelectedPhotoRequest selectedPhotoRequest;
    private byte[] bytes;

    @BeforeEach
    public void init() {
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        id = 1L;
        userDetails = new UserDetailsImpl(id, null, null, null);
        thumbnailResponses = List.of(new ThumbnailResponse());
        photoResponse = new PhotoResponse();
        selectedPhotoRequest = new SelectedPhotoRequest();
        bytes = new byte[id.intValue()];
        mockMvc = MockMvcBuilders
                .standaloneSetup(photoController)
                .setCustomArgumentResolvers(putUserDetails)
                .build();
    }

    @Test
    public void getThumbnailsTest() throws Exception {
        when(photoService.getThumbnails(userDetails.getId())).thenReturn(thumbnailResponses);
        mockMvc.perform(get("/photos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(photoService).getThumbnails(userDetails.getId());
    }

    @Test
    public void getPhotoTest() throws Exception {
        when(photoService.getPhoto(id, userDetails.getId())).thenReturn(photoResponse);
        mockMvc.perform(get("/photo/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(photoService).getPhoto(id, userDetails.getId());
    }

    @Test
    public void downloadPhotosTest() throws Exception {
        photoResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        photoResponse.setData(bytes);
        photoResponse.setFileName("img.jpg");
        selectedPhotoRequest.setIds(List.of(id, id));
        when(photoService.downloadPhotos(selectedPhotoRequest, userDetails.getId())).thenReturn(bytes);
        mockMvc.perform(post("/photos/download")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(selectedPhotoRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/zip"))
                .andExpect(content().bytes(bytes))
                .andReturn();
        verify(photoService).downloadPhotos(selectedPhotoRequest, userDetails.getId());
        selectedPhotoRequest.setIds(List.of(id));
        when(photoService.getPhoto(selectedPhotoRequest.getIds().get(0), userDetails.getId())).thenReturn(photoResponse);
        mockMvc.perform(post("/photos/download")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(selectedPhotoRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(IMAGE_JPEG))
                .andExpect(content().bytes(bytes))
                .andReturn();
        verify(photoService).getPhoto(selectedPhotoRequest.getIds().get(0), userDetails.getId());
    }

    @Test
    public void downloadPhotoTest() throws Exception {
        photoResponse.setContentType(MediaType.IMAGE_JPEG_VALUE);
        photoResponse.setData(bytes);
        photoResponse.setFileName("img.jpg");
        when(photoService.getPhoto(id, id)).thenReturn(photoResponse);
        mockMvc.perform(get("/photo/{id}/download", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(IMAGE_JPEG))
                .andExpect(content().bytes(bytes))
                .andReturn();
        verify(photoService).getPhoto(id, userDetails.getId());
    }

    @Test
    public void deletePhotosTest() throws Exception {
        selectedPhotoRequest.setIds(List.of(id, id));
        mockMvc.perform(delete("/photos")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(selectedPhotoRequest)))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(photoService).deletePhotos(selectedPhotoRequest, userDetails.getId());
    }

    @Test
    public void deletePhotoTest() throws Exception {
        mockMvc.perform(delete("/photo/{id}/delete", id))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(photoService).deletePhoto(id, userDetails.getId());
    }

    private final HandlerMethodArgumentResolver putUserDetails = new HandlerMethodArgumentResolver() {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterType().isAssignableFrom(UserDetailsImpl.class);
        }

        @Override
        public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            return userDetails;
        }
    };
}