package com.mart1I1.repository;

import com.mart1I1.entity.MusicLesson;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface MusicLessonsRepository extends CrudRepository<MusicLesson, Long> {

    @Override
    Collection<MusicLesson> findAll();

    @Override
    Optional<MusicLesson> findById(Long aLong);
}
