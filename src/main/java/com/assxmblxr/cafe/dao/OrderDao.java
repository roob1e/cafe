package com.assxmblxr.cafe.dao;

import com.assxmblxr.cafe.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
  void create(Order order);
  Optional<Order> findById(long id);
  List<Order> findAll();
  void update(Order order);
  void delete(Order order);
}
