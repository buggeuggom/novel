package com.novel.api.controller;

import com.novel.api.domain.user.User;
import com.novel.api.dto.request.episode.EpisodeSearch;
import com.novel.api.dto.request.episode.WriteEpisodeRequest;
import com.novel.api.dto.response.episode.EpisodeListResponse;
import com.novel.api.dto.response.episode.EpisodeResponse;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.service.EpisodeService;
import com.novel.api.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EpisodeController {

    private final EpisodeService episodeService;

    /**
     * 설명: 새로운 에피소드 생성
     * URL 파라미터: novelId
     * 요청 본문:  WriteEpisodeRequest
     */
    @PostMapping("/novels/{novelId}/episodes")
    public void write(@PathVariable Long novelId, @RequestBody WriteEpisodeRequest request, Authentication authentication) {

        User user = ClassUtils.getSafeUserBySafeCast(authentication);

        episodeService.write(novelId, request, user);
    }
    /**
     * 설명: 특정 에피소드 조회
     * URL 파라미터: episodeId
     */
    @GetMapping("/episodes/{episodeId}")
    public EpisodeResponse get(@PathVariable Long episodeId) {

        return EpisodeResponse.from(episodeService.get(episodeId));
    }

    /**
     * 설명: 특정 소설의 모든 챕터 조회
     * URL 파라미터: novelId
     */

    @GetMapping("/novels/{novelId}/episodes")
    public PageingResponse<EpisodeListResponse> getList(@PathVariable Long novelId, @ModelAttribute EpisodeSearch search) {

        return episodeService.getList(novelId, search);
    }

}
