package com.mart1I1.music.lesson.repository;

import com.mart1I1.music.lesson.entity.Lesson;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface LessonsRepository extends CrudRepository<Lesson, Long> {

    @Override
    Collection<Lesson> findAll();

    @Override
    Optional<Lesson> findById(Long aLong);
}
