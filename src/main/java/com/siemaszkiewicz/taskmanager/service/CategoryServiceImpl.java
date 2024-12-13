package com.siemaszkiewicz.taskmanager.service;

import com.siemaszkiewicz.taskmanager.model.Category;
import com.siemaszkiewicz.taskmanager.model.User;
import com.siemaszkiewicz.taskmanager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TaskService taskService;

    @Override
    public List<Category> getCategoriesByUser(User user) {
        return categoryRepository.findByUser(user);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> getCategoryByIdAndUser(Long id, User user) {
        return categoryRepository.findByIdAndUser(id, user);
    }

    @Override
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public boolean existsByNameAndUser(String name, User user) {
        return categoryRepository.existsByNameAndUser(name, user);
    }

    @Override
    public boolean canDeleteCategory(Category category) {
        return !taskService.getTasksByUserAndCategory(category.getUser(), category).isEmpty();
    }
}
