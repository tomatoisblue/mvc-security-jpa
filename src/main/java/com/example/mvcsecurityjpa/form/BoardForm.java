package com.example.mvcsecurityjpa.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * BoardForm
 */
public class BoardForm  {

  @Size(min = 1, max = 30)
  private String title;


  /*
   * Getters and Setters
   */

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}