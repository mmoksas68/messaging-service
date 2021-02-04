package com.armut.messagingservice.repository;

import com.armut.messagingservice.entity.Errors;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ErrorsRepository extends CrudRepository<Errors, Long> {
    List<Errors> findAllByUserId(Long userId);
}
