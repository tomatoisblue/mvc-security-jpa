package com.example.mvcsecurityjpa.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * EmailNotFoundException
 */
public class EmailNotFoundException extends UsernameNotFoundException {

  public EmailNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}