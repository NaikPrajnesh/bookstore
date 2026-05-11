package com.bookstore.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

/**
 * Order — represents a completed purchase.
 *
 * Stored in the "orders" table when checkout is successful.
 */
@Table("orders")
public class Order {

    @Id
    private Long id;

    private String orderCode;       // Human-readable order ID (e.g. "PT-ABC123")
    private Double total;           // Grand total amount
    private LocalDateTime createdAt;

    // ---- Constructors ----

    public Order() {}

    public Order(String orderCode, Double total) {
        this.orderCode = orderCode;
        this.total     = total;
        this.createdAt = LocalDateTime.now();
    }

    // ---- Getters & Setters ----

    public Long getId()                         { return id; }
    public void setId(Long id)                  { this.id = id; }

    public String getOrderCode()                { return orderCode; }
    public void setOrderCode(String orderCode)  { this.orderCode = orderCode; }

    public Double getTotal()                    { return total; }
    public void setTotal(Double total)          { this.total = total; }

    public LocalDateTime getCreatedAt()          { return createdAt; }
    public void setCreatedAt(LocalDateTime t)   { this.createdAt = t; }

    @Override
    public String toString() {
        return "Order{id=" + id + ", orderCode='" + orderCode + "', total=" + total + "}";
    }
}
