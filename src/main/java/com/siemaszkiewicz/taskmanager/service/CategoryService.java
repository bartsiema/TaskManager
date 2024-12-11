package com.siemaszkiewicz.taskmanager.service;

import com.siemaszkiewicz.taskmanager.model.Category;
import com.siemaszkiewicz.taskmanager.model.User;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getCategoriesByUser(User user);
    Category saveCategory(Category category);
    Optional<Category> getCategoryByIdAndUser(Long id, User user);
    void deleteCategory(Category category);
    Optional<Category> getCategoryByNameAndUser(String name, User user);
}