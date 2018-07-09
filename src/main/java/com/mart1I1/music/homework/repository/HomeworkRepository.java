package com.mart1I1.music.homework.repository;

import com.mart1I1.music.homework.entity.Homework;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HomeworkRepository extends CrudRepository<Homework, Long> {
    @Override
    Optional<Homework> findById(Long aLong);
}
