package com.mart1I1.musicLesson;

import com.mart1I1.entity.MusicLesson;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class MusicLessonValidator implements Validator {
    private final static String EMPTY_FIELD = "empty field";
    private final static String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSSSS";

    @Override
    public boolean supports(Class<?> aClass) {
        return MusicLesson.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        MusicLesson musicLesson = (MusicLesson)o;
        if (musicLesson.getTimestamp() != null && musicLesson.getTimestamp().isEmpty()) {
            errors.rejectValue("timestamp", EMPTY_FIELD);
        }
        if (musicLesson.getTimestamp() != null) {
            SimpleDateFormat format = new java.text.SimpleDateFormat(TIMESTAMP_PATTERN);
            try {
                format.parse(musicLesson.getTimestamp());
            } catch (ParseException e){
                errors.rejectValue("timestamp", "bad format. need: " + TIMESTAMP_PATTERN);
            }
        }
        if (musicLesson.getUsername() != null && musicLesson.getTimestamp().isEmpty()) {
            errors.rejectValue("username", EMPTY_FIELD);
        }
        if (musicLesson.getTitle() != null && musicLesson.getTimestamp().equals("")) {
            errors.rejectValue("title", EMPTY_FIELD);
        }
    }
}
