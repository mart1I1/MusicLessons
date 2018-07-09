package com.mart1I1.music.homework.assessment;

import com.mart1I1.music.homework.entity.Homework;

import java.util.ArrayList;
import java.util.List;

public class HomeworkAssessment {
    private Long assessment;
    private List<HomeworkMistake> homeworkMistakeList = new ArrayList<>();

    public HomeworkAssessment(){

    }

    public HomeworkAssessment(Homework homework){

    }

    public Long getAssessment() {
        return assessment;
    }

    public List<HomeworkMistake> getHomeworkMistakeList() {
        return homeworkMistakeList;
    }
}
