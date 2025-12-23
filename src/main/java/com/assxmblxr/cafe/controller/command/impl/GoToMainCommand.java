package com.assxmblxr.cafe.controller.command.impl;

import com.assxmblxr.cafe.controller.command.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GoToMainCommand implements Command {
  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) {
    return PATH_MAIN;
  }
}
