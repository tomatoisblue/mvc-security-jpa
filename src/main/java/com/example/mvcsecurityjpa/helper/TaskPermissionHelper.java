package com.example.mvcsecurityjpa.helper;

import org.springframework.stereotype.Component;

import com.example.mvcsecurityjpa.entity.Task;
import com.example.mvcsecurityjpa.entity.User;
import com.example.mvcsecurityjpa.service.task.TaskFindService;


/**
 * TaskPermissionHelper
 */
@Component
public class TaskPermissionHelper {
  private AuthenticationHelper authenticationHelper;
  private TaskFindService taskFindService;

  public TaskPermissionHelper(AuthenticationHelper authenticationHelper,
                              TaskFindService taskFindService) {
    this.authenticationHelper = authenticationHelper;
    this.taskFindService = taskFindService;
  }

  public boolean isOwner(Long taskId) {
    Task task = taskFindService.findById(taskId);
    if (task == null) return false;
    if (!authenticationHelper.isLoggedIn()) return false;

    User user = authenticationHelper.getAuthenticatedUserDetails().getUser();
    System.out.println("task owner id: " + task.getUser().getId());
    System.out.println("current user id: " + user.getId());
    if (user.getId() != task.getUser().getId()) return false;

    return true;
  }

  public Long getTaskOwnerId(Long taskId) {
    Task task = taskFindService.findById(taskId);
    if (task == null) return null;
    if (!authenticationHelper.isLoggedIn()) return null;

    return authenticationHelper.getAuthenticatedUserDetails().getUser().getId();
  }





}