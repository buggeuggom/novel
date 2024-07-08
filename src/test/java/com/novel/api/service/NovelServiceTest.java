package com.novel.api.service;

import com.novel.api.domain.novel.Genre;
import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.user.User;
import com.novel.api.dto.NovelDto;
import com.novel.api.dto.request.novel.GetNovelListSearch;
import com.novel.api.dto.request.novel.PostNovelRequest;
import com.novel.api.dto.request.novel.PutNovelRequest;
import com.novel.api.dto.response.novel.GetNovelResponse;
import com.novel.api.fixture.NovelFixture;
import com.novel.api.fixture.TestInfoFixture;
import com.novel.api.fixture.UserFixture;
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

        PostNovelRequest request = PostNovelRequest.builder()
                .title(fixture.getTitle())
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(userRepository.findByName(fixture.getUserName()))
                .thenReturn(Optional.of(UserFixture.get(fixture.getUserName(), fixture.getPassword())));
        when(novelRepository.findByTitle(fixture.getTitle()))
                .thenReturn(Optional.empty());
        when(novelRepository.save(any())).thenReturn(mock(Novel.class));

        //then
        assertDoesNotThrow(() -> novelService.write(request, fixture.getUserName()));
    }

    @Test
    @DisplayName("[write][fail]: USER_NOT_FOUND")
    void write_fail_user_not_found() {
        //given
        var fixture = TestInfoFixture.get();

        PostNovelRequest request = PostNovelRequest.builder()
                .title(fixture.getTitle())
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(userRepository.findByName(fixture.getUserName()))
                .thenReturn(Optional.empty());
        when(novelRepository.findByTitle(fixture.getTitle()))
                .thenReturn(Optional.empty());
        when(novelRepository.save(any())).thenReturn(mock(Novel.class));

        //then
        var exception = assertThrows(RuntimeException.class, () -> novelService.write(request, fixture.getUserName()));
    }

    @Test
    @DisplayName("[write][fail]: TITLE_ALREADY_EXIST")
    void write_fail_title_already_exist() {
        //given
        var fixture = TestInfoFixture.get();

        PostNovelRequest request = PostNovelRequest.builder()
                .title(fixture.getTitle())
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(userRepository.findByName(fixture.getUserName()))
                .thenReturn(Optional.of(UserFixture.get(fixture.getUserName(), fixture.getPassword())));
        when(novelRepository.findByTitle(fixture.getTitle()))
                .thenReturn(Optional.of(mock(Novel.class)));
        when(novelRepository.save(any())).thenReturn(mock(Novel.class));

        //then
        var exception = assertThrows(RuntimeException.class, () -> novelService.write(request, fixture.getUserName()));
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
        NovelDto novelDto = assertDoesNotThrow(() -> novelService.get(fixture.getNovelId()));
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
        var exception = assertThrows(RuntimeException.class, () -> novelService.get(fixture.getNovelId()));
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
                        .build()
                ).collect(Collectors.toList());


        PageImpl<Novel> novelPage = new PageImpl<>(novels, search.getPageable(), novels.size());

        //when
        when(novelRepository.getList(search)).thenReturn(novelPage);

        //then
        assertDoesNotThrow(()-> novelService.getList(search));
    }

    /**
     * edit test
     */

    @Test
    @DisplayName("[edit][성공]:")
    void edit_success() {
        //given
        Novel mockNovel = mock(Novel.class);
        User mockUser = mock(User.class);
        var fixture = TestInfoFixture.get();

        var request = PutNovelRequest.builder()
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.of(mockNovel));
        when(userRepository.findByName(fixture.getUserName()))
                .thenReturn(Optional.of(mockUser));
        when(mockNovel.getUser()).thenReturn(mockUser);

        //then
        assertDoesNotThrow(() -> novelService.edit(fixture.getNovelId(), request, fixture.getUserName()));
    }

    @Test
    @DisplayName("[edit][fail]: NOVEL_NOT_FOUND")
    void edit_fail_novel_not_found() {
        //given
        var fixture = TestInfoFixture.get();

        var request = PutNovelRequest.builder()
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.empty());
        when(userRepository.findByName(fixture.getUserName()))
                .thenReturn(Optional.of(UserFixture.get(fixture.getUserName(), fixture.getPassword())));


        //then
        var exception = assertThrows(RuntimeException.class, () -> novelService.edit(fixture.getNovelId(), request, fixture.getUserName()));
    }

    @Test
    @DisplayName("[edit][fail]: USER_NOT_FOUND")
    void edit_fail_user_not_found() {
        //given
        var fixture = TestInfoFixture.get();

        var request = PutNovelRequest.builder()
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.of(mock(Novel.class)));
        when(userRepository.findByName(fixture.getUserName()))
                .thenReturn(Optional.empty());


        //then
        var exception = assertThrows(RuntimeException.class,
                () -> novelService.edit(fixture.getNovelId(), request, fixture.getUserName()));
    }

    @Test
    @DisplayName("[edit][fail]: INVALID_PERMISSION")
    void edit_fail_invalid_permission() {
        //given
        Novel mockNovel = mock(Novel.class);
        User mockUser = mock(User.class);
        var fixture = TestInfoFixture.get();

        var request = PutNovelRequest.builder()
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.of(mockNovel));
        when(userRepository.findByName(fixture.getUserName()))
                .thenReturn(Optional.of(mockUser));
        when(mockNovel.getUser()).thenReturn(mock(User.class));

        //then
        var exception = assertThrows(RuntimeException.class,
                () -> novelService.edit(fixture.getNovelId(), request, fixture.getUserName()));
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
        when(userRepository.findByName(fixture.getUserName()))
                .thenReturn(Optional.of(mockUser));
        when(mockNovel.getUser()).thenReturn(mockUser);

        //then
        assertDoesNotThrow(() -> novelService.delete(fixture.getNovelId(), fixture.getUserName()));
    }

    @Test
    @DisplayName("[delete][fail]: NOVEL_NOT_FOUND")
    void delete_fail_novel_not_found() {
        //given
        var fixture = TestInfoFixture.get();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.empty());
        when(userRepository.findByName(fixture.getUserName()))
                .thenReturn(Optional.of(UserFixture.get(fixture.getUserName(), fixture.getPassword())));


        //then
        var exception = assertThrows(RuntimeException.class,
                () -> novelService.delete(fixture.getNovelId(), fixture.getUserName()));
    }

    @Test
    @DisplayName("[delete][fail]: USER_NOT_FOUND")
    void delete_fail_user_not_found() {
        //given
        var fixture = TestInfoFixture.get();

        var request = PutNovelRequest.builder()
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.of(mock(Novel.class)));
        when(userRepository.findByName(fixture.getUserName()))
                .thenReturn(Optional.empty());


        //then
        var exception = assertThrows(RuntimeException.class,
                () -> novelService.delete(fixture.getNovelId(), fixture.getUserName()));
    }

    @Test
    @DisplayName("[delete][fail]: INVALID_PERMISSION")
    void delete_fail_invalid_permission() {
        //given
        Novel mockNovel = mock(Novel.class);
        User mockUser = mock(User.class);
        var fixture = TestInfoFixture.get();

        var request = PutNovelRequest.builder()
                .explanation(fixture.getExplanation())
                .genre(fixture.getGenre())
                .build();

        //when
        when(novelRepository.findById(fixture.getNovelId()))
                .thenReturn(Optional.of(mockNovel));
        when(userRepository.findByName(fixture.getUserName()))
                .thenReturn(Optional.of(mockUser));
        when(mockNovel.getUser()).thenReturn(mock(User.class));

        //then
        var exception = assertThrows(RuntimeException.class,
                () -> novelService.delete(fixture.getNovelId(), fixture.getUserName()));
    }
}