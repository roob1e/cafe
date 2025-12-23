package com.assxmblxr.cafe.controller.command.impl;

import com.assxmblxr.cafe.controller.command.Command;
import com.assxmblxr.cafe.entity.User;
import com.assxmblxr.cafe.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterCommand implements Command {
  private final UserService userService;

  public RegisterCommand(UserService userService) {
    this.userService = userService;
  }

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) {
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String name = request.getParameter("name");

    // TODO: сделать валидацию

    User user = new User();
    user.setEmail(email);
    user.setPassword(password);
    user.setName(name);

    if (userService.register(user)) {
      log.info("New user registered: {}", email);
      return "redirect:/controller?command=go_to_login";
    } else {
      log.warn("Registration failed: email {} already exists", email);
      request.setAttribute(ATTR_ERROR, "User with this email already exists");
      return PATH_REGISTER;
    }
  }
}
