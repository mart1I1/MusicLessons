package com.mart1I1.music.homework.service;

import com.mart1I1.music.homework.entity.Homework;
import com.mart1I1.music.homework.assessment.HomeworkAssessment;
import com.mart1I1.music.homework.exception.HomeworkMelodyAlreadyExist;
import com.mart1I1.music.homework.exception.HomeworkNotFoundException;
import com.mart1I1.music.lesson.service.LessonService;
import com.mart1I1.music.homework.repository.HomeworkRepository;
import com.mart1I1.music.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HomeworkServiceImpl implements HomeworkService {

    private StorageService storageService;
    private HomeworkRepository homeworkRepository;
    private LessonService lessonService;

    @Autowired
    public HomeworkServiceImpl(StorageService storageService,
                               HomeworkRepository homeworkRepository,
                               LessonService lessonService) {
        this.storageService = storageService;
        this.homeworkRepository = homeworkRepository;
        this.lessonService = lessonService;
    }


    @Override
    public Homework getHomeworkBy(Long hwId) {
        return homeworkRepository.findById(hwId)
                .orElseThrow(() -> new HomeworkNotFoundException(hwId));
    }

    @Override
    public Homework addHomework(Homework homework) {
        lessonValidate(homework);
        return homeworkRepository.save(homework);
    }

    @Override
    public Resource getHomeworkMelody(Homework homework) {
        return storageService.loadAsResource(homework.getStoredMelodyName());
    }

    @Override
    public void storeHomeworkMelody(Homework homework, MultipartFile melody) {
        if (homework.getRealMelodyName() != null) throw new HomeworkMelodyAlreadyExist();

        homework.setStoredMelodyName(storageService.store(melody).toString());
        homework.setRealMelodyName(melody.getOriginalFilename());
        homeworkRepository.save(homework);
    }

    @Override
    public HomeworkAssessment getHomeworkAssessment(Homework homework) {
        melodiesValidate(homework);
        return new HomeworkAssessment(homework);
    }

    private void lessonValidate(Homework homework){
        lessonService.getLessonBy(homework.getLessonId());
    }

    private void melodiesValidate(Homework homework){
        lessonService.getLessonMelody(lessonService.getLessonBy(homework.getLessonId()));
        getHomeworkMelody(homework);
    }
}
