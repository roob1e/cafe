package com.assxmblxr.cafe.controller;

import com.assxmblxr.cafe.controller.command.Command;
import com.assxmblxr.cafe.controller.command.impl.*;
import com.assxmblxr.cafe.dao.impl.OrderDaoImpl;
import com.assxmblxr.cafe.dao.impl.UserDaoImpl;
import com.assxmblxr.cafe.service.UserService;
import com.assxmblxr.cafe.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@WebServlet("/controller")
public class MainServlet extends HttpServlet {

  private final Map<String, Command> commands = new HashMap<>();

  @Override
  public void init() throws ServletException {
    UserService userService = new UserServiceImpl(new UserDaoImpl(), new OrderDaoImpl());

    commands.put("login", new LoginCommand(userService));
    commands.put("logout", new LogoutCommand());
    commands.put("register", new RegisterCommand(userService));

    commands.put("go_to_login", (req, resp) -> Command.PATH_LOGIN);
    commands.put("go_to_register", (req, resp) -> Command.PATH_REGISTER);
    commands.put("go_to_main", (req, resp) -> Command.PATH_MAIN);

    log.info("MainServlet initialized with {} commands", commands.size());
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processRequest(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    processRequest(req, resp);
  }

  private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String commandName = req.getParameter("command");

    Command command = commands.getOrDefault(commandName, (request, response) -> Command.PATH_INDEX);

    try {
      String resultPath = command.execute(req, resp);

      if (resultPath.startsWith("redirect:")) {
        String redirectPath = req.getContextPath() + resultPath.substring(9);
        resp.sendRedirect(redirectPath);
      } else {
        req.getRequestDispatcher(resultPath).forward(req, resp);
      }
    } catch (Exception e) {
      log.error("Error executing command: {}", commandName, e);
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}