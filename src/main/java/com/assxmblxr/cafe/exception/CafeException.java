package com.assxmblxr.cafe.exception;

import java.sql.SQLException;

public class CafeException extends RuntimeException {
  public CafeException() {
    super();
  }

  public CafeException(String message) {
    super(message);
  }

  public CafeException(String message, Throwable cause) {
    super(message, cause);
  }

  public CafeException(SQLException e) {
    super(e);
  }
}
