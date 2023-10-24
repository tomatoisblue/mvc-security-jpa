package com.example.mvcsecurityjpa.service.task;

import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.entity.Task;
import com.example.mvcsecurityjpa.repository.TaskRepository;

/**
 * TaskService
 */
@Service
public class TaskService {
  private TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public void Save(Task task) {
    taskRepository.save(task);
  }


}