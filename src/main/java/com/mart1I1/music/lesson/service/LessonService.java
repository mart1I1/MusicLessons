package com.mart1I1.music.lesson.service;

import com.mart1I1.music.lesson.entity.Lesson;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface LessonService {
    Lesson getLessonBy(Long lessonId);
    Collection<Lesson> getLessons();
    Lesson addLesson(Lesson lesson);
    Resource getLessonMelody(Lesson lesson);
    void storeLessonMelody(Lesson lesson, MultipartFile melody);
}
