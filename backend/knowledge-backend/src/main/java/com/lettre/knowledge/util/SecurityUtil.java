package com.lettre.knowledge.util;


import com.lettre.knowledge.exception.BusinessException;
import com.lettre.knowledge.security.LoginUser;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



public final class SecurityUtil {


    private SecurityUtil() {
    }


    public static Optional<LoginUser> getLoginUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof LoginUser loginUser) {
            return Optional.of(loginUser);
        }

        return Optional.empty();

    }


    public static LoginUser requireLoginUser() {

        return getLoginUser().orElseThrow(() ->
                new BusinessException(40100, "未登录")
        );

    }


    public static Long getCurrentUserId() {

        return requireLoginUser().getUserId();

    }


    public static String getCurrentUsername() {

        return requireLoginUser().getUsername();

    }


    public static boolean isAuthenticated() {

        return getLoginUser().isPresent();

    }


}
