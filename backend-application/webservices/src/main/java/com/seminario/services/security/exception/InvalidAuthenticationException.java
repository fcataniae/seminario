package com.seminario.services.security.exception;


import org.springframework.security.core.AuthenticationException;

public class InvalidAuthenticationException extends AuthenticationException {
    public InvalidAuthenticationException(String e) {
        super(e);
    }
}