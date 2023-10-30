package com.example.mvcsecurityjpa.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mvcsecurityjpa.entity.User;
import com.example.mvcsecurityjpa.form.user.UserLoginForm;
import com.example.mvcsecurityjpa.form.user.UserRegistrationForm;
import com.example.mvcsecurityjpa.helper.AuthenticationHelper;
import com.example.mvcsecurityjpa.service.user.UserLogoutService;
import com.example.mvcsecurityjpa.service.user.UserSignupService;
import com.example.mvcsecurityjpa.service.user.UserSearchService;

import jakarta.validation.Valid;

/**
 * UserController
 */
@RestController
// @RequestMapping("/api/v1")
public class UserController {

  private UserSignupService userSignupService;
  private UserLogoutService userLogoutService;
  private UserSearchService userSearchService;
  private AuthenticationHelper authenticationHelper;
  // private Logger log = LoggerFactory.getLogger(AccountController.class);


  public UserController(UserSignupService userSignupService,
                        UserLogoutService userLogoutService,
                        UserSearchService userSearchService,
                        AuthenticationHelper authenticationHelper) {
    this.userSignupService = userSignupService;
    this.userLogoutService = userLogoutService;
    this.userSearchService = userSearchService;
    this.authenticationHelper = authenticationHelper;

  }


  @GetMapping("/")
  public String showTop() {
    return "redirect:/boards";
  }


  @GetMapping("/login")
  public ResponseEntity<?> showLoginPage() {
    // return authenticationHelper.redirectIfLoggedIn("redirect:/boards", "user/login");
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody UserLoginForm form, BindingResult result) {
    System.out.println("POST /login requested");
    System.out.println(form);

    if (result.hasErrors()) {
      System.out.println("Binding Result Failed...");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {
      // userSignupService.signup(form.getUsername(), form.getEmail(), form.getPassword());
      //  temporary code
      Authentication auth = authenticationHelper.authenticate(form);
      authenticationHelper.setAuthentication(auth);
    } catch (Exception e) {
      System.out.println("Singup Failed : " + e.getMessage());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @GetMapping("/signup")
  public String showSignup(@ModelAttribute("form") UserRegistrationForm form) {
    return authenticationHelper.redirectIfLoggedIn("redirect:/boards", "user/signup");
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@Valid @RequestBody UserRegistrationForm form, BindingResult result, RedirectAttributes redirectAttributes) {
    System.out.println("POST /signup requested");
    System.out.println(form);


    if (result.hasErrors()) {
      System.out.println("Binding Result Failed...");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {
      userSignupService.signup(form.getUsername(), form.getEmail(), form.getPassword());
    } catch (Exception e) {
      System.out.println("Singup Failed : " + e.getMessage());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @PostMapping("/logout")
  public String logout() {
    try {
      userLogoutService.logout(authenticationHelper.getAuthenticatedUserDetails().getUser().getId());
      return "redirect:/login";
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "redirect:/login";
    }
  }

  @GetMapping("/search")
  public String searchUser(@RequestParam("query") String query, @RequestHeader(value = "referer", required = false) final String referer, Model model) {
    // if (query.isEmpty()) {
    //   return "redirect:" + referer;
    // }

    List<User> userList= userSearchService.searchUser(query);
    model.addAttribute("userList", userList);
    return "user/search-result";
  }
}