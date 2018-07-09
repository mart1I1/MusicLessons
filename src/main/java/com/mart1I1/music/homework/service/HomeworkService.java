package com.mart1I1.music.homework.service;

import com.mart1I1.music.homework.entity.Homework;
import com.mart1I1.music.homework.assessment.HomeworkAssessment;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface HomeworkService {
    Homework getHomeworkBy(Long hwId);
    Homework addHomework(Homework homework);
    Resource getHomeworkMelody(Homework homework);
    void storeHomeworkMelody(Homework homework, MultipartFile melody);
    HomeworkAssessment getHomeworkAssessment(Homework homework);
}
