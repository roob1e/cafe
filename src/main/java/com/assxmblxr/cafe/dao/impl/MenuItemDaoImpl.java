package com.assxmblxr.cafe.dao.impl;

import com.assxmblxr.cafe.dao.MenuItemDao;
import com.assxmblxr.cafe.entity.MenuItem;
import com.assxmblxr.cafe.exception.CafeException;
import com.assxmblxr.cafe.util.DatabaseUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class MenuItemDaoImpl implements MenuItemDao {
  private static final String CREATE_ITEM = """
            INSERT INTO menu_items (name, description, price, is_available)
            VALUES (?, ?, ?, ?);
            """;
  private static final String SELECT_BY_ID = "SELECT * FROM menu_items WHERE id = ?;";
  private static final String SELECT_BY_NAME = "SELECT * FROM menu_items WHERE name = ?;";
  private static final String SELECT_ALL = "SELECT * FROM menu_items;";
  private static final String UPDATE_ITEM = """
          UPDATE menu_items
          SET name = ?,
              description = ?,
              price = ?,
              is_available = ?
          WHERE menu_item_id = ?;
          """;
  private static final String DELETE_ITEM = "DELETE FROM menu_items WHERE id = ?;";

  @Override
  public void create(MenuItem menuItem) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(CREATE_ITEM)) {
      ps.setString(1, menuItem.getName());
      ps.setString(2, menuItem.getDescription());
      ps.setBigDecimal(3, menuItem.getPrice());
      ps.setBoolean(4, menuItem.isAvailable());

      ps.executeUpdate();
    } catch (SQLException e) {
      log.error("Error creating menu item: {}", menuItem.getName() ,e);
      throw new CafeException(e);
    }
  }

  @Override
  public Optional<MenuItem> findById(Long id) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
      ps.setLong(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(extractItemFromResultSet(rs));
        }
      }
    } catch (SQLException e) {
      log.error("Error finding menu item by id {}", id ,e);
      throw new CafeException(e);
    }
    return Optional.empty();
  }

  @Override
  public Optional<MenuItem> findByName(String name) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_BY_NAME)) {
      ps.setString(1, name);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(extractItemFromResultSet(rs));
        }
      }
    } catch (SQLException e) {
      log.error("Error finding menu item by name {}", name ,e);
      throw new CafeException(e);
    }
    return Optional.empty();
  }

  @Override
  public List<MenuItem> findAll() {
    List<MenuItem> menuItems = new ArrayList<>();
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        menuItems.add(extractItemFromResultSet(rs));
      }
    } catch (SQLException e) {
      log.error("Error finding menu items ", e);
      throw new CafeException(e);
    }
    return menuItems;
  }

  @Override
  public void update(MenuItem menuItem) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(UPDATE_ITEM)) {
      ps.setString(1, menuItem.getName());
      ps.setString(2, menuItem.getDescription());
      ps.setBigDecimal(3, menuItem.getPrice());
      ps.setBoolean(4, menuItem.isAvailable());
      ps.setLong(5, menuItem.getMenuItemId());

      int affected = ps.executeUpdate();
      log.info("Updated menu item {}, rows affected: {}", menuItem.getMenuItemId(), affected);
    } catch (SQLException e) {
      log.error("Error updating menu item: {}", menuItem.getName() ,e);
      throw new CafeException(e);
    }
  }

  @Override
  public void delete(MenuItem menuItem) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(DELETE_ITEM)) {
      ps.setLong(1, menuItem.getMenuItemId());
      ps.executeUpdate();
      log.info("Deleted menu item {}", menuItem.getMenuItemId());
    } catch (SQLException e) {
      log.error("Error deleting menu item: {}", menuItem.getName() ,e);
      throw new CafeException(e);
    }
  }

  private MenuItem extractItemFromResultSet(ResultSet rs) throws SQLException {
    return MenuItem.builder()
            .id(rs.getLong(1))
            .name(rs.getString(2))
            .description(rs.getString(3))
            .price(rs.getBigDecimal(4))
            .isAvailable(rs.getBoolean(5))
            .build();
  }
}
