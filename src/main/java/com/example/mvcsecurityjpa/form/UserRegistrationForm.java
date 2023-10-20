package com.example.mvcsecurityjpa.form;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * UserRegistrationForm
 */
public class UserRegistrationForm {
  @NotBlank
  @Size(min = 4, max = 20)
  private String username;

  @NotBlank
  @Size(min = 8, max = 30)
  private String password;

  @NotBlank
  @Size(min = 8, max = 30)
  private String confirmPassword;

  @AssertTrue
  public boolean isPasswordValid() {
    if (password == null || confirmPassword == null) {
      return false;
    }
    return password.equals(confirmPassword);
  }

  /*
   * Getters and Setters
   */

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String password) {
    this.confirmPassword = password;
  }
}