package com.example.mvcsecurityjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mvcsecurityjpa.entity.Board;
import java.util.List;

/**
 * BoardRepository
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {


  public List<Board> findAllByUserId(Long userId);

}