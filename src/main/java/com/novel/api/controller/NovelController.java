package com.novel.api.controller;


import com.novel.api.domain.user.User;
import com.novel.api.dto.CustomUserDetails;
import com.novel.api.dto.request.novel.GetNovelListSearch;
import com.novel.api.dto.request.novel.WriteNovelRequest;
import com.novel.api.dto.request.novel.EditNovelRequest;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.dto.response.novel.GetNovelListResponse;
import com.novel.api.dto.response.novel.GetNovelResponse;
import com.novel.api.service.NovelService;
import com.novel.api.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/novels")
@RequiredArgsConstructor
public class NovelController {

    private final NovelService novelService;

    /**
     * 설명: 특정 소설 조회
     * URL 파라미터: id
     */
    @GetMapping("/{novelId}")
    public GetNovelResponse get(@PathVariable Long novelId) {

        return GetNovelResponse.from(novelService.get(novelId));
    }

    /**
     * 설명: 새로운 소설 생성
     * 요청 본문: PostNovelRequest
     */
    @PostMapping
    public void write(@RequestBody WriteNovelRequest request, Authentication authentication) {

        User user = ClassUtils.getSafeUserBySafeCast(authentication);

        novelService.write(request, user);
    }

    /**
     * 설명: 소설 정보 업데이트
     * URL 파라미터: id
     * 요청 본문: PostNovelRequest
     */
    @PutMapping("/{novelId}")
    public void edit(@PathVariable Long novelId, @RequestBody EditNovelRequest request, Authentication authentication) {

        User user = ClassUtils.getSafeUserBySafeCast(authentication);

        novelService.edit(novelId, request, user);
    }

    /**
     * 설명: 소설 삭제
     * URL 파라미터: id
     */
    @DeleteMapping("/{novelId}")
    public void delete(@PathVariable Long novelId, Authentication authentication) {
        User user = ClassUtils.getSafeUserBySafeCast(authentication);

        novelService.delete(novelId, user);
    }

    /**
     * 설명: 특정 소설 리스트 조회
     */
    @GetMapping
    public PageingResponse<GetNovelListResponse> getList(@ModelAttribute GetNovelListSearch getNovelListSearch) {
        return novelService.getList(getNovelListSearch);
    }

}
