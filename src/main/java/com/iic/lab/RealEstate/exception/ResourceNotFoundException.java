package com.iic.lab.RealEstate.exception;

public class ResourceNotFoundException extends RuntimeException{

    // Custom error message when resource not found.
    public ResourceNotFoundException(String message){
        super(message);
    }
}
