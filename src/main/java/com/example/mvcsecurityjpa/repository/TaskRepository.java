package com.example.mvcsecurityjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mvcsecurityjpa.entity.Task;
import java.util.List;

/**
 * TaskRepository
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  public List<Task> findAllByBoardId(Long boardId);


}