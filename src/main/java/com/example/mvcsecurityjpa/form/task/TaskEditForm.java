package com.example.mvcsecurityjpa.form.task;

import java.time.LocalDate;

import com.example.mvcsecurityjpa.enums.Status;

import jakarta.validation.constraints.Size;



/**
 * TaskForm
 */
public class TaskEditForm extends BaseTaskForm{
  private Long taskId;


  /*
   * Getters and Setters
   */

  public Long getTaskId() {
    return taskId;
  }

  public void setTaskId(Long taskId) {
    this.taskId = taskId;
  }


  public boolean isStatusSelected(Status status) {
    if(getStatus() == status) {
      return true;
    }
    return false;
  }

}