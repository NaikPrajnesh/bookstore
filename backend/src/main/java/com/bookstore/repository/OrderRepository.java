package com.bookstore.repository;

import com.bookstore.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * OrderRepository — data access layer for Order records.
 *
 * CrudRepository provides all basic CRUD operations.
 * Additional custom queries can be added here if needed.
 */
@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    // Built-in methods from CrudRepository are sufficient for now.
    // Example of a custom query you could add:
    // List<Order> findByOrderCodeContaining(String code);
}
