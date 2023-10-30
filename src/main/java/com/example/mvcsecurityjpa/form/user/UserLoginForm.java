package com.example.mvcsecurityjpa.form.user;

// import com.example.mvcsecurityjpa.annotation.UniqueEmail;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Pattern;
// import jakarta.validation.constraints.Size;



/**
 * UserLoginForm
 */
public class UserLoginForm {
  // @NotBlank
  // @Email
  private String email;

  // @Size(min = 8, max = 30)
  // @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,30}$", message = "パスワードには英小文字、英大文字、数字、記号から最低1つずつ使用してください")
  private String password;



  @Override
  public String toString() {
    return String.format("email: %s\npassword: %s\n",
      email,
      password
    );
  }

  /*
   * Getters and Setters
   */

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}