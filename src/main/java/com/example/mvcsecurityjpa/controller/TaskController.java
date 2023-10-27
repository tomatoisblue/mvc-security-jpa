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
import com.example.mvcsecurityjpa.form.task.TaskEditForm;
import com.example.mvcsecurityjpa.form.task.TaskCreationForm;
import com.example.mvcsecurityjpa.helper.AuthenticationHelper;
import com.example.mvcsecurityjpa.helper.BoardPermissionHelper;
import com.example.mvcsecurityjpa.helper.TaskPermissionHelper;
import com.example.mvcsecurityjpa.service.task.TaskFindService;
import com.example.mvcsecurityjpa.service.task.TaskService;

import jakarta.validation.Valid;

/**
 * TaskController
 */
@Controller
public class TaskController {
  private TaskService taskService;
  private TaskFindService taskFindService;
  private AuthenticationHelper authentiationHelper;
  private TaskPermissionHelper taskPermissionHelper;
  private BoardPermissionHelper boardPermissionHelper;

  public TaskController(TaskService taskService,
                        TaskFindService taskFindService,
                        AuthenticationHelper authenticationHelper,
                        TaskPermissionHelper taskPermissionHelper,
                        BoardPermissionHelper boardPermissionHelper) {
    this.taskService = taskService;
    this.taskFindService = taskFindService;
    this.authentiationHelper = authenticationHelper;
    this.taskPermissionHelper = taskPermissionHelper;
    this.boardPermissionHelper = boardPermissionHelper;
  }

  @GetMapping("/tasks/{taskId}")
  public String showTaskDetails(@PathVariable Long taskId, Model model) {
    if (!taskPermissionHelper.isOwner(taskId)) {
      return "redirect:/boards";
    }

    Task task = taskFindService.findById(taskId);
    model.addAttribute("task", task);
    return "task/task-details";
  }

  @GetMapping("/tasks/{boardId}/new")
  public String showTaskCreation(@ModelAttribute TaskCreationForm form, @PathVariable Long boardId, Model model) {
    if (!boardPermissionHelper.isOwner(boardId)) {
      return "redirect:/boards";
    }
    model.addAttribute("boardId", boardId);
    model.addAttribute("form", form);
    model.addAttribute("statusList", Status.values());

    return "task/task-creation";
  }

  @PostMapping("/tasks/{boardId}")
  public String createTask(@Valid @ModelAttribute TaskCreationForm form, BindingResult result, @PathVariable Long boardId, Model model, RedirectAttributes redirectAttributes) {
    if (!boardPermissionHelper.isOwner(boardId)) {
      return "redirect:/boards";
    }

    if(result.hasErrors()) {
      model.addAttribute("boardId", boardId);
      model.addAttribute("form", form);
      model.addAttribute("statusList", Status.values());
      return "task/task-creation";
    }


    form.setBoardId(boardId);
    form.setUserId(authentiationHelper.getCurrentUserId());
    Long taskId = taskService.save(form);


    redirectAttributes.addAttribute("boardId", boardId);
    redirectAttributes.addAttribute("taskId", taskId);

    return "redirect:/tasks/{taskId}";
  }

  @GetMapping("/tasks/{taskId}/edit")
  public String showTaskEdit(@ModelAttribute TaskEditForm form, @PathVariable("taskId") Long taskId, Model model) {
    if (!taskPermissionHelper.isOwner(taskId)) {
      return "redirect:/boards";
    }

    Task task = taskFindService.findById(taskId);
    if (task == null) {
      return "redirect:/boards";
    }

    form.setTaskId(task.getId());
    form.setTitle(task.getTitle());
    form.setStatus(task.getStatus());
    form.setDescription(task.getDescription());
    form.setExpirationDate(task.getExpirationDate());
    form.setUrl(task.getUrl());
    form.setBoardId(task.getBoard().getId());

    System.out.println(form);


    model.addAttribute("statusList", Status.values());
    model.addAttribute("form", form);
    return "task/task-edit";
  }

  @PostMapping("/tasks/{taskId}/edit")
  public String updateTask(@Valid @PathVariable Long taskId, @Valid @ModelAttribute("form") TaskEditForm form, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
    if (!taskPermissionHelper.isOwner(taskId)) {
      return "redirect:/boards";
    }

    if (result.hasErrors()) {
      model.addAttribute("statusList", Status.values());
      model.addAttribute("form", form);
      return "task/task-edit";
    }
    form.setUserId(authentiationHelper.getCurrentUserId());
    Task task = taskService.update(form);

    if (task == null) {
      return "redirect:/boards";
    }

    redirectAttributes.addAttribute("taskId", task.getId());
    return "redirect:/tasks/{taskId}";
  }

  @PostMapping("tasks/{taskId}/delete")
  public String deleteTask(@PathVariable Long taskId, RedirectAttributes redirectAttributes) {
    if (!taskPermissionHelper.isOwner(taskId)) {
      return "redirect:/boards";
    }

    Long boardId = taskService.deleteTask(taskId);

    redirectAttributes.addAttribute("boardId", boardId);
    return "redirect:/boards/{boardId}";
  }
}