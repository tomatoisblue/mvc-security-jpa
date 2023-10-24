package com.example.mvcsecurityjpa.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.List;

/**
 * Board
 */
@Entity
@Table(name = "board")
public class Board {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title", unique = false, nullable = false)
  private String title;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @OneToMany(mappedBy = "board")
  private List<Task> tasks;

  @CreationTimestamp
  @Column(name = "created_on")
  private Instant createdOn;

  @UpdateTimestamp
  @Column(name = "updated_on")
  private Instant updatedOn;

  // Constructors

  public Board(){}

  public Board(String title, User user) {
    this.title = title;
    this.user = user;
  }

  // Getters and Setters

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    setUpdatedOn();
    this.title = title;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  // update updatedOn
  private void setUpdatedOn() {
    this.updatedOn = Instant.now();
  }

}