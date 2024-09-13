package com.novel.api.service;

import com.novel.api.domain.novel.Genre;
import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.user.User;
import com.novel.api.dto.request.novel.GetNovelListSearch;
import com.novel.api.dto.request.novel.WriteNovelRequest;
import com.novel.api.dto.request.novel.EditNovelRequest;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.fixture.NovelFixture;
import com.novel.api.fixture.TestInfoFixture;
import com.novel.api.repository.UserRepository;
import com.novel.api.repository.novel.NovelRepository;
import org.junit.jupiter.api.*;
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
@DisplayName("NovelServiceTest")
@TestMethodOrder(MethodOrderer.MethodName.class)
class NovelServiceTest {

    @Autowired
    NovelService novelService;

    @MockBean
    UserRepository userRepository;
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

        var request = WriteNovelRequest.builder()
                .title(fixture.getTitle())
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(novelRepository.findByTitle(fixture.getTitle())).thenReturn(Optional.empty());
        when(novelRepository.save(any())).thenReturn(mock(Novel.class));

        //then
        assertDoesNotThrow(() -> novelService.write(request, mock(User.class)));
    }

    @Test
    @DisplayName("[write][fail]: DUPLICATED_NOVEL_NAME")
    void write_fail_duplicated_novel_name() {
        //given
        var fixture = TestInfoFixture.get();

        WriteNovelRequest request = WriteNovelRequest.builder()
                .title(fixture.getTitle())
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(novelRepository.findByTitle(fixture.getTitle()))
                .thenReturn(Optional.of(mock(Novel.class)));
        when(novelRepository.save(any())).thenReturn(mock(Novel.class));

        //then
        var exception = assertThrows(NovelApplicationException.class, () -> novelService.write(request, mock(User.class)));
        assertEquals(exception.getErrorCode(), DUPLICATED_NOVEL_NAME);
    }

    /**
     * get test
     */
    @Test
    @DisplayName("[get][성공]:")
    void get_success() {
        //given
        var fixture = TestInfoFixture.get();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.of(NovelFixture.get(mock(User.class))));

        //then
        assertDoesNotThrow(() -> novelService.get(fixture.getNovelId()));
    }

    @Test
    @DisplayName("[get][fail]: NOVEL_NOT_FOUND")
    void get_fail_novel_not_found() {
        //given
        var fixture = TestInfoFixture.get();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.empty());

        //then
        var exception = assertThrows(NovelApplicationException.class, () -> novelService.get(fixture.getNovelId()));
        assertEquals(exception.getErrorCode(), NOVEL_NOT_FOUND);
    }

    /**
     * getList
     */
    @Test
    @DisplayName("[getList][success]: ")
    void getList_success() {
        //given

        User mockUser = mock(User.class);

        var search = GetNovelListSearch.builder()
                .title("test")
                .author(mockUser.getName())
                .build();

        List<Novel> novels = IntStream.rangeClosed(0, 9)
                .mapToObj(i -> Novel.builder()
                        .id((long) i)
                        .title("test title: " + i)
                        .explanation("test explanation: " + i)
                        .genre(Genre.FANTASY)
                        .user(mockUser)
                        .build())
                .collect(Collectors.toList());


        PageImpl<Novel> novelPage = new PageImpl<>(novels, search.getPageable(), novels.size());

        //when
        when(novelRepository.getList(search)).thenReturn(novelPage);

        //then
        assertDoesNotThrow(() -> novelService.getNovelList(search));
    }

    /**
     * edit test
     */

    @Test
    @DisplayName("[edit][성공]:")
    void edit_success() {
        //given
        var fixture = TestInfoFixture.get();

        Novel mockNovel = mock(Novel.class);
        User mockUser = mock(User.class);

        var request = EditNovelRequest.builder()
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.of(mockNovel));
        when(mockNovel.getUser()).thenReturn(mockUser);

        //then
        assertDoesNotThrow(() -> novelService.edit(fixture.getNovelId(), request, mockUser));
    }

    @Test
    @DisplayName("[edit][fail]: NOVEL_NOT_FOUND")
    void edit_fail_novel_not_found() {
        //given
        var fixture = TestInfoFixture.get();

        var request = EditNovelRequest.builder()
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        User mockUser = mock(User.class);
        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.empty());


        //then
        var exception = assertThrows(NovelApplicationException.class,
                () -> novelService.edit(fixture.getNovelId(), request, mockUser));

        assertEquals(exception.getErrorCode(), NOVEL_NOT_FOUND);
    }

    @Test
    @DisplayName("[edit][fail]: INVALID_PERMISSION")
    void edit_fail_invalid_permission() {
        //given
        Novel mockNovel = mock(Novel.class);
        User mockUser = mock(User.class);
        var fixture = TestInfoFixture.get();

        var request = EditNovelRequest.builder()
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.of(mockNovel));
        when(mockNovel.getUser()).thenReturn(mock(User.class));

        //then
        var exception = assertThrows(NovelApplicationException.class,
                () -> novelService.edit(fixture.getNovelId(), request, mockUser));

        assertEquals(exception.getErrorCode(), INVALID_PERMISSION);
    }

    /**
     * delete test
     */

    @Test
    @DisplayName("[delete][성공]:")
    void delete_success() {
        //given
        Novel mockNovel = mock(Novel.class);
        User mockUser = mock(User.class);

        var fixture = TestInfoFixture.get();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.of(mockNovel));
        when(mockNovel.getUser()).thenReturn(mockUser);

        //then
        assertDoesNotThrow(() -> novelService.delete(fixture.getNovelId(), mockUser));
    }

    @Test
    @DisplayName("[delete][fail]: NOVEL_NOT_FOUND")
    void delete_fail_novel_not_found() {
        //given
        var fixture = TestInfoFixture.get();

        User mockUser = mock(User.class);

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.empty());

        //then
        var exception = assertThrows(NovelApplicationException.class,
                () -> novelService.delete(fixture.getNovelId(), mockUser));
        assertEquals(exception.getErrorCode(), NOVEL_NOT_FOUND);
    }

    @Test
    @DisplayName("[delete][fail]: INVALID_PERMISSION")
    void delete_fail_invalid_permission() {
        //given
        Novel mockNovel = mock(Novel.class);
        User mockUser = mock(User.class);
        var fixture = TestInfoFixture.get();

        var request = EditNovelRequest.builder()
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.of(mockNovel));
        when(mockNovel.getUser()).thenReturn(mock(User.class));

        //then
        var exception = assertThrows(NovelApplicationException.class,
                () -> novelService.delete(fixture.getNovelId(), mockUser));
        assertEquals(exception.getErrorCode(), INVALID_PERMISSION);
    }
}