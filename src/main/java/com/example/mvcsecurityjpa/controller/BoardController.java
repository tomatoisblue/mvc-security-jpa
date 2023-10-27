package com.example.mvcsecurityjpa.controller;

import java.util.List;
import java.util.Map;

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
import com.example.mvcsecurityjpa.enums.Status;
import com.example.mvcsecurityjpa.form.board.BoardEditForm;
import com.example.mvcsecurityjpa.form.board.BoardCreationForm;
import com.example.mvcsecurityjpa.service.board.BoardFindService;
import com.example.mvcsecurityjpa.service.board.BoardService;
import com.example.mvcsecurityjpa.service.task.TaskService;
import com.example.mvcsecurityjpa.userDetails.CustomUserDetails;
import com.example.mvcsecurityjpa.helper.AuthenticationHelper;
import com.example.mvcsecurityjpa.helper.BoardPermissionHelper;

import jakarta.validation.Valid;

/**
 * BoardController
 */
@Controller
public class BoardController {
  private BoardService boardService;
  private BoardFindService boardFindService;
  private TaskService taskService;
  private AuthenticationHelper authenticationHelper;
  private BoardPermissionHelper boardPermissionHelper;

  BoardController(BoardService boardService,
                  BoardFindService boardFindService,
                  TaskService taskService,
                  AuthenticationHelper authenticationHelper,
                  BoardPermissionHelper boardPermissionHelper) {
    this.boardService = boardService;
    this.boardFindService = boardFindService;
    this.taskService = taskService;
    this.authenticationHelper = authenticationHelper;
    this.boardPermissionHelper = boardPermissionHelper;
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
    System.out.println("show board details method in board controller");
    if (!boardPermissionHelper.isOwner(id)) {
      return "redirect:/boards";
    }

    Board board = boardFindService.findByBoardId(id);
    if (board == null) {
      return "redirect:/boards";
    }
    Map<Status, List<Task>> tasks = taskService.groupByStatus(taskService.findAllByBoardId(id));
    model.addAttribute("board", board);
    model.addAttribute("todoTasks", tasks.get(Status.TODO));
    model.addAttribute("inProgressTasks", tasks.get(Status.IN_PROGRESS));
    model.addAttribute("doneTasks", tasks.get(Status.DONE));

    return "board/board-details";
  }

  @GetMapping("/board/new")
  public String showBoardCreation(@ModelAttribute("form") BoardCreationForm form) {
    return "board/board-creation";
  }


  @PostMapping("/board/new")
  public String createBoard(@Valid @ModelAttribute("form") BoardCreationForm form, RedirectAttributes redirectAttributes) {

    form.setUser(authenticationHelper.getCurrentUser());
    Long boardId = boardService.save(form).getId();

    // redirect to board details page based on id
    redirectAttributes.addAttribute("id", boardId);
    return "redirect:/boards/{id}";
  }


  @GetMapping("/boards/{id}/edit")
  public String showBoardEdit(@ModelAttribute BoardEditForm form, @PathVariable("id") Long id, Model model) {
    if (!boardPermissionHelper.isOwner(id)) {
      return "redirect:/boards";
    }

    Board board = boardFindService.findByBoardId(id);
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

    form.setBoardId(id);
    boardService.update(form);

    redirectAttributes.addAttribute("id", id);
    return "redirect:/boards/{id}";
  }


  @PostMapping("/boards/{id}/delete")
  public String DeleteBoard(@PathVariable Long id) {
    try {
      boardService.deleteBoard(id);
      return "redirect:/boards";
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return "redirect:/boards";
    }
  }


}