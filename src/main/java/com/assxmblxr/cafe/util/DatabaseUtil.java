package com.assxmblxr.cafe.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Утилитарный класс для управления пулом соединений с базой данных на базе {@link HikariDataSource}.
 * <p>
 * Инициализирует пул соединений один раз при загрузке класса, используя параметры из файла {@code db.properties}.
 * Класс является потокобезопасным и предоставляет статический доступ к соединениям.
 * </p>
 *
 * <b>Конфигурация (db.properties):</b>
 * <ul>
 * <li>{@code db.url} - URL подключения к БД</li>
 * <li>{@code db.username} - имя пользователя</li>
 * <li>{@code db.password} - пароль</li>
 * <li>{@code db.driver} - имя JDBC драйвера</li>
 * <li>{@code db.poolSize} - максимальное количество соединений (по умолчанию 10)</li>
 * </ul>
 *
 * @author assxmblxr
 */
@Slf4j
public class DatabaseUtil {
  private static final HikariDataSource dataSource;

  static {
    Properties props = new Properties();
    try (InputStream in = DatabaseUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
      if (in == null) {
        throw new RuntimeException("db.properties not found!");
      }
      props.load(in);

      HikariConfig config = new HikariConfig(props);
      dataSource = new HikariDataSource(config);
    } catch (IOException e) {
      log.error("Got an IOException when loading DB properties", e);
      throw new RuntimeException("Got an IOException when loading DB properties", e);
    }
  }

  private DatabaseUtil() {}

  public static Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

  public static void shutdown() {
    if (dataSource != null) {
      dataSource.close();
    }
  }
}