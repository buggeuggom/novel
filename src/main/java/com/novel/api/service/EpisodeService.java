package com.novel.api.service;

import com.novel.api.domain.episode.Episode;
import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.user.User;
import com.novel.api.dto.EpisodeDto;
import com.novel.api.dto.request.episode.EpisodeSearch;
import com.novel.api.dto.request.episode.WriteEpisodeRequest;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.dto.response.episode.EpisodeListResponse;
import com.novel.api.repository.episode.EpisodeRepository;
import com.novel.api.repository.novel.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EpisodeService {

    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;

    public void write(Long novelId, WriteEpisodeRequest request, User user) {
        Novel novel = novelRepository.findById(novelId).orElseThrow(RuntimeException::new);

        if(!novel.getUser().equals(user)){
            throw new RuntimeException();
        }

        Episode episode = Episode.builder()
                .title(request.getTitle())
                .detail(request.getContent())
                .novel(novel)
                .build();

        episodeRepository.save(episode);
    }

    public EpisodeDto get(Long episodeId) {
        Episode episode = episodeRepository.findById(episodeId).orElseThrow(RuntimeException::new);

        return EpisodeDto.from(episode);
    }

    public PageingResponse<EpisodeListResponse> getList(Long novelId, EpisodeSearch search) {
        Page<Episode> list = episodeRepository.getList(novelId, search);

        return new PageingResponse<>(list, EpisodeListResponse.class);
    }
}
