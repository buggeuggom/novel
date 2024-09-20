package com.novel.api.repository.novel;

import com.novel.api.domain.novel.Genre;
import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.subscription.QSubscription;
import com.novel.api.domain.user.User;
import com.novel.api.dto.request.novel.GetNovelListSearch;
import com.novel.api.dto.request.novel.GetSubscribeNovelListSearch;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.novel.api.domain.novel.QNovel.*;
import static com.novel.api.domain.subscription.QSubscription.*;

@RequiredArgsConstructor
public class NovelRepositoryImpl implements NovelRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<Novel> getList(GetNovelListSearch search) {

        long totalCount = jpaQueryFactory.select(novel.count())
                .from(novel)
                .where(titleStartWith(search.getTitle()), novelAuthorStartWith(search.getAuthor()), isNovelGenre(search.getGenre()))
                .fetchFirst();

        List<Novel> novels = jpaQueryFactory.selectFrom(novel)
                .where(titleStartWith(search.getTitle()), novelAuthorStartWith(search.getAuthor()), isNovelGenre(search.getGenre()))
                .limit(search.getSize())
                .offset(search.getOffset())
                .orderBy(novel.id.desc())
                .fetch();

        return new PageImpl<>(novels, search.getPageable(), totalCount);
    }

    @Override
    public Page<Novel> getSubscribeList(GetSubscribeNovelListSearch search, User user) {
        long totalCount = jpaQueryFactory.select(novel.count())
                .from(subscription)
                .where(subscription.user.eq(user))
                .fetchFirst();

        List<Novel> novels = jpaQueryFactory.select(novel).from(subscription)
                .where(subscription.user.eq(user))
                .limit(search.getSize())
                .offset(search.getOffset())
                .orderBy(novel.id.desc())
                .fetch();
        return new PageImpl<>(novels, search.getPageable(), totalCount);
    }

    private static BooleanExpression novelAuthorStartWith(String author) {
        if (author == null) return null;

        return novel.user.name.startsWith(author);
    }


    private static BooleanExpression titleStartWith(String title) {
        if (title == null) return null;

        return novel.title.startsWith(title);
    }

    private static BooleanExpression isNovelGenre(Genre genre) {
        if (genre == null) return null;

        return novel.genre.eq(genre);
    }


}
