package com.assxmblxr.cafe.dao.impl;

import com.assxmblxr.cafe.dao.OrderDao;
import com.assxmblxr.cafe.entity.Order;
import com.assxmblxr.cafe.entity.OrderItem;
import com.assxmblxr.cafe.entity.MenuItem;
import com.assxmblxr.cafe.entity.enums.OrderStatus;
import com.assxmblxr.cafe.entity.enums.PaymentMethod;
import com.assxmblxr.cafe.exception.CafeException;
import com.assxmblxr.cafe.util.DatabaseUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class OrderDaoImpl implements OrderDao {

  private static final String INSERT_ORDER = "INSERT INTO orders (user_id, total_price, pickup_time, payment_method, status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String INSERT_ITEM = "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)";
  private static final String UPDATE_ORDER = "UPDATE orders SET user_id=?, total_price=?, pickup_time=?, payment_method=?, status=? WHERE order_id=?";
  private static final String DELETE_ITEMS = "DELETE FROM order_items WHERE order_id = ?";
  private static final String SELECT_BY_ID = "SELECT * FROM orders WHERE order_id = ?";
  private static final String SELECT_ALL = "SELECT * FROM orders ORDER BY created_at DESC";

  @Override
  public void create(Order order) {
    try (Connection conn = DatabaseUtil.getConnection()) {
      conn.setAutoCommit(false);
      try (PreparedStatement psOrder = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
        psOrder.setLong(1, order.getUserId());
        psOrder.setBigDecimal(2, order.getTotalPrice());
        psOrder.setTimestamp(3, Timestamp.valueOf(order.getPickupTime()));
        psOrder.setString(4, order.getPaymentMethod().name());
        psOrder.setString(5, order.getStatus().name());
        psOrder.setTimestamp(6, Timestamp.valueOf(order.getCreatedAt()));

        psOrder.executeUpdate();
        try (ResultSet rs = psOrder.getGeneratedKeys()) {
          if (rs.next()) {
            order.setOrderId(rs.getLong(1));
          }
        }
        insertOrderItems(conn, order);

        conn.commit();
        log.info("Order created successfully, ID: {}", order.getOrderId());
      } catch (SQLException e) {
        conn.rollback();
        log.error("Create transaction rolled back", e);
        throw e;
      }
    } catch (SQLException e) {
      throw new CafeException("Error during order creation", e);
    }
  }

  @Override
  public void update(Order order) {
    try (Connection conn = DatabaseUtil.getConnection()) {
      conn.setAutoCommit(false);
      try {
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_ORDER)) {
          ps.setLong(1, order.getUserId());
          ps.setBigDecimal(2, order.getTotalPrice());
          ps.setTimestamp(3, Timestamp.valueOf(order.getPickupTime()));
          ps.setString(4, order.getPaymentMethod().name());
          ps.setString(5, order.getStatus().name());
          ps.setLong(6, order.getOrderId());
          ps.executeUpdate();
        }

        try (PreparedStatement psDel = conn.prepareStatement(DELETE_ITEMS)) {
          psDel.setLong(1, order.getOrderId());
          psDel.executeUpdate();
        }

        insertOrderItems(conn, order);

        conn.commit();
        log.info("Order {} updated successfully", order.getOrderId());
      } catch (SQLException e) {
        conn.rollback();
        log.error("Update transaction rolled back for order: {}", order.getOrderId(), e);
        throw e;
      }
    } catch (SQLException e) {
      throw new CafeException("Error updating order", e);
    }
  }

  private void insertOrderItems(Connection conn, Order order) throws SQLException {
    if (order.getItems() != null && !order.getItems().isEmpty()) {
      try (PreparedStatement psItems = conn.prepareStatement(INSERT_ITEM)) {
        for (OrderItem item : order.getItems()) {
          psItems.setLong(1, order.getOrderId());
          psItems.setLong(2, item.getMenuItem().getMenuItemId());
          psItems.setInt(3, item.getQuantity());
          psItems.addBatch();
        }
        psItems.executeBatch();
      }
    }
  }

  @Override
  public Optional<Order> findById(long id) {
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
      ps.setLong(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Order order = extractOrderFromResultSet(rs);
          order.setItems(fetchOrderItems(conn, id));
          return Optional.of(order);
        }
      }
    } catch (SQLException e) {
      log.error("Error finding order by id: {}", id, e);
      throw new CafeException(e);
    }
    return Optional.empty();
  }

  @Override
  public List<Order> findAll() {
    List<Order> orders = new ArrayList<>();
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Order order = extractOrderFromResultSet(rs);
        order.setItems(fetchOrderItems(conn, order.getOrderId()));
        orders.add(order);
      }
    } catch (SQLException e) {
      log.error("Error fetching all orders", e);
      throw new CafeException(e);
    }
    return orders;
  }

  @Override
  public void delete(Order order) {
    String sql = "DELETE FROM orders WHERE order_id = ?";
    try (Connection conn = DatabaseUtil.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, order.getOrderId());
      ps.executeUpdate();
      log.info("Order deleted, ID: {}", order.getOrderId());
    } catch (SQLException e) {
      log.error("Error deleting order", e);
      throw new CafeException(e);
    }
  }

  private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
    return Order.builder()
            .orderId(rs.getLong("order_id"))
            .userId(rs.getLong("user_id"))
            .totalPrice(rs.getBigDecimal("total_price"))
            .pickupTime(rs.getTimestamp("pickup_time").toLocalDateTime())
            .paymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")))
            .status(OrderStatus.valueOf(rs.getString("status")))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .build();
  }

  private List<OrderItem> fetchOrderItems(Connection conn, long orderId) throws SQLException {
    String sql = """
                SELECT mi.menu_item_id, mi.name, mi.description, mi.price, mi.is_available, oi.quantity
                FROM menu_items mi
                JOIN order_items oi ON mi.menu_item_id = oi.menu_item_id
                WHERE oi.order_id = ?""";
    List<OrderItem> items = new ArrayList<>();
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setLong(1, orderId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          MenuItem menuItem = MenuItem.builder()
                  .id(rs.getLong("menu_item_id"))
                  .name(rs.getString("name"))
                  .description(rs.getString("description"))
                  .price(rs.getBigDecimal("price"))
                  .isAvailable(rs.getBoolean("is_available"))
                  .build();
          items.add(new OrderItem(menuItem, rs.getInt("quantity")));
        }
      }
    }
    return items;
  }
}