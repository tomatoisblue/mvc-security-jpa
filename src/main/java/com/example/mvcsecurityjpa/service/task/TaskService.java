package com.example.mvcsecurityjpa.service.task;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.entity.Board;
import com.example.mvcsecurityjpa.entity.Task;
import com.example.mvcsecurityjpa.enums.Status;
import com.example.mvcsecurityjpa.form.TaskEditForm;
import com.example.mvcsecurityjpa.form.TaskForm;
import com.example.mvcsecurityjpa.helper.AuthenticationHelper;
import com.example.mvcsecurityjpa.repository.TaskRepository;
import com.example.mvcsecurityjpa.service.board.BoardFindService;


/**
 * TaskService
 */
@Service
public class TaskService {
  private TaskRepository taskRepository;
  private BoardFindService boardFindService;
  private AuthenticationHelper authenticationHelper;

  public TaskService(TaskRepository taskRepository,
                     BoardFindService boardFindService,
                     AuthenticationHelper authenticationHelper) {
    this.taskRepository = taskRepository;
    this.boardFindService = boardFindService;
    this.authenticationHelper = authenticationHelper;
  }

  public Long save(TaskForm form) {
    Board board = boardFindService.findByBoardId(form.getBoardId());

    if (board == null) {
      return null;
    }

    Task task = new Task();
    task.setTitle(form.getTitle());
    task.setStatus(form.getStatus());
    task.setDescription(form.getDescription());
    task.setExpirationDate(form.getExpirationDate());
    task.setUrl(form.getUrl());
    task.setBoard(board);
    task.setUser(authenticationHelper.getCurrentUser());

    return taskRepository.save(task).getId();
  }

  public List<Task> findAllByBoardId(Long boardId) {
    return taskRepository.findAllByBoardId(boardId);
  }

  // public Task findById(Long taskId) {
  //   Optional<Task> optionalTask = taskRepository.findById(taskId);
  //   if (optionalTask == null) {
  //     return null;
  //   }
  //   return optionalTask.get();
  // }

  public Map<Status, List<Task>> groupByStatus(List<Task> tasks) {
    Map<Status, List<Task>> groupedTasks =
      tasks.stream()
           .collect(Collectors.groupingBy(task -> task.getStatus()));

    return groupedTasks;
  }

  public Task update(TaskEditForm form) {
    Optional<Task> optionalTask = taskRepository.findById(form.getId());
    if (optionalTask.isEmpty()) {
      System.out.println("task not found");
      return null;
    }
    Task task = optionalTask.get();

    Board board = boardFindService.findByBoardId(form.getBoardId());
    if (board == null) {
      System.out.println("board not found");
      return null;
    }

    System.out.println("task title : " + task.getTitle());
    System.out.println("form title : " + form.getTitle());
    task.setTitle(form.getTitle());
    task.setStatus(form.getStatus());
    task.setDescription(form.getDescription());
    task.setExpirationDate(form.getExpirationDate());
    task.setUrl(form.getUrl());
    task.setBoard(board);
    task.setUser(authenticationHelper.getCurrentUser());

    return taskRepository.save(task);
  }

  // returns Board ID
  public Long deleteTask(Long taskId) {
    Optional<Task>  optionalTask = taskRepository.findById(taskId);
    if (optionalTask.isEmpty()) {
      return null;
    }
    Long boardId = optionalTask.get().getBoard().getId();
    try {
      taskRepository.deleteById(taskId);
      return boardId;
    } catch (Exception e) {
      return null;
    }
  }

}