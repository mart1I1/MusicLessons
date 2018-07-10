package com.mart1I1.music.lesson.validator;

import com.mart1I1.music.lesson.entity.Lesson;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class LessonValidator implements Validator {
    private final static String EMPTY_FIELD = "empty field";
    private final static String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSSSS";

    @Override
    public boolean supports(Class<?> aClass) {
        return Lesson.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Lesson lesson = (Lesson)o;
        if (lesson.getTimestamp() != null) {
            SimpleDateFormat format = new java.text.SimpleDateFormat(TIMESTAMP_PATTERN);
            try {
                format.parse(lesson.getTimestamp());
            } catch (ParseException e){
                errors.rejectValue("timestamp", "bad format. need: " + TIMESTAMP_PATTERN);
            }
        }
    }
}
