package com.ing.fr.app.exceptions;

public class EntityAlreadyPresentException extends ServiceException{

    public EntityAlreadyPresentException(String message) {
        super(message);
    }
}
