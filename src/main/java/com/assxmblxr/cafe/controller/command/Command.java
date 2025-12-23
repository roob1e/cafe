package com.assxmblxr.cafe.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Базовый интерфейс для всех команд приложения.
 * Содержит общие константы для атрибутов сессии и путей к страницам.
 */
public interface Command {
  String ATTR_USER = "user";
  String ATTR_ROLE = "role";
  String ATTR_ERROR = "errorMessage";
  String ATTR_SUCCESS = "successMessage";

  String PATH_LOGIN = "/WEB-INF/jsp/login.jsp";
  String PATH_REGISTER = "/WEB-INF/jsp/register.jsp";
  String PATH_MAIN = "/WEB-INF/jsp/main.jsp";
  String PATH_INDEX = "/index.jsp";
  String PATH_ADMIN_USERS = "/WEB-INF/jsp/admin/users.jsp";
  String PATH_ERROR = "/WEB-INF/jsp/error/error500.jsp";

  /**
   * Выполняет логику команды.
   * * @param request  объект запроса
   * @param response объект ответа
   * @return строка с путем для forward или префикс "redirect:" для редиректа
   * @throws Exception если что-то пошло не так
   */
  String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}