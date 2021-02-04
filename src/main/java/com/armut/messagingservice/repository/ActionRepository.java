package com.armut.messagingservice.repository;

import com.armut.messagingservice.entity.Action;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ActionRepository extends CrudRepository<Action, Long> {
    List<Action> findAllByUserId(Long userId);

    List<Action> findAllByUserIdAndActionType(Long userId, String actionType);
}
