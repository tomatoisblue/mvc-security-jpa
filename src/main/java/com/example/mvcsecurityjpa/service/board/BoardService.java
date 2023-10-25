package com.example.mvcsecurityjpa.service.board;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.entity.Board;
import com.example.mvcsecurityjpa.form.BoardEditForm;
import com.example.mvcsecurityjpa.form.BoardForm;
import com.example.mvcsecurityjpa.repository.BoardRepository;
import com.example.mvcsecurityjpa.service.task.TaskService;

/**
 * BoardService
 */
@Service
public class BoardService {
  private final BoardRepository boardRepository;
  private final TaskService taskService;

  public BoardService(BoardRepository boardRepository,
                      TaskService taskService) {
    this.boardRepository = boardRepository;
    this.taskService = taskService;
  }

  public Board save(BoardForm form) {
    Board board = new Board(form.getTitle(), form.getUser());

    return boardRepository.save(board);
  }

  public Board update(BoardEditForm form) {
    Optional<Board> optionalBoard = boardRepository.findById(form.getId());
    if (optionalBoard.isEmpty()) {
      return null;
    }

    Board board = (Board) optionalBoard.get();
    board.setTitle(form.getTitle());
    return boardRepository.save(board);
  }

  public Board findById(Long id) {
    Optional<Board> optionalBoard = boardRepository.findById(id);
    if(optionalBoard.isPresent()) {
      return optionalBoard.get();
    }
    return null;
  }

  public List<Board> findAllByUserId(Long id) {
    return boardRepository.findAllByUserId(id);
  }


  public void changeTitle(BoardEditForm form) {
    Board board = findById(form.getId());
    if (board == null) {
      return;
    }

    board.setTitle(form.getTitle());
    boardRepository.save(board);
  }

  public void deleteBoard(Long boardId) {
    Board board = findById(boardId);
    if (board == null) {
      return;
    }

    if (board.isTasksExist()) {
      taskService.deleteAllByBoardId(boardId);
    }

    boardRepository.delete(board);
  }
}