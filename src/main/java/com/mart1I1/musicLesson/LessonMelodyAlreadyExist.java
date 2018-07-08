package com.mart1I1.musicLesson;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class LessonMelodyAlreadyExist extends RuntimeException{
    public LessonMelodyAlreadyExist() {
        super("lesson melody is already exist");
    }
}
