package com.example.mvcsecurityjpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * User
 */
@Entity
@Table(name = "person")
public class User {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "username", unique=true)
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "rolename")
  private String rolename;


  public User() {}

  public User(String username, String password, String rolename) {
    this.username = username;
    this.password = password;
    this.rolename = rolename;
  }

  @Override
  public String toString() {
    return String.format("Username : %s\nRole: %s\n", username, rolename);
  }

  /*
   * Getters and Setter
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

  public String getRolename() {
    return rolename;
  }

  public void setRolename(String rolename) {
    this.rolename = rolename;
  }
}
