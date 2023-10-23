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
import com.example.mvcsecurityjpa.service.UserDeletionService;
import com.example.mvcsecurityjpa.service.UserRegistrationService;

import jakarta.validation.Valid;

/**
 * AccountController
 */
@Controller
public class AccountController {

  private UserRegistrationService userRegistrationService;
  private UserDeletionService userDeletionService;
  private Logger log = LoggerFactory.getLogger(AccountController.class);


  AccountController(
    UserRegistrationService userRegistrationService,
    UserDeletionService userDeletionService) {
    this.userRegistrationService = userRegistrationService;
    this.userDeletionService = userDeletionService;
  }

  @GetMapping("/")
  public String showTop() {
    return "index";
  }

  @GetMapping("/profile")
  public String showProfile(Model model) {
    UserDetails user = getAuthenticatedUserDetails();

    model.addAttribute("username", user.getUsername());
    return "profile";
  }

  @GetMapping("/login")
  public String showLoginPage() {
    return redirectIfLoggedIn("redirect:/profile", "login");
  }

  @GetMapping("/user/registration")
  public String showUserRegistration(@ModelAttribute("form") UserRegistrationForm form) {
    return redirectIfLoggedIn("redirect:/profile", "user-resistration");
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

  @PostMapping("/logout")
  public String deleteUser() {
    UserDetails user = getAuthenticatedUserDetails();
    userDeletionService.deleteUser(user.getUsername());

    return "redirect:/user/registration";
  }

  private boolean isLoggedIn() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof UserDetails) {
      return true;
    }
    return false;
  }

  private String redirectIfLoggedIn(String viewIfLoggedIn, String viewIfNotLoggedIn) {
    if (isLoggedIn()) {
      return viewIfLoggedIn;
    }
    return viewIfNotLoggedIn;
  }

  private static UserDetails getAuthenticatedUserDetails() {
    return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}