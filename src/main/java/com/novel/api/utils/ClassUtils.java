package com.novel.api.utils;

import com.novel.api.domain.user.User;
import com.novel.api.dto.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;

public class ClassUtils {

    public static User getSafeUserBySafeCast(Authentication authentication) {
        UserDetailsImpl userDetails = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), UserDetailsImpl.class);

        return userDetails == null ? null : userDetails.getUser();
    }

    public static <T> T getSafeCastInstance(Object o, Class<T> clazz) {
        return clazz != null && clazz.isInstance(o) ? clazz.cast(o) : null;
    }

}
