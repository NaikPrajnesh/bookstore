package com.bookstore.service;

import com.bookstore.model.Order;
import com.bookstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;

/**
 * CheckoutService — business logic for order processing.
 *
 * When the user checks out:
 *   1. Calculate the order total from the cart
 *   2. Generate a unique order code
 *   3. Save the order to the database
 *   4. Clear the cart
 */
@Service
public class CheckoutService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    /**
     * Process checkout: create an order and clear the cart.
     *
     * @return The saved Order object (including its generated order code)
     */
    public Order processCheckout() {
        // Calculate total from current cart
        double total = cartService.calculateTotal();

        // Generate a human-friendly order code using current timestamp
        String orderCode = "PT-" + Long.toString(Instant.now().toEpochMilli(), 36).toUpperCase();

        // Create and save the order record
        Order order = new Order(orderCode, total);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart after a successful order
        cartService.clearCart();

        return savedOrder;
    }
}
