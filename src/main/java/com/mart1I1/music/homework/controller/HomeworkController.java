package com.mart1I1.music.homework.controller;

import com.mart1I1.music.homework.entity.Homework;
import com.mart1I1.music.homework.assessment.HomeworkAssessment;
import com.mart1I1.music.homework.service.HomeworkService;
import com.mart1I1.music.homework.validator.HomeworkValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/music/homeworks")
public class HomeworkController {

    private HomeworkService homeworkService;

    @Autowired
    private HomeworkValidator validator;

    @InitBinder
    protected void initBinder(final WebDataBinder binder)
    {
        binder.addValidators(validator);
    }

    @Autowired
    HomeworkController(HomeworkService homeworkService) {
        this.homeworkService = homeworkService;
    }

    @PostMapping
    ResponseEntity<Homework> addHomework(@Valid @RequestBody Homework homework) {
        return ResponseEntity.status(HttpStatus.CREATED).body(homeworkService.addHomework(homework));
    }

    @GetMapping("/{hwId}")
    Homework getHomework(@PathVariable Long hwId) {
        return homeworkService.getHomeworkBy(hwId);
    }


    @GetMapping("/{hwId}/melody")
    ResponseEntity<Resource> getHomeworkMelody(@PathVariable Long hwId) {
        Homework homework = homeworkService.getHomeworkBy(hwId);
        Resource melody = homeworkService.getHomeworkMelody(homework);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + homework.getRealMelodyName() + "\"")
                .body(melody);
    }

    @PostMapping("/{hwId}/melody")
    ResponseEntity<?> addHomeworkMelody(@PathVariable Long hwId,
                                        @RequestParam(name = "melody") MultipartFile melody){
        Homework homework = homeworkService.getHomeworkBy(hwId);
        homeworkService.storeHomeworkMelody(homework, melody);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{hwId}/assessment")
    ResponseEntity<HomeworkAssessment> getHomeworkAssessment(@PathVariable Long hwId) {
        Homework homework = homeworkService.getHomeworkBy(hwId);
        return ResponseEntity.status(HttpStatus.OK).body(homeworkService.getHomeworkAssessment(homework));
    }

}
