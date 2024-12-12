package com.siemaszkiewicz.taskmanager.service;

import com.siemaszkiewicz.taskmanager.model.Category;
import com.siemaszkiewicz.taskmanager.model.Task;
import com.siemaszkiewicz.taskmanager.model.User;
import com.siemaszkiewicz.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Task> getTasksByUser(User user) {
        return taskRepository.findByUser(user);
    }

    @Override
    public List<Task> getTasksByUserAndCategory(User user, Category category) {
        return taskRepository.findByUserAndCategory(user, category);
    }

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> getTaskByIdAndUser(Long id, User user) {
        return taskRepository.findByIdAndUser(id, user);
    }

    @Override
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }
}
