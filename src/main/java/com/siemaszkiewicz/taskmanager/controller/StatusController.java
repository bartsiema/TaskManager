package com.siemaszkiewicz.taskmanager.controller;

import com.siemaszkiewicz.taskmanager.model.Status;
import com.siemaszkiewicz.taskmanager.model.User;
import com.siemaszkiewicz.taskmanager.service.StatusService;
import com.siemaszkiewicz.taskmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/statuses")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listStatuses(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("statuses", statusService.getStatusesByUser(user));
        return "statuses/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("status", new Status());
        return "statuses/create";
    }

    @PostMapping("/create")
    public String createStatus(@ModelAttribute("status") Status status,
                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        status.setUser(user);

        if (statusService.getStatusByNameAndUser(status.getName(), user).isPresent()) {
            return "redirect:/statuses?error=exists";
        }
        statusService.saveStatus(status);
        return "redirect:/statuses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Status status = statusService.getStatusByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Status Id: " + id));
        model.addAttribute("status", status);
        return "statuses/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateStatus(@PathVariable Long id,
                               @ModelAttribute("status") Status updatedStatus,
                               @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Status existingStatus = statusService.getStatusByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Status Id: " + id));

        if (!existingStatus.getName().equals(updatedStatus.getName()) &&
                statusService.getStatusByNameAndUser(updatedStatus.getName(), user).isPresent()) {
            return "redirect:/statuses?error=exists";
        }

        existingStatus.setName(updatedStatus.getName());
        statusService.saveStatus(existingStatus);
        return "redirect:/statuses";
    }

    @GetMapping("/delete/{id}")
    public String deleteStatus(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Status status = statusService.getStatusByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Status Id: " + id));

        statusService.deleteStatus(status);
        return "redirect:/statuses";
    }
}