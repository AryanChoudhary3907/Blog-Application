package com.Aryan.Blog.Application.Exception.CustomException;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
