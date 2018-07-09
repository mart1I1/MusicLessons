package com.mart1I1.music.lesson.service;

import com.mart1I1.music.lesson.entity.Lesson;
import com.mart1I1.music.lesson.exception.LessonMelodyAlreadyExist;
import com.mart1I1.music.lesson.exception.LessonNotFoundException;
import com.mart1I1.music.lesson.repository.LessonsRepository;
import com.mart1I1.music.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@Service
public class LessonServiceImpl implements LessonService {

    private LessonsRepository lessonsRepository;
    private StorageService storageService;

    @Autowired
    public LessonServiceImpl(LessonsRepository lessonsRepository,
                             StorageService storageService) {
        this.lessonsRepository = lessonsRepository;
        this.storageService = storageService;
    }

    @Override
    public Lesson getLessonBy(Long lessonId) {
        return lessonsRepository
                .findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException(lessonId));
    }

    @Override
    public Collection<Lesson> getLessons() {
        return lessonsRepository.findAll();
    }

    @Override
    public Lesson addLesson(Lesson lesson) {
        return lessonsRepository.save(lesson);
    }

    @Override
    public Resource getLessonMelody(Lesson lesson) {
        return storageService.loadAsResource(lesson.getStoredMelodyName());
    }

    @Override
    public void storeLessonMelody(Lesson lesson, MultipartFile melody) {
        if (lesson.getRealMelodyName() != null) throw new LessonMelodyAlreadyExist();

        lesson.setStoredMelodyName(storageService.store(melody).toString());
        lesson.setRealMelodyName(melody.getOriginalFilename());
        lessonsRepository.save(lesson);
    }
}
