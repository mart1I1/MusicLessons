package com.mart1I1;

import com.mart1I1.entity.MusicLesson;
import com.mart1I1.musicLesson.MusicLessonService;
import com.mart1I1.musicLesson.MusicLessonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/music/lessons")
public class MusicLessonsController {

    private MusicLessonService musicLessonService;

    @Autowired
    private MusicLessonValidator validator;

    @InitBinder
    protected void initBinder(final WebDataBinder binder)
    {
        binder.addValidators(validator);
    }

    @Autowired
    MusicLessonsController(MusicLessonService musicLessonService) {
        this.musicLessonService = musicLessonService;
    }

    @PostMapping
    ResponseEntity<MusicLesson> addMusicLesson(@Valid @RequestBody MusicLesson musicLesson) {
        return ResponseEntity.status(HttpStatus.CREATED).body(musicLessonService.addLesson(musicLesson));
    }

    @GetMapping
    Collection<MusicLesson> getMusicLessons() {
        return musicLessonService.getLessons();
    }


    @GetMapping("/{lessonId}")
    MusicLesson getMusicLesson(@PathVariable Long lessonId) {
        return musicLessonService.getLessonBy(lessonId);
    }


    @GetMapping("/{lessonId}/melody")
    ResponseEntity<Resource> getMusicLessonMelody(@PathVariable Long lessonId) {
        MusicLesson musicLesson = musicLessonService.getLessonBy(lessonId);
        Resource melody = musicLessonService.getLessonMelody(musicLesson);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + musicLesson.getRealMelodyName() + "\"")
                .body(melody);
    }

    @PostMapping("/{lessonId}/melody")
    ResponseEntity<?> addMusicLessonMelody(@PathVariable Long lessonId,
                                @RequestParam(name = "melody") MultipartFile melody){
        MusicLesson musicLesson = musicLessonService.getLessonBy(lessonId);
        musicLessonService.storeLessonMelody(musicLesson, melody);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
