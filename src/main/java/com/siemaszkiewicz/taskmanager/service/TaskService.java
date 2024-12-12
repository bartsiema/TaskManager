package com.siemaszkiewicz.taskmanager.service;

import com.siemaszkiewicz.taskmanager.model.Category;
import com.siemaszkiewicz.taskmanager.model.Task;
import com.siemaszkiewicz.taskmanager.model.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getTasksByUser(User user);
    List<Task> getTasksByUserAndCategory(User user, Category category);
    Task saveTask(Task task);
    Optional<Task> getTaskByIdAndUser(Long id, User user);
    void deleteTask(Task task);
}