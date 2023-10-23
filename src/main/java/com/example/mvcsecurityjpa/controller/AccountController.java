package com.example.mvcsecurityjpa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.mvcsecurityjpa.form.UserRegistrationForm;
import com.example.mvcsecurityjpa.service.UserRegistrationService;
import com.example.mvcsecurityjpa.userDetails.UserDetailsImpl;

import jakarta.validation.Valid;

/**
 * AccountController
 */
@Controller
public class AccountController {

  private UserRegistrationService userRegistrationService;
  private Logger log = LoggerFactory.getLogger(AccountController.class);


  AccountController(UserRegistrationService userRegistrationService) {
    this.userRegistrationService = userRegistrationService;
  }

  @GetMapping("/")
  public String showTop() {
    return "index";
  }

  @GetMapping("/profile")
  public String showProfile(Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl) {
      UserDetails user = (UserDetails) auth.getPrincipal();
      if (user != null) {
        model.addAttribute("username", user.getUsername());
        System.out.println("Username : " + user.getUsername());
      } else {
        log.info("USER NOT FOUND");
      }
    }
    return "profile";
  }

  @GetMapping("/login")
  public String showLoginPage() {
    return "login";
  }

  @GetMapping("/user/registration")
  public String showUserRegistration(@ModelAttribute("form") UserRegistrationForm form) {
    return "user-registration";
  }

  @PostMapping("/user/registration")
  public String userRegistration(@Valid @ModelAttribute("form") UserRegistrationForm form, BindingResult result) {
    if (result.hasErrors()) {
      return "user-registration";
    }
    // Register the user to the database.
    userRegistrationService.userRegistration(form.getUsername(), form.getPassword());
    return "redirect:/login";
  }
}