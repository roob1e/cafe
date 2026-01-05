package com.assxmblxr.cafe.dao;

import com.assxmblxr.cafe.entity.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemDao {
  void create(MenuItem menuItem);
  Optional<MenuItem> findById(Long id);
  Optional<MenuItem> findByName(String name);
  List<MenuItem> findAll();
  void update(MenuItem menuItem);
  void delete(MenuItem menuItem);
}
