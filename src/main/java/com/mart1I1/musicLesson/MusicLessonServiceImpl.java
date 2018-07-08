package com.mart1I1.musicLesson;

import com.mart1I1.entity.MusicLesson;
import com.mart1I1.repository.MusicLessonsRepository;
import com.mart1I1.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@Service
public class MusicLessonServiceImpl implements MusicLessonService {

    private MusicLessonsRepository musicLessonsRepository;
    private StorageService storageService;

    @Autowired
    public MusicLessonServiceImpl(MusicLessonsRepository musicLessonsRepository,
                                  StorageService storageService) {
        this.musicLessonsRepository = musicLessonsRepository;
        this.storageService = storageService;
    }

    @Override
    public MusicLesson getLessonBy(Long lessonId) {
        return musicLessonsRepository
                .findById(lessonId)
                .orElseThrow(() -> new LessonNotFoundException(lessonId));
    }

    @Override
    public Collection<MusicLesson> getLessons() {
        return musicLessonsRepository.findAll();
    }

    @Override
    public MusicLesson addLesson(MusicLesson musicLesson) {
        return musicLessonsRepository.save(musicLesson);
    }

    @Override
    public Resource getLessonMelody(MusicLesson musicLesson) {
        return storageService.loadAsResource(musicLesson.getStoredMelodyName());
    }

    @Override
    public void storeLessonMelody(MusicLesson musicLesson, MultipartFile melody) {
        if (musicLesson.getRealMelodyName() != null) throw new LessonMelodyAlreadyExist();

        musicLesson.setStoredMelodyName(storageService.store(melody).toString());
        musicLesson.setRealMelodyName(melody.getOriginalFilename());
        musicLessonsRepository.save(musicLesson);
    }
}
