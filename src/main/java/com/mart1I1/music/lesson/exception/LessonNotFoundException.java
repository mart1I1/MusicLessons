package com.mart1I1.music.lesson.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LessonNotFoundException extends RuntimeException {

    public LessonNotFoundException(Long userId) {
        super("Could not find lesson '" + userId + "'.");
    }
}
