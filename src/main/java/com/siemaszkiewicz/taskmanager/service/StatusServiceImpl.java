package com.siemaszkiewicz.taskmanager.service;

import com.siemaszkiewicz.taskmanager.model.Status;
import com.siemaszkiewicz.taskmanager.model.User;
import com.siemaszkiewicz.taskmanager.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public List<Status> getStatusesByUser(User user) {
        return statusRepository.findByUser(user);
    }

    @Override
    public Status saveStatus(Status status) {
        return statusRepository.save(status);
    }

    @Override
    public Optional<Status> getStatusByIdAndUser(Long id, User user) {
        return statusRepository.findByIdAndUser(id, user);
    }

    @Override
    public Optional<Status> getStatusByNameAndUser(String name, User user) {
        return statusRepository.findByNameAndUser(name, user);
    }

    @Override
    public void deleteStatus(Status status) {
        statusRepository.delete(status);
    }
}