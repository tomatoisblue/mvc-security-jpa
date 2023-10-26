package com.example.mvcsecurityjpa.service.task;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.entity.Task;
import com.example.mvcsecurityjpa.repository.TaskRepository;


/**
 * TaskFindService
 */
@Service
public class TaskFindService {
  private TaskRepository taskRepository;

  public TaskFindService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }


  // public List<Task> findAllByBoardId(Long boardId) {
  //   return taskRepository.findAllByBoardId(boardId);
  // }

  public Task findById(Long taskId) {
    Optional<Task> optionalTask = taskRepository.findById(taskId);
    if (optionalTask.isEmpty()) {
      return null;
    }
    return optionalTask.get();
  }



}