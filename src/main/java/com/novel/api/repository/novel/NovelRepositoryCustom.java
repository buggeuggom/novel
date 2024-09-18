package com.novel.api.repository.novel;

import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.user.User;
import com.novel.api.dto.request.novel.GetNovelListSearch;
import com.novel.api.dto.request.novel.GetSubscribeNovelListSearch;
import org.springframework.data.domain.Page;

public interface NovelRepositoryCustom {

    Page<Novel>  getList(GetNovelListSearch search);
    Page<Novel> getSubscribeList(GetSubscribeNovelListSearch search, User user);
}
