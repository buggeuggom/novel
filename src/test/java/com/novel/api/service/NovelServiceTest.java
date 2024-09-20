package com.novel.api.service;

import com.novel.api.domain.novel.Genre;
import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.user.User;
import com.novel.api.dto.NovelDto;
import com.novel.api.dto.request.novel.GetNovelListSearch;
import com.novel.api.dto.request.novel.WriteNovelRequest;
import com.novel.api.dto.request.novel.EditNovelRequest;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.dto.response.novel.GetNovelListResponse;
import com.novel.api.exception.NovelApplicationException;
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
        User userFixture = UserFixture.get();
        Novel novelFixture = NovelFixture.get(userFixture);

        var request = WriteNovelRequest.builder()
                .title(novelFixture.getTitle())
                .explanation(novelFixture.getExplanation())
                .genre(novelFixture.getGenre())
                .build();

        //when
        when(novelRepository.findByTitle(request.getTitle())).thenReturn(Optional.empty());
        when(novelRepository.save(any())).thenReturn(novelFixture);

        //then
        assertDoesNotThrow(() -> novelService.write(request, userFixture));
    }

    @Test
    @DisplayName("[write][fail]: DUPLICATED_NOVEL_NAME")
    void write_fail_duplicated_novel_name() {
        //given
        User userFixture = UserFixture.get();
        Novel novelFixture = NovelFixture.get(userFixture);

        var request = WriteNovelRequest.builder()
                .title(novelFixture.getTitle())
                .explanation(novelFixture.getExplanation())
                .genre(novelFixture.getGenre())
                .build();

        //when
        when(novelRepository.findByTitle(request.getTitle())).thenReturn(Optional.of(novelFixture));

        //then
        var exception = assertThrows(NovelApplicationException.class, () -> novelService.write(request, userFixture));
        assertEquals(exception.getErrorCode(), DUPLICATED_NOVEL_NAME);
    }

    /**
     * get test
     */
    @Test
    @DisplayName("[get][성공]:")
    void get_success() {
        //given
        User userFixture = UserFixture.get();
        Novel novelFixture = NovelFixture.get(userFixture);

        //when
        when(novelRepository.findById(novelFixture.getId()))
                .thenReturn(Optional.of(novelFixture));

        //then
        NovelDto result = assertDoesNotThrow(() -> novelService.get(novelFixture.getId()));
        assertEquals(novelFixture.getTitle(), result.getTitle());
        assertEquals(novelFixture.getNovelStatus(), result.getNovelStatus());
    }

    @Test
    @DisplayName("[get][fail]: NOVEL_NOT_FOUND")
    void get_fail_novel_not_found() {
        //given
        User userFixture = UserFixture.get();
        Novel novelFixture = NovelFixture.get(userFixture);

        //when
        when(novelRepository.findById(novelFixture.getId()))
                .thenReturn(Optional.empty());

        //then
        var exception = assertThrows(NovelApplicationException.class, () -> novelService.get(novelFixture.getId()));
        assertEquals(exception.getErrorCode(), NOVEL_NOT_FOUND);
    }

    /**
     * getList
     */
    @Test
    @DisplayName("[getList][success]:")
    void getList_success() {
        //given
        User mockUser = mock(User.class);

        var search = GetNovelListSearch.builder()
                .title("test")
                .author(mockUser.getName())
                .size(10)
                .page(1)
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
        PageingResponse<GetNovelListResponse> result = assertDoesNotThrow(() -> novelService.getNovelList(search));
        assertEquals(result.getTotalCount(), novels.size());
        assertEquals(result.getItems().getFirst().getTitle(), novels.getFirst().getTitle());
    }

    /**
     * edit test
     */

    @Test
    @DisplayName("[edit][성공]:")
    void edit_success() {
        //given
        User userFixture = UserFixture.get();
        Novel novelFixture = NovelFixture.get(userFixture);

        var request = EditNovelRequest.builder()
                .explanation("changed")
                .genre(Genre.ROMANCE)
                .build();

        //when
        when(novelRepository.findById(novelFixture.getId()))
                .thenReturn(Optional.of(novelFixture));

        //then
        assertDoesNotThrow(() -> novelService.edit(novelFixture.getId(), request, userFixture));
    }

    @Test
    @DisplayName("[edit][fail]: NOVEL_NOT_FOUND")
    void edit_fail_novel_not_found() {
        //given
        User userFixture = UserFixture.get();
        Novel novelFixture = NovelFixture.get(userFixture);

        var request = EditNovelRequest.builder()
                .explanation("changed")
                .genre(Genre.ROMANCE)
                .build();

        //when
        when(novelRepository.findById(novelFixture.getId()))
                .thenReturn(Optional.empty());


        //then
        var exception = assertThrows(NovelApplicationException.class,
                () -> novelService.edit(novelFixture.getId(), request, userFixture));

        assertEquals(exception.getErrorCode(), NOVEL_NOT_FOUND);
    }

    @Test
    @DisplayName("[edit][fail]: INVALID_PERMISSION")
    void edit_fail_invalid_permission() {
        //given
        User userFixture = UserFixture.get();
        Novel novelFixture = NovelFixture.get(userFixture);

        var request = EditNovelRequest.builder()
                .explanation("changed")
                .genre(Genre.ROMANCE)
                .build();

        //when
        when(novelRepository.findById(novelFixture.getId()))
                .thenReturn(Optional.of(novelFixture));


        //then
        var exception = assertThrows(NovelApplicationException.class,
                () -> novelService.edit(novelFixture.getId(), request, mock(User.class)));

        assertEquals(exception.getErrorCode(), INVALID_PERMISSION);
    }

    /**
     * delete test
     */

    @Test
    @DisplayName("[delete][성공]:")
    void delete_success() {
        //given
        User userFixture = UserFixture.get();
        Novel novelFixture = NovelFixture.get(userFixture);

        //when
        when(novelRepository.findById(novelFixture.getId()))
                .thenReturn(Optional.of(novelFixture));

        //then
        assertDoesNotThrow(() -> novelService.delete(novelFixture.getId(), userFixture));
    }

    @Test
    @DisplayName("[delete][fail]: NOVEL_NOT_FOUND")
    void delete_fail_novel_not_found() {
        //given
        User userFixture = UserFixture.get();
        Novel novelFixture = NovelFixture.get(userFixture);

        //when
        when(novelRepository.findById(novelFixture.getId()))
                .thenReturn(Optional.empty());

        //then
        var exception = assertThrows(NovelApplicationException.class,
                () -> novelService.delete(novelFixture.getId(), userFixture));

        assertEquals(exception.getErrorCode(), NOVEL_NOT_FOUND);
    }
    @Test
    @DisplayName("[delete][fail]: INVALID_PERMISSION")
    void delete_fail_invalid_permission() {
        //given
        User userFixture = UserFixture.get();
        Novel novelFixture = NovelFixture.get(userFixture);

        //when
        when(novelRepository.findById(novelFixture.getId()))
                .thenReturn(Optional.of(novelFixture));

        //then
        var exception = assertThrows(NovelApplicationException.class,
                () -> novelService.delete(novelFixture.getId(), mock(User.class)));

        assertEquals(exception.getErrorCode(), INVALID_PERMISSION);
    }
}