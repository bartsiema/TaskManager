package com.siemaszkiewicz.taskmanager.repository;

import com.siemaszkiewicz.taskmanager.model.Status;
import com.siemaszkiewicz.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    List<Status> findByUser(User user);
    Optional<Status> findByIdAndUser(Long id, User user);
    Optional<Status> findByNameAndUser(String name, User user);
}