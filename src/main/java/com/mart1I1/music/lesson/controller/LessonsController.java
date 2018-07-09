package com.mart1I1.music.lesson.controller;

import com.mart1I1.music.lesson.entity.Lesson;
import com.mart1I1.music.lesson.service.LessonService;
import com.mart1I1.music.lesson.validator.LessonValidator;
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
public class LessonsController {

    private LessonService lessonService;

    @Autowired
    private LessonValidator validator;

    @InitBinder
    protected void initBinder(final WebDataBinder binder)
    {
        binder.addValidators(validator);
    }

    @Autowired
    LessonsController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    ResponseEntity<Lesson> addMusicLesson(@Valid @RequestBody Lesson lesson) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.addLesson(lesson));
    }

    @GetMapping
    Collection<Lesson> getMusicLessons() {
        return lessonService.getLessons();
    }


    @GetMapping("/{lessonId}")
    Lesson getMusicLesson(@PathVariable Long lessonId) {
        return lessonService.getLessonBy(lessonId);
    }


    @GetMapping("/{lessonId}/melody")
    ResponseEntity<Resource> getMusicLessonMelody(@PathVariable Long lessonId) {
        Lesson lesson = lessonService.getLessonBy(lessonId);
        Resource melody = lessonService.getLessonMelody(lesson);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + lesson.getRealMelodyName() + "\"")
                .body(melody);
    }

    @PostMapping("/{lessonId}/melody")
    ResponseEntity<?> addMusicLessonMelody(@PathVariable Long lessonId,
                                @RequestParam(name = "melody") MultipartFile melody){
        Lesson lesson = lessonService.getLessonBy(lessonId);
        lessonService.storeLessonMelody(lesson, melody);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
