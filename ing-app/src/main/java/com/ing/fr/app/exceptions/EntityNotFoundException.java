package com.ing.fr.app.exceptions;

public class EntityNotFoundException extends ServiceException{

    public EntityNotFoundException(String message) {
        super(message);
    }
}
