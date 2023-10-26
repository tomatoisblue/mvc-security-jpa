package com.example.mvcsecurityjpa.service.board;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.mvcsecurityjpa.entity.Board;
import com.example.mvcsecurityjpa.repository.BoardRepository;

/**
 * BoardService
 */
@Service
public class BoardFindService {
  private final BoardRepository boardRepository;

  public BoardFindService(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  public Board findByBoardId(Long boardId) {
    Optional<Board> optionalBoard = boardRepository.findById(boardId);
    if(optionalBoard.isPresent()) {
      return optionalBoard.get();
    }
    return null;
  }
}