package com.novel.api.repository.episode;

import com.novel.api.domain.episode.Episode;
import com.novel.api.dto.request.episode.EpisodeSearch;
import org.springframework.data.domain.Page;

public interface EpisodeRepositoryCustom {

    Page<Episode> getList(Long novelId, EpisodeSearch search);

}
