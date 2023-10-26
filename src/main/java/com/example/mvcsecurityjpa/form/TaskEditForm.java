package com.example.mvcsecurityjpa.form;

import java.time.LocalDate;

import com.example.mvcsecurityjpa.enums.Status;

import jakarta.validation.constraints.Size;



/**
 * TaskForm
 */
public class TaskEditForm {
  private Long id;
  private Long boardId;
  private Long userId;

  @Size(min = 1, max = 30)
  private String title;

  private Status status;

  @Size(max = 512)
  private String description;

  private LocalDate expirationDate;

  @Size(max = 512)
  private String url;

  @Override
  public String toString(){
    return String.format("id: %d\ntitle: %s\nstatus: %s\nboardId: %d", id, title, status, boardId);
  }

  /*
   * Getters and Setters
   */

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDate expirationDate) {
    this.expirationDate = expirationDate;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Long getBoardId() {
    return boardId;
  }

  public void setBoardId(Long boardId) {
    this.boardId = boardId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public boolean isStatusSelected(Status status) {
    if(this.status == status) {
      return true;
    }
    return false;
  }

}