package com.example.mvcsecurityjpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mvcsecurityjpa.entity.Task;
import com.example.mvcsecurityjpa.enums.Status;
import com.example.mvcsecurityjpa.form.TaskForm;
import com.example.mvcsecurityjpa.service.task.TaskService;

import jakarta.validation.Valid;

/**
 * TaskController
 */
@Controller
public class TaskController {
  private TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping("/tasks/{taskId}")
  public String showTaskDetails(@PathVariable Long taskId, Model model) {
    Task task = taskService.findById(taskId);
    model.addAttribute("task", task);
    return "task/task-details";
  }

  @GetMapping("/tasks/{boardId}/new")
  public String showTaskCreation(@ModelAttribute TaskForm form, @PathVariable Long boardId, Model model) {
    model.addAttribute("boardId", boardId);
    model.addAttribute("form", form);
    model.addAttribute("statusList", Status.values());

    return "task/task-creation";
  }

  @PostMapping("/tasks/{boardId}")
  public String createTask(@Valid @ModelAttribute TaskForm form, BindingResult result, @PathVariable Long boardId, Model model, RedirectAttributes redirectAttributes) {
    if(result.hasErrors()) {
      model.addAttribute("boardId", boardId);
      model.addAttribute("form", form);
      model.addAttribute("statusList", Status.values());
      return "task/task-creation";
    }

    form.setBoardId(boardId);

    Long taskId = taskService.save(form);

    redirectAttributes.addAttribute("boardId", boardId);
    redirectAttributes.addAttribute("taskId", taskId);

    return "redirect:/tasks/{taskId}";
  }



}