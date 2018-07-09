package com.mart1I1.music.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mart1I1.music.Application;
import com.mart1I1.music.homework.entity.Homework;
import com.mart1I1.music.lesson.entity.Lesson;
import com.mart1I1.music.homework.service.HomeworkService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HomeworkControllerTestIT {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Homework> homeworkList = new ArrayList<>();
    private MockMultipartFile multipartFile;
    private Lesson lesson;

    @Before
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        lesson = lessonService.addLesson(new Lesson(
                "2018-07-08 12:07:57.252",
                "username1",
                "title1",
                "description1"));

        homeworkList.add(homeworkService.addHomework(new Homework(
                "2018-07-08 12:07:57.252",
                "username1",
                1L)));

        homeworkList.add(homeworkService.addHomework(new Homework(
                "2018-07-08 12:07:57.252",
                "username2",
                1L)));

        multipartFile = new MockMultipartFile(
                "melody",
                "originalFilename.mp3",
                "audio/mpeg",
                "HelloWorld".getBytes());
        homeworkService.storeHomeworkMelody(homeworkList.get(0), multipartFile);
    }

    @Test
    public void addHomework() throws Exception{
        Homework homework = new Homework(3L, "2018-07-08 12:07:57.252", "username", lesson.getLessonId());
        mockMvc.perform(post("/music/homeworks")
                .content(objectMapper.writeValueAsBytes(homework))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.hwId", is(homework.getHwId().intValue())))
                .andExpect(jsonPath("$.lessonId", is(homework.getLessonId().intValue())))
                .andExpect(jsonPath("$.timestamp", is(homework.getTimestamp())))
                .andExpect(jsonPath("$.username", is(homework.getUsername())));
    }

    @Test
    public void addHomeworkWithNullField() throws Exception {
        Homework homework = new Homework(3L, "2018-07-08 12:07:57.252", "username", 1L);
        homework.setUsername(null);

        mockMvc.perform(post("/music/homeworks")
                .content(objectMapper.writeValueAsBytes(homework))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addHomeworkWithEmptyField() throws Exception {
        Homework homework = new Homework(3L, "", "username", 1L);
        mockMvc.perform(post("/music/homeworks")
                .content(objectMapper.writeValueAsBytes(homework))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addHomeworkLessonNotFound() throws Exception {
        Homework homework = new Homework(3L, "2018-07-08 12:07:57.252", "username", 3L);
        mockMvc.perform(post("/music/homeworks")
                .content(objectMapper.writeValueAsBytes(homework))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getHomework() throws Exception {
        Homework homework = homeworkList.get(0);
        mockMvc.perform(get("/music/homeworks/{hwId}",homework.getHwId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hwId", is(homework.getHwId().intValue())))
                .andExpect(jsonPath("$.lessonId", is(homework.getLessonId().intValue())))
                .andExpect(jsonPath("$.timestamp", is(homework.getTimestamp())))
                .andExpect(jsonPath("$.username", is(homework.getUsername())));
    }

    @Test
    public void getHomeworkNotFound() throws Exception {
        mockMvc.perform(get("/music/homeworks/{hwId}", homeworkList.size() + 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getHomeworkMelody() throws Exception {
        Homework homework = homeworkList.get(0);
        mockMvc.perform(get("/music/homeworks/{hwId}/melody", homework.getHwId()))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + homework.getRealMelodyName() + "\""));
    }

    @Test
    public void getHomeworkMelodyFileNotFound() throws Exception {
        Homework homework = homeworkList.get(1);
        mockMvc.perform(get("/music/homeworks/{hwId}/melody", homework.getHwId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getHomeworkMelodyHomeworkNotFound() throws Exception {
        mockMvc.perform(get("/music/homeworks/{hwId}/melody", homeworkList.size() + 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addHomeworkMelody() throws Exception {
        Homework homework = homeworkList.get(1);
        mockMvc.perform(multipart("/music/homeworks/{hwId}/melody", homework.getHwId())
                .file(multipartFile))
                .andExpect(status().isCreated());
    }

    @Test
    public void addHomeworkMelodyConflict() throws Exception {
        Homework homework = homeworkList.get(0);
        mockMvc.perform(multipart("/music/homeworks/{hwId}/melody", homework.getHwId())
                .file(multipartFile))
                .andExpect(status().isConflict());
    }

    @Test
    public void addHomeworkMelodyHomeworkNotFound() throws Exception {
        mockMvc.perform(multipart("/music/homeworks/{hwId}/melody", homeworkList.size() + 1)
                .file(multipartFile))
                .andExpect(status().isNotFound());
    }

}