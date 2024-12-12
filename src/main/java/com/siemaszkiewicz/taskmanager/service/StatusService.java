package com.siemaszkiewicz.taskmanager.service;

import com.siemaszkiewicz.taskmanager.model.Status;
import com.siemaszkiewicz.taskmanager.model.User;

import java.util.List;
import java.util.Optional;

public interface StatusService {
    List<Status> getStatusesByUser(User user);
    Status saveStatus(Status status);
    Optional<Status> getStatusByIdAndUser(Long id, User user);
    Optional<Status> getStatusByNameAndUser(String name, User user);
    void deleteStatus(Status status);
    boolean canDeleteStatus(Status status);
}