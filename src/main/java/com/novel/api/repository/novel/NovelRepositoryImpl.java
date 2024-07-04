package com.novel.api.repository.novel;

import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.novel.QNovel;
import com.novel.api.dto.request.novel.GetNovelListSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.novel.api.domain.novel.QNovel.*;

@RequiredArgsConstructor
public class NovelRepositoryImpl implements NovelRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<Novel> getList(GetNovelListSearch search) {

        long totalCount = jpaQueryFactory.select(novel.count())
                .from(novel)
                .fetchFirst();

        List<Novel> novels = jpaQueryFactory.selectFrom(novel)
                .where(novel.title.eq(search.getTitle()), novel.user.name.eq(search.getAuthor()), novel.genre.eq(search.getGenre()))
                .limit(search.getSize())
                .offset(search.getOffset())
                .orderBy(novel.id.desc())
                .fetch();

        return new PageImpl<>(novels, search.getPageable(), totalCount);
    }
}
