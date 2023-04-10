package org.cos7els.storage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.cos7els.storage.model.domain.Authority;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.AuthorityService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthorityControllerTest {
    private MockMvc mockMvc;
    private ObjectWriter objectWriter;
    @Mock
    private AuthorityService authorityService;
    @InjectMocks
    private AuthorityController authorityController;
    private Long id;
    private Authority authority;
    private List<Authority> authorities;
    @BeforeEach
    public void init() {
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        id = 1L;
        authority = new Authority();
        authorities = List.of(new Authority());
        mockMvc = MockMvcBuilders
                .standaloneSetup(authorityController)
                .setCustomArgumentResolvers(putUserDetails)
                .build();
    }

    @Test
    public void getAuthoritiesTest() throws Exception {
        when(authorityService.getAllAuthorities()).thenReturn(authorities);
        mockMvc.perform(get("/admin/authorities"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(authorityService).getAllAuthorities();
    }

    @Test
    public void getAuthorityTest() throws Exception {
        when(authorityService.getAuthority(id)).thenReturn(authority);
        mockMvc.perform(get("/admin/authority/{id}", id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(authorityService).getAuthority(id);
    }

    @Test
    public void createAuthorityTest() throws Exception {
        when(authorityService.saveAuthority(authority)).thenReturn(authority);
        mockMvc.perform(post("/admin/authority")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(authority)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(authorityService).saveAuthority(authority);
    }

    @Test
    public void updateAuthorityTest() throws Exception {
        when(authorityService.saveAuthority(authority)).thenReturn(authority);
        mockMvc.perform(put("/admin/authority")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(authority)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(authorityService).saveAuthority(authority);
    }

    @Test
    public void deleteAuthorityTest() throws Exception {
        mockMvc.perform(delete("/admin/authority/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(authorityService).deleteAuthority(id);
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