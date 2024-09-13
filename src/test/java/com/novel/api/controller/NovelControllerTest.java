package com.novel.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novel.api.dto.NovelDto;
import com.novel.api.dto.request.novel.WriteNovelRequest;
import com.novel.api.dto.request.novel.EditNovelRequest;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.fixture.NovelFixture;
import com.novel.api.fixture.TestInfoFixture;
import com.novel.api.fixture.UserFixture;
import com.novel.api.service.NovelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static com.novel.api.domain.novel.Genre.*;
import static com.novel.api.domain.novel.NovelStatus.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("NovelControllerTest")
@TestMethodOrder(MethodOrderer.MethodName.class)
class NovelControllerTest {


    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    NovelService novelService;


    /*
    get test
     */
    @Test
    @WithMockUser
    @DisplayName("[get][success]: ")
    void get_success() throws Exception {
        //given
        var info = TestInfoFixture.get();
        NovelDto novelDto = NovelDto.from(NovelFixture.get(UserFixture.get()));
        //when
        when(novelService.get(info.getNovelId())).thenReturn(novelDto);

        //expected
        mockMvc.perform(get("/api/v1/novels/{novelId}", info.getNovelId())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(novelDto.getTitle()))
                .andExpect(jsonPath("$.explanation").value(novelDto.getExplanation()))
                .andExpect(jsonPath("$.novelStatus").value(String.valueOf(novelDto.getNovelStatus())))
                .andExpect(jsonPath("$.genre").value(String.valueOf(novelDto.getGenre())))
                .andExpect(jsonPath("$.authorName").value(novelDto.getUserDto().getName()));
    }

    @Test
    @WithAnonymousUser
    @DisplayName("[get][fail]: forbidden <- 로그인 안된 유저")
    void get_fail_forbidden() throws Exception {
        //given
        var info = TestInfoFixture.get();

        //when
        NovelDto novelDto = NovelDto.from(NovelFixture.get(UserFixture.get()));
        when(novelService.get(info.getNovelId())).thenReturn(novelDto);

        //expected
        mockMvc.perform(get("/api/v1/novels/{novelId}", info.getNovelId())
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isForbidden());
    }

    /*
    write test
     */
    @Test
    @WithMockUser
    @DisplayName("[write][success]: ")
    void write_success() throws Exception {
        //given
        WriteNovelRequest request = WriteNovelRequest.builder()
                .title("title")
                .explanation("explanation")
                .genre(FANTASY)
                .build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/api/v1/novels")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("[write][fail]: forbidden <- 로그인 안된 유저")
    void write_fail_forbidden() throws Exception {
        //given
        var request = WriteNovelRequest.builder()
                .title("title")
                .explanation("explanation")
                .genre(FANTASY)
                .build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/api/v1/novels")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("[edit][fail]: forbidden <- 로그인 안된 유저")
    void edit_fail_forbidden() throws Exception {
        //given
        var info = TestInfoFixture.get();

        var request = EditNovelRequest.builder()
                .novelStatus(COMPLETED)
                .explanation("explanation")
                .genre(FANTASY)
                .build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(put("/api/v1/novels/{novelId}", info.getNovelId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("[delete][fail]: forbidden <- 로그인 안된 유저")
    void delete_fail_forbidden() throws Exception {
        //given
        var info = TestInfoFixture.get();

        //expected
        mockMvc.perform(delete("/api/v1/novels/{novelId}", info.getNovelId())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    @DisplayName("[getList][success]: ")
    void getList_success() throws Exception {

        //when
        when(novelService.getNovelList(any())).thenReturn(mock(PageingResponse.class));

        //expected
        mockMvc.perform(get("/api/v1/novels")
                        .param("page", "1")
                        .param("size", "10")

                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("[getList][fail]: forbidden <- 로그인 안된 유저")
    void getList_fail_forbidden() throws Exception {
        //when
        when(novelService.getNovelList(any())).thenReturn(mock(PageingResponse.class));

        //expected
        mockMvc.perform(get("/api/v1/novels")
                        .param("page", "1")
                        .param("size", "10")

                ).andDo(print())
                .andExpect(status().isForbidden());
    }
}