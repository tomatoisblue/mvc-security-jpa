package com.example.mvcsecurityjpa.form.board;

import com.example.mvcsecurityjpa.entity.User;

import jakarta.validation.constraints.Size;

/**
 * BaseBoardForm
 */
public class BaseBoardForm  {

  private User user;

  @Size(min = 1, max = 30)
  private String title;


  /*
   * Getters and Setters
   */

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getTitle() {
    if (title != null){
      title.strip();
    }
    return title;
  }
  public void setTitle(String title) {
    if (title != null){
      title.strip();
    }
    this.title = title;
  }
}