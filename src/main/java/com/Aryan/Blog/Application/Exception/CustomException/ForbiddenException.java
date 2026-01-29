package com.Aryan.Blog.Application.Exception.CustomException;

public class ForbiddenException extends RuntimeException{

    public ForbiddenException(String message){
        super(message);
    }

}
