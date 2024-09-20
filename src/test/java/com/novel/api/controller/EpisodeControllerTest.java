package com.novel.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novel.api.domain.episode.Episode;
import com.novel.api.domain.novel.Novel;
import com.novel.api.dto.EpisodeDto;
import com.novel.api.dto.request.episode.WriteEpisodeRequest;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.fixture.EpisodeFixture;
import com.novel.api.fixture.NovelFixture;
import com.novel.api.service.EpisodeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.novel.api.exception.ErrorCode.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("EpisodeControllerTest")
@TestMethodOrder(MethodOrderer.MethodName.class)
class EpisodeControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EpisodeService episodeService;

    /*
    write test
    */
    @Test
    @WithMockUser
    @DisplayName("[write][success]: ")
    void write_success() throws Exception {
        //given
        Novel novel = NovelFixture.get();

        WriteEpisodeRequest mockRequest = mock(WriteEpisodeRequest.class);
        String json = objectMapper.writeValueAsString(mockRequest);

        //expected
        mockMvc.perform(post("/api/v1/novels/{novelId}/episodes", novel.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("[write][fail]: forbidden")
    void write_fail_forbidden() throws Exception {
        //given
        Novel novel = NovelFixture.get();

        WriteEpisodeRequest mockRequest = mock(WriteEpisodeRequest.class);
        String json = objectMapper.writeValueAsString(mockRequest);

        //expected
        mockMvc.perform(post("/api/v1/novels/{novelId}/episodes", novel.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("[write][fail]: NOVEL_NOT_FOUND")
    void write_fail_novel_not_found() throws Exception {
        //given
        Novel novel = NovelFixture.get();

        WriteEpisodeRequest mockRequest = mock(WriteEpisodeRequest.class);
        String json = objectMapper.writeValueAsString(mockRequest);

        doThrow(new NovelApplicationException(NOVEL_NOT_FOUND)).when(episodeService).write(eq(novel.getId()), any(), any());
        //expected
        mockMvc.perform(post("/api/v1/novels/{novelId}/episodes", novel.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(NOVEL_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("[write][fail]: INVALID_PERMISSION")
    void write_fail_invalid_permission() throws Exception {
        //given
        Novel novel = NovelFixture.get();
        
        WriteEpisodeRequest mockRequest = mock(WriteEpisodeRequest.class);
        String json = objectMapper.writeValueAsString(mockRequest);

        doThrow(new NovelApplicationException(INVALID_PERMISSION)).when(episodeService).write(eq(novel.getId()), any(), any());
        //expected
        mockMvc.perform(post("/api/v1/novels/{novelId}/episodes", novel.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is(INVALID_PERMISSION.getStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("[get][success]: ")
    void get_success() throws Exception {
        //given
        Episode episode = EpisodeFixture.get();
        
        //when
        when(episodeService.get(episode.getId())).thenReturn(EpisodeDto.from(episode));

        //expected
        mockMvc.perform(get("/api/v1/episodes/{episodeId}", episode.getId())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("[get][fail]: EPISODE_NOT_FOUND")
    void get_fail_episode_not_found() throws Exception {
        //given
        Episode episode = EpisodeFixture.get();

        //when
        doThrow(new NovelApplicationException(EPISODE_NOT_FOUND)).when(episodeService).get(eq(episode.getId()));

        //expected
        mockMvc.perform(get("/api/v1/episodes/{episodeId}", episode.getId())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(EPISODE_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithMockUser
    @DisplayName("[getList][success]: ")
    void getList_success() throws Exception {
        //given
        Novel novel = NovelFixture.get();

        //when
        when(episodeService.getEpisodeList(eq(novel.getId()), any())).thenReturn(mock(PageingResponse.class));

        //expected
        mockMvc.perform(get("/api/v1/novels/{novelId}/episodes", novel.getId())
                        .contentType(APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}