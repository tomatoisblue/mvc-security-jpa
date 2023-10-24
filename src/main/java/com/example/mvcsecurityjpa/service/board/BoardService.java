package com.example.mvcsecurityjpa.service.board;

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.entity.Board;
import com.example.mvcsecurityjpa.repository.BoardRepository;

/**
 * BoardService
 */
@Service
public class BoardService {
  private final BoardRepository boardRepository;

  public BoardService(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  public Board save(Board board) {
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


  public void changeTitle(String title, Board board) {
    board.setTitle(title);
    boardRepository.save(board);
  }

}