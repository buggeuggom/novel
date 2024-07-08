package com.novel.api.repository.episode;

import com.novel.api.domain.episode.Episode;
import com.novel.api.dto.request.episode.EpisodeSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.novel.api.domain.episode.QEpisode.*;

@RequiredArgsConstructor
public class EpisodeRepositoryImpl implements EpisodeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Episode> getList(Long novelId, EpisodeSearch search) {

        long totalCount = jpaQueryFactory.select(episode.count())
                .from(episode)
                .fetchFirst();

        List<Episode> episodes = jpaQueryFactory.selectFrom(episode)
                .where(episode.novel.id.eq(novelId))
                .limit(search.getSize())
                .offset(search.getOffset())
                .orderBy(episode.id.desc())
                .fetch();
        return new PageImpl<>(episodes, search.getPageable(), totalCount);
    }
}
