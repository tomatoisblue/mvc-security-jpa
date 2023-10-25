package com.example.mvcsecurityjpa.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mvcsecurityjpa.entity.Board;
import com.example.mvcsecurityjpa.form.UserRegistrationForm;
import com.example.mvcsecurityjpa.service.board.BoardService;
import com.example.mvcsecurityjpa.service.user.UserDeletionService;
import com.example.mvcsecurityjpa.service.user.UserRegistrationService;
import com.example.mvcsecurityjpa.userDetails.CustomUserDetails;

import jakarta.validation.Valid;

/**
 * AccountController
 */
@Controller
public class UserController {

  private UserRegistrationService userRegistrationService;
  private UserDeletionService userDeletionService;
  private AuthenticationHelper authenticationHelper;
  // private Logger log = LoggerFactory.getLogger(AccountController.class);


  public UserController(UserRegistrationService userRegistrationService,
                        UserDeletionService userDeletionService,
                        AuthenticationHelper authenticationHelper) {
    this.userRegistrationService = userRegistrationService;
    this.userDeletionService = userDeletionService;
    this.authenticationHelper = authenticationHelper;
  }


  @GetMapping("/")
  public String showTop() {
    return "index";
  }


  @GetMapping("/login")
  public String showLoginPage() {
    return authenticationHelper.redirectIfLoggedIn("redirect:/boards", "user/login");
  }

  @GetMapping("/signup")
  public String showSignup(@ModelAttribute("form") UserRegistrationForm form) {
    return authenticationHelper.redirectIfLoggedIn("redirect:/boards", "user/signup");
  }

  @PostMapping("/signup")
  public String signup(@Valid @ModelAttribute("form") UserRegistrationForm form, BindingResult result, RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      return "user/signup";
    }
    // Register the user to the database.
    userRegistrationService.userRegistration(form.getUsername(), form.getEmail(), form.getPassword());

    return "redirect:/login";
  }

  @PostMapping("/logout")
  public String deleteUser() {
    UserDetails user = authenticationHelper.getAuthenticatedUserDetails();
    userDeletionService.deleteUser(user.getUsername());

    return "redirect:/login";
  }

}