package com.novel.api.service;

import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.user.User;
import com.novel.api.dto.NovelDto;
import com.novel.api.dto.request.novel.GetNovelListSearch;
import com.novel.api.dto.request.novel.WriteNovelRequest;
import com.novel.api.dto.request.novel.EditNovelRequest;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.dto.response.novel.GetNovelListResponse;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.repository.novel.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.novel.api.domain.novel.NovelStatus.*;
import static com.novel.api.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class NovelService {

    private final NovelRepository novelRepository;

    public void write(WriteNovelRequest request, User user) {

        novelRepository.findByTitle(request.getTitle()).ifPresent(it -> {
            throw new NovelApplicationException(DUPLICATED_NOVEL_NAME);
        });

        var novel = Novel.builder()
                .title(request.getTitle())
                .explanation(request.getExplanation())
                .novelStatus(PROGRESS)
                .genre(request.getGenre())
                .user(user)
                .build();

        novelRepository.save(novel);
    }

    @Transactional(readOnly = true)
    public NovelDto get(Long novelId) {

        Novel novel = getNovelOrNovelNotFoundException(novelId);

        return NovelDto.from(novel);
    }

    @Transactional(readOnly = true)
    public PageingResponse<GetNovelListResponse> getList(GetNovelListSearch search) {
        Page<Novel> novelPage = novelRepository.getList(search);
        return new PageingResponse<>(novelPage, GetNovelListResponse.class);
    }

    public void edit(Long novelId, EditNovelRequest request, User user) {

        Novel novel = getNovelOrNovelNotFoundException(novelId);

        isPermittedUserForNovelOrInvalidPermissionException(user, novel);

        novel.edit(request);
    }

    public void delete(Long novelId, User user) {

        Novel novel = getNovelOrNovelNotFoundException(novelId);

        isPermittedUserForNovelOrInvalidPermissionException(user, novel);

        novelRepository.delete(novel);
    }

    /**
     * 추출된 부분
     */

    private Novel getNovelOrNovelNotFoundException(Long novelId) {
        return novelRepository.findById(novelId).orElseThrow(() ->
                new NovelApplicationException(NOVEL_NOT_FOUND));
    }

    private static void isPermittedUserForNovelOrInvalidPermissionException(User user, Novel novel) {
        if (!novel.getUser().equals(user)) {
            throw new NovelApplicationException(INVALID_PERMISSION);
        }
    }


}
