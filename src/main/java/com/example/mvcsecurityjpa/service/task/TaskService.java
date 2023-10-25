package com.example.mvcsecurityjpa.service.task;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.entity.Board;
import com.example.mvcsecurityjpa.entity.Task;
import com.example.mvcsecurityjpa.form.TaskForm;
import com.example.mvcsecurityjpa.repository.TaskRepository;
import com.example.mvcsecurityjpa.service.board.BoardService;

/**
 * TaskService
 */
@Service
public class TaskService {
  private TaskRepository taskRepository;
  private BoardService boardService;

  public TaskService(TaskRepository taskRepository,
                     BoardService boardService) {
    this.taskRepository = taskRepository;
    this.boardService = boardService;
  }

  public Long save(TaskForm form) {
    Board board = boardService.findById(form.getBoardId());
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

    return taskRepository.save(task).getId();
  }

  public List<Task> findAllByBoardId(Long boardId) {
    return taskRepository.findAllByBoardId(boardId);
  }

  public Task findById(Long taskId) {
    Optional<Task> optionalTask = taskRepository.findById(taskId);
    if (optionalTask == null) {
      return null;
    }

    return optionalTask.get();
  }

  public void deleteAllByBoardId(Long boardId) {
    List<Task> tasks = taskRepository.findAllByBoardId(boardId);

    taskRepository.deleteAllInBatch(tasks);
  }

}