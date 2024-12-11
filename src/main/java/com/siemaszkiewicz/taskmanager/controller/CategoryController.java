package com.siemaszkiewicz.taskmanager.controller;

import com.siemaszkiewicz.taskmanager.model.Category;
import com.siemaszkiewicz.taskmanager.model.User;
import com.siemaszkiewicz.taskmanager.service.CategoryService;
import com.siemaszkiewicz.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listCategories(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        model.addAttribute("categories", categoryService.getCategoriesByUser(user));
        return "categories/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/create";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute("category") Category category,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        category.setUser(user);
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Category category = categoryService.getCategoryByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "categories/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute("category") Category category,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        category.setId(id);
        category.setUser(user);
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Category category = categoryService.getCategoryByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        categoryService.deleteCategory(category);
        return "redirect:/categories";
    }

    private User getCurrentUser(UserDetails userDetails) {
        return userService.findByUsername(userDetails.getUsername());
    }
}