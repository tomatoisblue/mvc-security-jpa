package com.example.mvcsecurityjpa.form;

import com.example.mvcsecurityjpa.entity.User;

import jakarta.validation.constraints.Size;

/**
 * BoardEditForm
 */
public class BoardEditForm  {
  private Long id;

  private User user;

  @Size(min = 1, max = 30)
  private String title;
  /*
   * Getters and Setters
   */

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}