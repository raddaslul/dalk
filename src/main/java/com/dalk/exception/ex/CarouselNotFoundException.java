package com.dalk.exception.ex;

public class CarouselNotFoundException extends RuntimeException{
    public CarouselNotFoundException(String message) {
        super(message);
    }
}
