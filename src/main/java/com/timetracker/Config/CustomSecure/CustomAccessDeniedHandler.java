package com.timetracker.Config.CustomSecure;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ServletOutputStream out = response.getOutputStream();
        String errorMessage = "{\"error\":\""
                + "Ошибка авторизации: у вас недостаточно прав для совершения данного действия" + "\"}";
        out.write(errorMessage.getBytes("UTF-8"));
    }
/*
    private void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        if (!response.isCommitted()) {
            new DefaultRedirectStrategy().sendRedirect(request, response, url);
        }
    }*/
}