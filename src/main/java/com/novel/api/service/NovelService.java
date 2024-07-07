package com.novel.api.service;

import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.user.User;
import com.novel.api.dto.NovelDto;
import com.novel.api.dto.request.novel.GetNovelListSearch;
import com.novel.api.dto.request.novel.PostNovelRequest;
import com.novel.api.dto.request.novel.PutNovelRequest;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.dto.response.novel.GetNovelListResponse;
import com.novel.api.repository.UserRepository;
import com.novel.api.repository.novel.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.novel.api.domain.novel.NovelStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class NovelService {

    private final NovelRepository novelRepository;
    private final UserRepository userRepository;

    public void write(PostNovelRequest request, String name) {

        User user = userRepository.findByName(name).orElseThrow(RuntimeException::new);

        novelRepository.findByTitle(request.getTitle()).ifPresent(it -> {
            throw new RuntimeException();
        });

        Novel novel = Novel.builder()
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

        Novel novel = novelRepository.findById(novelId).orElseThrow(RuntimeException::new);

        return NovelDto.from(novel);
    }

    @Transactional(readOnly = true)
    public PageingResponse<GetNovelListResponse> getList(GetNovelListSearch search) {
        Page<Novel> novelPage = novelRepository.getList(search);
        return new PageingResponse<>(novelPage, GetNovelListResponse.class);
    }

    public void edit(Long novelId, PutNovelRequest request, String name) {

        Novel novel = novelRepository.findById(novelId).orElseThrow(RuntimeException::new);

        User user = userRepository.findByName(name).orElseThrow(RuntimeException::new);
        if(!novel.getUser().equals(user)){
            throw new RuntimeException();
        }


        novel.edit(request);
    }

    public void delete(Long novelId, String name) {
        Novel novel = novelRepository.findById(novelId).orElseThrow(RuntimeException::new);

        User user = userRepository.findByName(name).orElseThrow(RuntimeException::new);
        if(!novel.getUser().equals(user)){
            throw new RuntimeException();
        }

        novelRepository.delete(novel);
    }


}
