package com.example.mvcsecurityjpa.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mvcsecurityjpa.entity.Board;
import com.example.mvcsecurityjpa.form.BoardForm;
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
public class AccountController {

  private UserRegistrationService userRegistrationService;
  private UserDeletionService userDeletionService;
  private BoardService boardService;
  private Logger log = LoggerFactory.getLogger(AccountController.class);


  public AccountController(
    UserRegistrationService userRegistrationService,
    UserDeletionService userDeletionService,
    BoardService boardService) {
    this.userRegistrationService = userRegistrationService;
    this.userDeletionService = userDeletionService;
    this.boardService = boardService;
  }

  @GetMapping("/")
  public String showTop() {
    return "index";
  }

  @GetMapping("/profile")
  public String showProfile(Model model) {
    CustomUserDetails user = getAuthenticatedUserDetails();

    List<Board> boards = boardService.findAllByUserId(user.getId());

    model.addAttribute("username", user.getUsername());
    model.addAttribute("boards", boards);
    return "profile";
  }

  @GetMapping("/board/new")
  public String showBoardCreationPage(@ModelAttribute("form") BoardForm form) {
    return "board-creation";
  }

  @PostMapping("/board/new")
  public String createBoard(@Valid @ModelAttribute("form") BoardForm form, RedirectAttributes redirectAttributes) {
    CustomUserDetails userDetails = getAuthenticatedUserDetails();
    Board board = new Board(form.getTitle(), userDetails.getUser());
    Long boardId = boardService.save(board).getId();

    // redirect to board details page based on id
    redirectAttributes.addAttribute("id", boardId);
    return "redirect:/boards/{id}";
  }

  @GetMapping("/boards/{id}")
  public String showBoardDetails(@PathVariable Long id, Model model) {
    Board board = boardService.findById(id);
    if (board == null) {
      return "redirect:/profile";
    }

    model.addAttribute("board", board);
    return "board-details";
  }

  @GetMapping("/login")
  public String showLoginPage() {
    return redirectIfLoggedIn("redirect:/profile", "login");
  }

  @GetMapping("/user/registration")
  public String showUserRegistration(@ModelAttribute("form") UserRegistrationForm form) {
    return redirectIfLoggedIn("redirect:/profile", "user-registration");
  }

  @PostMapping("/user/registration")
  public String userRegistration(@Valid @ModelAttribute("form") UserRegistrationForm form, BindingResult result) {
    if (result.hasErrors()) {
      return "user-registration";
    }
    // Register the user to the database.
    userRegistrationService.userRegistration(form.getUsername(), form.getEmail(), form.getPassword());

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

  private static CustomUserDetails getAuthenticatedUserDetails() {
    return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}