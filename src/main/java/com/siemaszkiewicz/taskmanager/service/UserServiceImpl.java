package com.siemaszkiewicz.taskmanager.service;

import com.siemaszkiewicz.taskmanager.model.Category;
import com.siemaszkiewicz.taskmanager.model.Status;
import com.siemaszkiewicz.taskmanager.model.User;
import com.siemaszkiewicz.taskmanager.repository.CategoryRepository;
import com.siemaszkiewicz.taskmanager.repository.StatusRepository;
import com.siemaszkiewicz.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        String[] defaultStatuses = {"NEW", "IN_PROGRESS", "DONE"};
        for (String sName : defaultStatuses) {
            Status status = new Status();
            status.setName(sName);
            status.setUser(user);
            statusRepository.save(status);
        }

        Category personalCategory = new Category();
        personalCategory.setName("PERSONAL");
        personalCategory.setUser(user);
        categoryRepository.save(personalCategory);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
