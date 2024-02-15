package com.packtpub.springsecurity.access.expression;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;


@Component
public class CustomWebExpression {

    public boolean isLocalHost(final HttpServletRequest request) {
       return "localhost".equals(request.getServerName());
    }

}