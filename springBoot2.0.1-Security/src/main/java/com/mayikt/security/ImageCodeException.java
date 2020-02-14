package com.mayikt.security;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常，抛出该异常后会自动进入失败的处理handle中， MyAuthenticationFailureHandler
 */
public class ImageCodeException extends AuthenticationException {

    public ImageCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public ImageCodeException(String msg) {
        super(msg);
    }
}
