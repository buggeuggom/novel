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

    @PostMapping("/novels/{novelId}/episodes")
    public void write(@PathVariable Long novelId, @RequestBody WriteEpisodeRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);

        episodeService.write(novelId, request, user);
    }

    @GetMapping("/episodes/{episodeId}")
    public EpisodeResponse get(@PathVariable Long episodeId) {

        return EpisodeResponse.from(episodeService.get(episodeId));
    }

    @GetMapping("/novels/{novelId}/episodes")
    public PageingResponse<EpisodeListResponse> getList(@PathVariable Long novelId, @ModelAttribute EpisodeSearch search) {

     return episodeService.getList(novelId, search);
    }

}
