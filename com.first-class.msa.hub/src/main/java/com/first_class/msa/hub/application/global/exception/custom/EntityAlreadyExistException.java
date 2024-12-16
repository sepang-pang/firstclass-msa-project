package com.first_class.msa.hub.application.global.exception.custom;

public class EntityAlreadyExistException extends RuntimeException {

    public EntityAlreadyExistException(String message) {
        super(message);
    }

}
