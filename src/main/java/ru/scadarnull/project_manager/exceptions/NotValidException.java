package ru.scadarnull.project_manager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class NotValidException extends RuntimeException{
    public NotValidException(String message){
        super(message);
    }
}
