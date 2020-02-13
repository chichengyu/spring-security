package com.security;

import org.springframework.security.core.AuthenticationException;

/**
 * @author http://www.sm1234.cn
 * @version 1.0
 * @description cn.sm1234.security
 * @date 18/4/15
 */
public class ImageCodeException extends AuthenticationException {

    public ImageCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public ImageCodeException(String msg) {
        super(msg);
    }
}
