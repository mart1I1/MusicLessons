package com.mart1I1.musicLesson;

import com.mart1I1.entity.MusicLesson;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

public interface MusicLessonService {
    MusicLesson getLessonBy(Long lessonId);
    Collection<MusicLesson> getLessons();
    MusicLesson addLesson(MusicLesson musicLesson);
    Resource getLessonMelody(MusicLesson musicLesson);
    void storeLessonMelody(MusicLesson musicLesson, MultipartFile melody);
}
