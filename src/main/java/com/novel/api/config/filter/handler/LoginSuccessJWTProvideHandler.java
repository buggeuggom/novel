package com.novel.api.config.filter.handler;

import com.novel.api.domain.user.User;
import com.novel.api.utils.ClassUtils;
import com.novel.api.utils.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessJWTProvideHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final String secretKey;
    private final Long expiredTimeMs;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        User user = ClassUtils.getSafeUserBySafeCast(authentication);
        String email = user.getEmail();

        String token = JwtUtils.generateAccessToken(email, secretKey, expiredTimeMs);

        log.info( "로그인 성공. JWT 발급. email: {}" ,email);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        response.setStatus(SC_OK);
    }
}
