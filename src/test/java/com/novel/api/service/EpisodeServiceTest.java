package com.novel.api.service;

import com.novel.api.domain.episode.Episode;
import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.user.User;
import com.novel.api.dto.EpisodeDto;
import com.novel.api.dto.request.episode.EpisodeSearch;
import com.novel.api.dto.request.episode.WriteEpisodeRequest;
import com.novel.api.fixture.EpisodeFixture;
import com.novel.api.fixture.TestInfoFixture;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
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
        var fixture = TestInfoFixture.get();

        Novel mockNovel = mock(Novel.class);
        User mockUser = mock(User.class);

        //when
        when(novelRepository.findById(fixture.getNovelId())).thenReturn(Optional.of(mockNovel));
        when(mockNovel.getUser()).thenReturn(mockUser);
        when(novelRepository.save(any())).thenReturn(mock(Episode.class));

        //then
        assertDoesNotThrow(() -> episodeService
                .write(fixture.getNovelId(), mock(WriteEpisodeRequest.class), mockUser));
    }

    @Test
    @DisplayName("[write][fail]: NOVEL_NOT_FOUND")
    void write_fail_novel_not_found() {
        //given
        var fixture = TestInfoFixture.get();

        Novel mockNovel = mock(Novel.class);
        User mockUser = mock(User.class);

        //when
        when(novelRepository.findById(fixture.getNovelId())).thenReturn(Optional.empty());
        when(mockNovel.getUser()).thenReturn(mockUser);
        when(novelRepository.save(any())).thenReturn(mock(Episode.class));

        //then
        assertThrows(RuntimeException.class, () -> episodeService
                .write(fixture.getNovelId(), mock(WriteEpisodeRequest.class), mockUser));
    }

    @Test
    @DisplayName("[write][fail]: INVALID_PERMISSION")
    void write_fail_invalid_permission() {
        //given
        var fixture = TestInfoFixture.get();

        Novel mockNovel = mock(Novel.class);
        User mockUser = mock(User.class);

        //when
        when(novelRepository.findById(fixture.getNovelId())).thenReturn(Optional.of(mockNovel));
        when(mockNovel.getUser()).thenReturn(mock(User.class));
        when(novelRepository.save(any())).thenReturn(mock(Episode.class));

        //then
        assertThrows(RuntimeException.class, () -> episodeService
                .write(fixture.getNovelId(), mock(WriteEpisodeRequest.class), mockUser));
    }

    /**
     * get test
     */
    @Test
    @DisplayName("[get][success]:")
    void get_success() {
        //given
        var fixture = TestInfoFixture.get();
        Episode episode = EpisodeFixture.get();

        //when
        when(episodeRepository.findById(fixture.getEpisodeId())).thenReturn(Optional.of(episode));

        //then
        EpisodeDto episodeDto = assertDoesNotThrow(() -> episodeService.get(fixture.getEpisodeId()));

        assertEquals(episodeDto.getId(), episode.getId());
        assertEquals(episodeDto.getTitle(), episode.getTitle());
        assertEquals(episodeDto.getDetail(), episode.getDetail());
    }

    @Test
    @DisplayName("[get][fail]: EPISODE_NOT_FOUND")
    void get_fail_episode_not_found() {
        //given
        var fixture = TestInfoFixture.get();

        //when
        when(episodeRepository.findById(fixture.getEpisodeId())).thenReturn(Optional.empty());

        //then
        assertThrows(RuntimeException.class, () -> episodeService.get(fixture.getEpisodeId()));
    }

    /**
     * getList test
     */

    @Test
    @DisplayName("[getList][success]:")
    void getList_success() {
        //given
        var info = TestInfoFixture.get();
        var search = EpisodeSearch.builder().build();

        List<Episode> collect = IntStream.rangeClosed(0, 9)
                .mapToObj(i -> Episode.builder()
                        .id((long) i)
                        .title(info.getTitle() + i)
                        .detail(info.getEpisodeDetail() + i)
                        .build())
                .collect(Collectors.toList());

        PageImpl<Episode> episodes = new PageImpl<>(collect, search.getPageable(), collect.size());

        //when
        when(episodeRepository.getList(info.getEpisodeId(), search)).thenReturn(episodes);

        //then
        assertDoesNotThrow(()-> episodeService.getList(info.getEpisodeId(), search));

    }

}