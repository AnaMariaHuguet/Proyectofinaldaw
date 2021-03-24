package com.proyectofinal.daw.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class AuthenticationSuccessWithSessionHandler implements AuthenticationSuccessHandler, LogoutSuccessHandler {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        request.getSession().removeAttribute(USERNAME);
        request.getSession().removeAttribute(PASSWORD);

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        request.getSession().setAttribute(PASSWORD, request.getParameter(PASSWORD));
        request.getSession().setAttribute(USERNAME, request.getParameter(USERNAME));

        response.sendRedirect("/index/2");
    }
}