package com.mart1I1.music.lesson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mart1I1.music.Application;
import com.mart1I1.music.lesson.entity.Lesson;
import com.mart1I1.music.lesson.service.LessonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LessonsControllerTestIT {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private LessonService musicLessonService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Lesson> lessonList = new ArrayList<>();
    private MockMultipartFile multipartFile;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        lessonList.add(musicLessonService.addLesson(new Lesson(
                "2018-07-08 12:07:57.252",
                "username1",
                "title1",
                "description1")));

        lessonList.add(musicLessonService.addLesson(new Lesson(
                "2018-07-08 12:07:57.252",
                "username2",
                "title2",
                "description2")));
        multipartFile = new MockMultipartFile(
                "melody",
                "originalFilename.mp3",
                "audio/mpeg",
                "HelloWorld".getBytes());
        musicLessonService.storeLessonMelody(lessonList.get(0), multipartFile);
    }

    @Test
    public void addMusicLesson() throws Exception{
        Lesson lesson = new Lesson(3L, "2018-07-08 12:07:57.252", "username", "title", "description");
        mockMvc.perform(post("/music/lessons")
                .content(objectMapper.writeValueAsBytes(lesson))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lessonId", is(lesson.getLessonId().intValue())))
                .andExpect(jsonPath("$.timestamp", is(lesson.getTimestamp())))
                .andExpect(jsonPath("$.username", is(lesson.getUsername())))
                .andExpect(jsonPath("$.title", is(lesson.getTitle())))
                .andExpect(jsonPath("$.description", is(lesson.getDescription())));
    }

    @Test
    public void addMusicLessonWithNullField() throws Exception {
        Lesson lesson = new Lesson(3L, "2018-07-08 12:07:57.252", "username", "title", "description");
        lesson.setTitle(null);

        mockMvc.perform(post("/music/lessons")
                .content(objectMapper.writeValueAsBytes(lesson))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addMusicLessonWithEmptyField() throws Exception {
        Lesson lesson = new Lesson(3L, "", "username", "title", "description");
        mockMvc.perform(post("/music/lessons")
                .content(objectMapper.writeValueAsBytes(lesson))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getMusicLessons() throws Exception {
        mockMvc.perform(get("/music/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lessonId", is(lessonList.get(0).getLessonId().intValue())))
                .andExpect(jsonPath("$[0].timestamp", is(lessonList.get(0).getTimestamp())))
                .andExpect(jsonPath("$[0].username", is(lessonList.get(0).getUsername())))
                .andExpect(jsonPath("$[0].title", is(lessonList.get(0).getTitle())))
                .andExpect(jsonPath("$[0].description", is(lessonList.get(0).getDescription())))
                .andExpect(jsonPath("$[1].lessonId", is(lessonList.get(1).getLessonId().intValue())))
                .andExpect(jsonPath("$[1].timestamp", is(lessonList.get(1).getTimestamp())))
                .andExpect(jsonPath("$[1].username", is(lessonList.get(1).getUsername())))
                .andExpect(jsonPath("$[1].title", is(lessonList.get(1).getTitle())))
                .andExpect(jsonPath("$[1].description", is(lessonList.get(1).getDescription())));
    }

    @Test
    public void getMusicLesson() throws Exception {
        Lesson lesson = lessonList.get(0);
        mockMvc.perform(get("/music/lessons/{lessonId}", lesson.getLessonId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lessonId", is(lesson.getLessonId().intValue())))
                .andExpect(jsonPath("$.timestamp", is(lesson.getTimestamp())))
                .andExpect(jsonPath("$.username", is(lesson.getUsername())))
                .andExpect(jsonPath("$.title", is(lesson.getTitle())))
                .andExpect(jsonPath("$.description", is(lesson.getDescription())));
    }

    @Test
    public void getMusicLessonNotFound() throws Exception {
        mockMvc.perform(get("/music/lessons/{lessonId}", lessonList.size() + 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getMusicLessonMelody() throws Exception {
        Lesson lesson = lessonList.get(0);
        mockMvc.perform(get("/music/lessons/{lessonId}/melody", lesson.getLessonId()))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + lesson.getRealMelodyName() + "\""));
    }

    @Test
    public void getMusicLessonMelodyFileNotFound() throws Exception {
        Lesson lesson = lessonList.get(1);
        mockMvc.perform(get("/music/lessons/{lessonId}/melody", lesson.getLessonId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getMusicLessonMelodyLessonNotFound() throws Exception {
        mockMvc.perform(get("/music/lessons/{lessonId}/melody", lessonList.size() + 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addMusicLessonMelody() throws Exception {
        Lesson lesson = lessonList.get(1);
        mockMvc.perform(multipart("/music/lessons/{lessonId}/melody", lesson.getLessonId())
                .file(multipartFile))
                .andExpect(status().isCreated());
    }

    @Test
    public void addMusicLessonMelodyConflict() throws Exception {
        Lesson lesson = lessonList.get(0);
        mockMvc.perform(multipart("/music/lessons/{lessonId}/melody", lesson.getLessonId())
                .file(multipartFile))
                .andExpect(status().isConflict());
    }

    @Test
    public void addMusicLessonMelodyLessonNotFound() throws Exception {
        mockMvc.perform(multipart("/music/lessons/{lessonId}/melody", lessonList.size() + 1)
                .file(multipartFile))
                .andExpect(status().isNotFound());
    }

}