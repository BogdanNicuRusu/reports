package com.nicu.reports.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nicu.reports.service.TokenService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String INVALID_AUTHORIZATION_TOKEN = "Invalid authorization token";

    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationToken = request.getHeader(AUTHORIZATION_HEADER);
        if (!isOptionsRequest(request) && (StringUtils.isEmpty(authorizationToken) || !tokenService.isValidAuthorizationToken(authorizationToken))) {
            response.sendError(401, INVALID_AUTHORIZATION_TOKEN);
            return false;
        }
        return true;
    }

    private boolean isOptionsRequest(HttpServletRequest request) {
        return request.getMethod().equals("OPTIONS");
    }
}
