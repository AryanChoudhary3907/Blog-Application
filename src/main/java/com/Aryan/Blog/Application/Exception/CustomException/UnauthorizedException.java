package com.Aryan.Blog.Application.Exception.CustomException;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message){
        super(message);
    }
}
