package com.siemaszkiewicz.taskmanager.controller;

import com.siemaszkiewicz.taskmanager.model.Category;
import com.siemaszkiewicz.taskmanager.model.Status;
import com.siemaszkiewicz.taskmanager.model.Task;
import com.siemaszkiewicz.taskmanager.model.User;
import com.siemaszkiewicz.taskmanager.service.CategoryService;
import com.siemaszkiewicz.taskmanager.service.StatusService;
import com.siemaszkiewicz.taskmanager.service.TaskService;
import com.siemaszkiewicz.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private StatusService statusService; // Added

    @GetMapping
    public String listTasks(Model model, @AuthenticationPrincipal UserDetails userDetails,
                            @RequestParam(required = false) Long categoryId) {
        User user = getCurrentUser(userDetails);
        List<Task> tasks;

        if (categoryId != null) {
            Category category = categoryService.getCategoryByIdAndUser(categoryId, user)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
            tasks = taskService.getTasksByUserAndCategory(user, category);
        } else {
            tasks = taskService.getTasksByUser(user);
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("categories", categoryService.getCategoriesByUser(user));
        model.addAttribute("selectedCategoryId", categoryId);
        return "tasks/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        model.addAttribute("task", new Task());
        model.addAttribute("categories", categoryService.getCategoriesByUser(user));
        model.addAttribute("statuses", statusService.getStatusesByUser(user));
        return "tasks/create";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute("task") Task task,
                             @RequestParam("categoryId") Long categoryId,
                             @RequestParam("statusId") Long statusId,
                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Category category = categoryService.getCategoryByIdAndUser(categoryId, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));

        Status status = statusService.getStatusByIdAndUser(statusId, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid status Id:" + statusId));

        task.setUser(user);
        task.setCategory(category);
        task.setStatus(status);

        taskService.saveTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Task task = taskService.getTaskByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));

        model.addAttribute("task", task);
        model.addAttribute("categories", categoryService.getCategoriesByUser(user));
        model.addAttribute("statuses", statusService.getStatusesByUser(user));
        return "tasks/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable Long id,
                             @ModelAttribute("task") Task updatedTask,
                             @RequestParam("categoryId") Long categoryId,
                             @RequestParam("statusId") Long statusId,
                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Task existingTask = taskService.getTaskByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));

        Category category = categoryService.getCategoryByIdAndUser(categoryId, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));

        Status status = statusService.getStatusByIdAndUser(statusId, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid status Id:" + statusId));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCategory(category);
        existingTask.setStatus(status);

        taskService.saveTask(existingTask);
        return "redirect:/tasks";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        Task task = taskService.getTaskByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task Id:" + id));
        taskService.deleteTask(task);
        return "redirect:/tasks";
    }

    private User getCurrentUser(UserDetails userDetails) {
        return userService.findByUsername(userDetails.getUsername());
    }
}
