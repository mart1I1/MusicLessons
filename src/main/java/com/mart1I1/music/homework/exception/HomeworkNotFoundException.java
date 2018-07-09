package com.mart1I1.music.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HomeworkNotFoundException extends RuntimeException  {
    public HomeworkNotFoundException(Long userId) {
        super("Could not find homework '" + userId + "'.");
    }
}
