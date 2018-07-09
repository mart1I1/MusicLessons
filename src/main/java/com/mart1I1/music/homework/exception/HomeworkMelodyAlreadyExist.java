package com.mart1I1.music.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class HomeworkMelodyAlreadyExist extends RuntimeException {
    public HomeworkMelodyAlreadyExist() {
        super("homework already exist");
    }
}
