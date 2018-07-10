package com.mart1I1.music.homework.validator;

import com.mart1I1.music.homework.entity.Homework;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class HomeworkValidator implements Validator {
    private final static String EMPTY_FIELD = "empty field";
    private final static String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSSSS";

    @Override
    public boolean supports(Class<?> aClass) {
        return Homework.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Homework homework = (Homework) o;
        if (homework.getTimestamp() != null) {
            SimpleDateFormat format = new SimpleDateFormat(TIMESTAMP_PATTERN);
            try {
                format.parse(homework.getTimestamp());
            } catch (ParseException e){
                errors.rejectValue("timestamp", "bad format. need: " + TIMESTAMP_PATTERN);
            }
        }
    }
}
