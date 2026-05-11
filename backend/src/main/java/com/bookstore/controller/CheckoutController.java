package com.bookstore.controller;

import com.bookstore.model.Order;
import com.bookstore.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * CheckoutController — handles the checkout process.
 *
 * Available endpoints:
 *   POST /checkout → process order and clear cart
 */
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    /**
     * POST /checkout
     * Process the order:
     *   1. Saves an Order record to the database
     *   2. Clears the cart
     *   3. Returns the order details to the frontend
     *
     * Request body (optional — we read from the cart in the DB):
     * { "items": [...] }
     *
     * Response:
     * {
     *   "orderId": "PT-ABC123",
     *   "total": 47.97,
     *   "message": "Order placed successfully!"
     * }
     *
     * @return 200 OK with order summary
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> checkout() {
        // Process the order through the service layer
        Order order = checkoutService.processCheckout();

        // Build a simple response map for the frontend
        Map<String, Object> response = Map.of(
            "orderId",  order.getOrderCode(),
            "total",    order.getTotal(),
            "message",  "Order placed successfully!"
        );

        return ResponseEntity.ok(response);
    }
}
