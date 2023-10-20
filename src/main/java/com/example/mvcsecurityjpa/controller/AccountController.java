package com.example.mvcsecurityjpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * AccountController
 */
@Controller
public class AccountController {

  @GetMapping("/user/registration")
  public String showUserRegistration() {
    return "user-registration";
  }


}