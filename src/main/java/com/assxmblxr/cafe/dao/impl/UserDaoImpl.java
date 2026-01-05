package com.assxmblxr.cafe.dao.impl;

import com.assxmblxr.cafe.dao.UserDao;
import com.assxmblxr.cafe.type.Role;
import com.assxmblxr.cafe.entity.User;
import com.assxmblxr.cafe.exception.CafeException;
import com.assxmblxr.cafe.util.DatabaseUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserDaoImpl implements UserDao {
  private static final String INSERT_USER = """
                INSERT INTO users (name, email, account_balance, loyalty_points, blocked, role, password)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;
  private static final String SELECT_BY_ID = "SELECT * FROM users WHERE user_id = ?;";
  private static final String SELECT_ALL = "SELECT * FROM users;";
  private static final String UPDATE_USER = """
               UPDATE users
               SET name = ?,
                   email = ?,
                   account_balance = ?,
                   loyalty_points = ?,
                   blocked = ?,
                   role = ?,
                   password = ?
               WHERE user_id = ?;
               """;
  private static final String DELETE_USER = "DELETE FROM users WHERE user_id = ?;";
  private static final String SELECT_BY_EMAIL = "SELECT * FROM users WHERE email = ?;";
  private static final String UPDATE_BLOCKED_STATUS = "UPDATE users SET blocked = ? WHERE user_id = ?;";
  private static final String UPDATE_LOYALTY_POINTS = "UPDATE users SET loyalty_points = ? WHERE user_id = ?; ";

  @Override
  public void create(User user) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(INSERT_USER)) {
      ps.setString(1, user.getName());
      ps.setString(2, user.getEmail());
      ps.setBigDecimal(3, user.getAccountBalance());
      ps.setBigDecimal(4, user.getLoyaltyPoints());
      ps.setBoolean(5, user.isBlocked());
      ps.setString(6, user.getRole().name());
      ps.setString(7, user.getPassword());

      ps.executeUpdate();
      log.info("User created: {}", user.getEmail());
    } catch (SQLException e) {
      log.error("Error creating user: {}", user.getEmail(), e);
      throw new CafeException("Database error during user creation", e);
    }
  }

  @Override
  public Optional<User> findById(long id) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
      ps.setLong(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(extractUserFromResultSet(rs));
        }
      }
    } catch (SQLException e) {
      log.error("Error finding user by id: {}", id, e);
      throw new CafeException(e);
    }
    return Optional.empty();
  }

  @Override
  public List<User> findAll() {
    List<User> users = new ArrayList<>();

    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        users.add(extractUserFromResultSet(rs));
      }
    } catch (SQLException e) {
      log.error("Error fetching all users", e);
      throw new CafeException(e);
    }
    return users;
  }

  @Override
  public void update(User user) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(UPDATE_USER)) {
      ps.setString(1, user.getName());
      ps.setString(2, user.getEmail());
      ps.setBigDecimal(3, user.getAccountBalance());
      ps.setBigDecimal(4, user.getLoyaltyPoints());
      ps.setBoolean(5, user.isBlocked());
      ps.setString(6, user.getRole().name());
      ps.setString(7, user.getPassword());
      ps.setLong(8, user.getId());

      int affected = ps.executeUpdate();
      log.info("User {} updated, affected rows: {}", user.getId(), affected);
    } catch (SQLException e) {
      log.error("Error updating user: {}", user.getId(), e);
      throw new CafeException(e);
    }
  }

  @Override
  public void delete(User user) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(DELETE_USER)) {
      ps.setLong(1, user.getId());
      ps.executeUpdate();
      log.info("User deleted, id: {}", user.getId());
    } catch (SQLException e) {
      log.error("Error deleting user: {}", user.getId(), e);
      throw new CafeException(e);
    }
  }

  @Override
  public Optional<User> findByEmail(String email) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_BY_EMAIL)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(extractUserFromResultSet(rs));
        }
      }
    } catch (SQLException e) {
      log.error("Error finding user by email: {}", email, e);
      throw new CafeException(e);
    }
    return Optional.empty();
  }

  @Override
  public void updateBlockedStatus(long userId, boolean blocked) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(UPDATE_BLOCKED_STATUS)) {
      ps.setBoolean(1, blocked);
      ps.setLong(2, userId);
      ps.executeUpdate();
      log.info("User {} blocked status updated to: {}", userId, blocked);
    } catch (SQLException e) {
      log.error("Error updating blocked status for user: {}", userId, e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void updateLoyaltyPoints(long userId, BigDecimal points) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(UPDATE_LOYALTY_POINTS)) {
      ps.setBigDecimal(1, points);
      ps.setLong(2, userId);
      ps.executeUpdate();
      log.info("User {} loyalty points updated to: {}", userId, points);
    } catch (SQLException e) {
      log.error("Error updating loyalty points for user: {}", userId, e);
      throw new RuntimeException(e);
    }
  }

  private User extractUserFromResultSet(ResultSet rs) throws SQLException {
    return User.builder()
            .id(rs.getLong("user_id"))
            .name(rs.getString("name"))
            .email(rs.getString("email"))
            .accountBalance(rs.getBigDecimal("account_balance"))
            .loyaltyPoints(rs.getBigDecimal("loyalty_points"))
            .blocked(rs.getBoolean("blocked"))
            .role(Role.fromString(rs.getString("role")))
            .password(rs.getString("password"))
            .build();
  }
}