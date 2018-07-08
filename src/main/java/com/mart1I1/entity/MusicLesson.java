package com.mart1I1.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "MUSIC_LESSONS")
public class MusicLesson {

    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lessonId;

    @NotNull
    private String timestamp;
    @NotNull
    private String username;
    @NotNull
    private String title;
    @NotNull
    private String description;

    @JsonIgnore
    private String realMelodyName;
    @JsonIgnore
    private String storedMelodyName;

    public MusicLesson() {

    }

    public MusicLesson(Long lessonId, String timestamp, String username, String title, String description){
        this(timestamp, username, title, description);
        this.lessonId = lessonId;
    }

    public MusicLesson(String timestamp, String username, String title, String description) {
        this.timestamp = timestamp;
        this.username = username;
        this.title = title;
        this.description = description;
    }

    public Long getLessonId() {
        return lessonId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
