package com.assxmblxr.cafe.controller.command.impl;

import com.assxmblxr.cafe.controller.command.Command;
import com.assxmblxr.cafe.entity.User;
import com.assxmblxr.cafe.exception.CafeException;
import com.assxmblxr.cafe.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class LoginCommand implements Command {
  private final UserService userService;

  public LoginCommand(UserService userService) {
    this.userService = userService;
  }

  @Override
  public String execute(HttpServletRequest request, HttpServletResponse response) {
    String email = request.getParameter("email");
    String password = request.getParameter("password");

    try {
      Optional<User> optionalUser = userService.login(email, password);

      if (optionalUser.isPresent()) {
        User user = optionalUser.get();

        HttpSession session = request.getSession();
        session.setAttribute(ATTR_USER, user);
        session.setAttribute(ATTR_ROLE, user.getRole());

        log.info("User {} successfully logged in with role: {}", email, user.getRole());
        return "redirect:/controller?command=go_to_main";
      } else {
        request.setAttribute(ATTR_ERROR, "Invalid password. Please try again.");
        return PATH_LOGIN;
      }
    } catch (CafeException e) {
      log.warn("Login attempt failed: {}", e.getMessage());
      request.setAttribute(ATTR_ERROR, e.getMessage());
      return PATH_LOGIN;
    }
  }
}