package org.cos7els.storage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.cos7els.storage.model.request.CreateUpdateAlbumRequest;
import org.cos7els.storage.model.response.AlbumResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.AlbumService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AlbumControllerTest {
    private MockMvc mockMvc;
    private ObjectWriter objectWriter;
    @Mock
    private AlbumService albumService;
    @InjectMocks
    private AlbumController albumController;
    private Long id;
    private byte[] bytes;
    private AlbumResponse albumResponse;
    private List<AlbumResponse> albumResponses;
    private CreateUpdateAlbumRequest createUpdateAlbumRequest;

    @BeforeEach
    public void init() {
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        id = 1L;
        albumResponse = new AlbumResponse();
        albumResponses = List.of(albumResponse);
        bytes = new byte[id.intValue()];
        createUpdateAlbumRequest = new CreateUpdateAlbumRequest("test", List.of(id, id, id));
        mockMvc = MockMvcBuilders
                .standaloneSetup(albumController)
                .setCustomArgumentResolvers(putUserDetails)
                .build();
    }

    @Test
    public void getAlbumsTest() throws Exception {
        when(albumService.getAlbums(id)).thenReturn(albumResponses);
        mockMvc.perform(get("/albums"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(albumService).getAlbums(id);
    }

    @Test
    public void getAlbumTest() throws Exception {
        when(albumService.getAlbum(id, id)).thenReturn(albumResponse);
        mockMvc.perform(get("/album/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(albumService).getAlbum(id, id);
    }

    @Test
    public void downloadAlbumTest() throws Exception {
        when(albumService.downloadAlbum(id, id)).thenReturn(bytes);
        mockMvc.perform(get("/album/{id}/download", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/zip"))
                .andExpect(content().bytes(bytes))
                .andReturn();
        verify(albumService).downloadAlbum(id, id);
    }

    @Test
    public void createAlbumTest() throws Exception {
        when(albumService.createAlbum(createUpdateAlbumRequest, id)).thenReturn(albumResponse);
        mockMvc.perform(post("/album")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(createUpdateAlbumRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(albumService).createAlbum(createUpdateAlbumRequest, id);
    }

    @Test
    public void updateAlbumTest() throws Exception {
        when(albumService.updateAlbum(id, createUpdateAlbumRequest, id)).thenReturn(albumResponse);
        mockMvc.perform(put("/album/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(createUpdateAlbumRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(albumService).updateAlbum(id, createUpdateAlbumRequest, id);
    }

    @Test
    public void deleteAlbumTest() throws Exception {
        mockMvc.perform(delete("/album/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(albumService).deleteAlbum(id, id);
    }

    private final HandlerMethodArgumentResolver putUserDetails = new HandlerMethodArgumentResolver() {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterType().isAssignableFrom(UserDetailsImpl.class);
        }

        @Override
        public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            return new UserDetailsImpl(1L, null, null, null);
        }
    };
}