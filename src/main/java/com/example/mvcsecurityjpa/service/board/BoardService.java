package com.example.mvcsecurityjpa.service.board;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.entity.Board;
import com.example.mvcsecurityjpa.form.board.BoardEditForm;
import com.example.mvcsecurityjpa.form.board.BoardCreationForm;
import com.example.mvcsecurityjpa.repository.BoardRepository;

/**
 * BoardService
 */
@Service
public class BoardService {
  private BoardRepository boardRepository;

  public BoardService(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  public Board save(BoardCreationForm form) {
    form.getTitle().strip();
    Board board = new Board(form.getTitle(), form.getUser());

    return boardRepository.save(board);
  }

  public Board update(BoardEditForm form) {
    form.getTitle().strip();
    Optional<Board> optionalBoard = boardRepository.findById(form.getBoardId());
    if (optionalBoard.isEmpty()) {
      return null;
    }

    Board board = (Board) optionalBoard.get();
    board.setTitle(form.getTitle());
    return boardRepository.save(board);
  }

  public List<Board> findAllByUserId(Long boardId) {
    return boardRepository.findAllByUserId(boardId);
  }

  // public void changeTitle(BoardEditForm form) {
  //   Optional<Board> optionalBoard = boardRepository.findById(form.getBoardId());
  //   if (optionalBoard.isEmpty()) {
  //     return;
  //   }

  //   Board board = optionalBoard.get();
  //   board.setTitle(form.getTitle());
  //   boardRepository.save(board);
  // }

  public void deleteBoard(Long boardId) {
    Optional<Board> optionalBoard = boardRepository.findById(boardId);
    if (optionalBoard.isEmpty()) {
      return;
    }
    boardRepository.delete(optionalBoard.get());
  }
}