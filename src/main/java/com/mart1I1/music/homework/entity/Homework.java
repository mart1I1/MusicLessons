package com.mart1I1.music.homework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "MUSIC_HOMEWORK")
public class Homework {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name= "increment", strategy= "increment")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long hwId;

    @NotNull
    private String timestamp;
    @NotNull
    private String username;
    @NotNull
    private Long lessonId;

    @JsonIgnore
    private String realMelodyName;
    @JsonIgnore
    private String storedMelodyName;

    public Homework() {

    }

    public Homework(String timestamp, String username, Long lessonId) {
        this.timestamp = timestamp;
        this.username = username;
        this.lessonId = lessonId;
    }

    public Homework(Long hwId, String timestamp, String username, Long lessonId) {
        this(timestamp, username, lessonId);
        this.hwId = hwId;
    }

    public Long getHwId() {
        return hwId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public String getRealMelodyName() {
        return realMelodyName;
    }

    public void setRealMelodyName(String realMelodyName) {
        this.realMelodyName = realMelodyName;
    }

    public String getStoredMelodyName() {
        return storedMelodyName;
    }

    public void setStoredMelodyName(String storedMelodyName) {
        this.storedMelodyName = storedMelodyName;
    }
}