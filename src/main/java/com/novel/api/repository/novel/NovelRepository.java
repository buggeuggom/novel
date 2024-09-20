package com.novel.api.repository.novel;

import com.novel.api.domain.novel.Novel;
import com.novel.api.dto.request.novel.GetSubscribeNovelListSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NovelRepository  extends JpaRepository<Novel, Long>, NovelRepositoryCustom {

    Optional<Novel> findByTitle(String title);


}
