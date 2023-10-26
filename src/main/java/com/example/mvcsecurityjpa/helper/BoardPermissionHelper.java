package com.example.mvcsecurityjpa.helper;

import org.springframework.stereotype.Component;

import com.example.mvcsecurityjpa.entity.Board;
import com.example.mvcsecurityjpa.entity.User;
import com.example.mvcsecurityjpa.service.board.BoardFindService;


/**
 * BoardPermissionHelper
 */
@Component
public class BoardPermissionHelper {
  private AuthenticationHelper authenticationHelper;
  private BoardFindService boardFindService;

  public BoardPermissionHelper(AuthenticationHelper authenticationHelper,
                              BoardFindService boardFindService) {
    this.authenticationHelper = authenticationHelper;
    this.boardFindService = boardFindService;
  }

  public boolean isOwner(Long boardId) {
    System.out.println("BOARD PERMISSION HELPER IS OWNER");
    Board board = boardFindService.findByBoardId(boardId);
    if (board == null) return false;
    if (!authenticationHelper.isLoggedIn()) return false;

    User user = authenticationHelper.getAuthenticatedUserDetails().getUser();
    System.out.println("board owner id: " + board.getUser().getId());
    System.out.println("current user id: " + user.getId());

    if (user.getId() != board.getUser().getId()) return false;

    return true;
  }

  // public Long getBoardOwnerId(Long taskId) {
  //   Task task = taskFindService.findById(taskId);
  //   if (task == null) return null;
  //   if (!authenticationHelper.isLoggedIn()) return null;

  //   return authenticationHelper.getAuthenticatedUserDetails().getUser().getId();
  // }





}