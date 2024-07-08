package com.novel.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novel.api.domain.user.User;
import com.novel.api.dto.EpisodeDto;
import com.novel.api.dto.request.episode.WriteEpisodeRequest;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.fixture.EpisodeFixture;
import com.novel.api.fixture.TestInfoFixture;
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


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("NovelControllerTest")
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
        var info = TestInfoFixture.get();
        WriteEpisodeRequest mockRequest = mock(WriteEpisodeRequest.class);
        String json = objectMapper.writeValueAsString(mockRequest);

        //expected
        mockMvc.perform(post("/api/v1/novels/{novelId}/episodes", info.getNovelId())
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
        var info = TestInfoFixture.get();
        WriteEpisodeRequest mockRequest = mock(WriteEpisodeRequest.class);
        String json = objectMapper.writeValueAsString(mockRequest);

        //expected
        mockMvc.perform(post("/api/v1/novels/{novelId}/episodes", info.getNovelId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("[get][success]: ")
    void get_success() throws Exception {
        //given
        var info = TestInfoFixture.get();

        //when
        when(episodeService.get(info.getEpisodeId())).thenReturn(EpisodeDto.from(EpisodeFixture.get()));

        //expected
        mockMvc.perform(get("/api/v1/episodes/{episodeId}", info.getEpisodeId())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("[getList][success]: ")
    void getList_success() throws Exception {
        //given
        var info = TestInfoFixture.get();

        //when
        when(episodeService.getList(eq(info.getNovelId()), any())).thenReturn(mock(PageingResponse.class));

        //expected
        mockMvc.perform(get("/api/v1/novels/{novelId}/episodes", info.getNovelId())
                        .contentType(APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}