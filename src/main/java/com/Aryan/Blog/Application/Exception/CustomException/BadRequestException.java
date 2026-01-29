package com.Aryan.Blog.Application.Exception.CustomException;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message){
        super(message);
    }
}
