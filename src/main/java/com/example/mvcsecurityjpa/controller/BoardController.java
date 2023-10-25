package com.example.mvcsecurityjpa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mvcsecurityjpa.entity.Board;
import com.example.mvcsecurityjpa.entity.Task;
import com.example.mvcsecurityjpa.form.BoardEditForm;
import com.example.mvcsecurityjpa.form.BoardForm;
import com.example.mvcsecurityjpa.service.board.BoardService;
import com.example.mvcsecurityjpa.service.task.TaskService;
import com.example.mvcsecurityjpa.userDetails.CustomUserDetails;

import jakarta.validation.Valid;

/**
 * BoardController
 */
@Controller
public class BoardController {
  private BoardService boardService;
  private TaskService taskService;
  private AuthenticationHelper authenticationHelper;

  BoardController(BoardService boardService,
                  TaskService taskService,
                  AuthenticationHelper authenticationHelper) {
    this.boardService = boardService;
    this.taskService = taskService;
    this.authenticationHelper = authenticationHelper;
  }

  @GetMapping("/boards")
  public String showAllBoards(Model model) {
    CustomUserDetails user = authenticationHelper.getAuthenticatedUserDetails();

    List<Board> boards = boardService.findAllByUserId(user.getId());

    model.addAttribute("username", user.getUsername());
    model.addAttribute("boards", boards);

    return "board/boards";
  }

  @GetMapping("/boards/{id}")
  public String showBoardDetails(@PathVariable Long id, Model model) {
    Board board = boardService.findById(id);
    if (board == null) {
      return "redirect:/boards";
    }
    List<Task> tasks = taskService.findAllByBoardId(id);

    model.addAttribute("board", board);
    model.addAttribute("tasks", tasks);
    return "board/board-details";
  }

  @GetMapping("/board/new")
  public String showBoardCreation(@ModelAttribute("form") BoardForm form) {
    return "board/board-creation";
  }


  @PostMapping("/board/new")
  public String createBoard(@Valid @ModelAttribute("form") BoardForm form, RedirectAttributes redirectAttributes) {
    CustomUserDetails userDetails = authenticationHelper.getAuthenticatedUserDetails();

    form.setUser(userDetails.getUser());
    Long boardId = boardService.save(form).getId();

    // redirect to board details page based on id
    redirectAttributes.addAttribute("id", boardId);
    return "redirect:/boards/{id}";
  }


  @GetMapping("/boards/{id}/edit")
  public String showBoardEdit(@ModelAttribute BoardEditForm form, @PathVariable("id") Long id, Model model) {
    Board board = boardService.findById(id);
    if (board == null) {
      return "redirect:/boards";
    }
    model.addAttribute(board);
    return "board/board-edit";
  }

  @PostMapping("/boards/{id}/edit")
  public String updateBoard(@Valid @PathVariable Long id, @Valid @ModelAttribute("form") BoardEditForm form, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      model.addAttribute("form", form);
      return "board/board-edit";
    }

    form.setId(id);
    boardService.changeTitle(form);

    redirectAttributes.addAttribute("id", id);
    return "redirect:/boards/{id}";
  }


  @PostMapping("/boards/{id}/delete")
  public String DeleteBoard(@PathVariable Long id) {
    boardService.deleteBoard(id);
    return "redirect:/boards";
  }


}