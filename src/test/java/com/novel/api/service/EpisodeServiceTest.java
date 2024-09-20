package com.novel.api.service;

import com.novel.api.domain.episode.Episode;
import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.user.User;
import com.novel.api.dto.EpisodeDto;
import com.novel.api.dto.request.episode.EpisodeSearch;
import com.novel.api.dto.request.episode.WriteEpisodeRequest;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.fixture.EpisodeFixture;
import com.novel.api.fixture.NovelFixture;
import com.novel.api.fixture.UserFixture;
import com.novel.api.repository.episode.EpisodeRepository;
import com.novel.api.repository.novel.NovelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.novel.api.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("EpisodeServiceTest")
@TestMethodOrder(MethodOrderer.MethodName.class)
class EpisodeServiceTest {

    @Autowired
    EpisodeService episodeService;

    @MockBean
    EpisodeRepository episodeRepository;
    @MockBean
    NovelRepository novelRepository;

    /**
     * write test
     */
    @Test
    @DisplayName("[write][성공]:")
    void write_success() {
        //given
        User user = UserFixture.get();
        Novel novel = NovelFixture.get();
        Episode episode = EpisodeFixture.get();

        //when
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.of(novel));
        when(novelRepository.save(any())).thenReturn(episode);

        //then
        assertDoesNotThrow(() -> episodeService
                .write(novel.getId(), mock(WriteEpisodeRequest.class), user));
    }

    @Test
    @DisplayName("[write][fail]: NOVEL_NOT_FOUND")
    void write_fail_novel_not_found() {
        //given
        User user = UserFixture.get();
        Novel novel = NovelFixture.get();

        //when
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.empty());

        //then
        var e = assertThrows(NovelApplicationException.class, () -> episodeService
                .write(novel.getId(), mock(WriteEpisodeRequest.class), user));
        assertEquals(e.getErrorCode(), NOVEL_NOT_FOUND);
    }

    @Test
    @DisplayName("[write][fail]: INVALID_PERMISSION")
    void write_fail_invalid_permission() {
        //given
        User user = UserFixture.get();
        Novel novel = NovelFixture.get();
        Episode episode = EpisodeFixture.get();

        //when
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.of(novel));
        when(novelRepository.save(any())).thenReturn(episode);
        ;

        //then
        var e = assertThrows(NovelApplicationException.class, () -> episodeService
                .write(novel.getId(), mock(WriteEpisodeRequest.class), mock(User.class)));
        assertEquals(e.getErrorCode(), INVALID_PERMISSION);
    }

    /**
     * get test
     */
    @Test
    @DisplayName("[get][success]:")
    void get_success() {
        //given
        Episode episode = EpisodeFixture.get();

        //when
        when(episodeRepository.findById(episode.getId())).thenReturn(Optional.of(episode));

        //then
        EpisodeDto episodeDto = assertDoesNotThrow(() -> episodeService.get(episode.getId()));

        assertEquals(episodeDto.getId(), episode.getId());
        assertEquals(episodeDto.getTitle(), episode.getTitle());
        assertEquals(episodeDto.getDetail(), episode.getDetail());
    }

    @Test
    @DisplayName("[get][fail]: EPISODE_NOT_FOUND")
    void get_fail_episode_not_found() {
        //given
        Episode episode = EpisodeFixture.get();

        //when
        when(episodeRepository.findById(episode.getId())).thenReturn(Optional.empty());

        //then
        var e = assertThrows(NovelApplicationException.class, () -> episodeService.get(episode.getId()));
        assertEquals(e.getErrorCode(), EPISODE_NOT_FOUND);
    }

    /**
     * getList test
     */

    @Test
    @DisplayName("[getList][success]:")
    void getList_success() {
        //given
        var episode = EpisodeFixture.get();
        var search = EpisodeSearch.builder().build();

        List<Episode> collect = IntStream.rangeClosed(0, 9)
                .mapToObj(i -> Episode.builder()
                        .id((long) i)
                        .title(episode.getTitle() + i)
                        .detail(episode.getDetail() + i)
                        .build())
                .collect(Collectors.toList());

        PageImpl<Episode> episodes = new PageImpl<>(collect, search.getPageable(), collect.size());

        //when
        when(episodeRepository.getList(episode.getId(), search)).thenReturn(episodes);

        //then
        assertDoesNotThrow(() -> episodeService.getEpisodeList(episode.getId(), search));
    }

}