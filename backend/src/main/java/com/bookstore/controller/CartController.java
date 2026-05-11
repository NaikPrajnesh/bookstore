package com.bookstore.controller;

import com.bookstore.model.CartItem;
import com.bookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * CartController — handles HTTP requests for the shopping cart.
 *
 * Available endpoints:
 *   GET    /cart        → get all cart items
 *   POST   /cart        → add a book to the cart
 *   DELETE /cart/{id}   → remove an item from the cart
 *   DELETE /cart        → clear the entire cart
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * GET /cart
     * Returns all items currently in the cart (with book details).
     *
     * Response example:
     * [
     *   { "id": 1, "bookId": 3, "title": "Sapiens", "price": 17.99, "quantity": 2 },
     *   ...
     * ]
     *
     * @return 200 OK with list of cart items
     */
    @GetMapping
    public ResponseEntity<List<CartItem>> getCart() {
        List<CartItem> items = cartService.getCartItems();
        return ResponseEntity.ok(items);
    }

    /**
     * POST /cart
     * Add a book to the cart.
     *
     * Request body (JSON):
     * {
     *   "bookId": 3,
     *   "quantity": 1
     * }
     *
     * If the book is already in the cart, its quantity is incremented.
     *
     * @param payload - map containing "bookId" and optional "quantity"
     * @return 200 OK with updated cart items
     */
    @PostMapping
    public ResponseEntity<List<CartItem>> addToCart(@RequestBody Map<String, Object> payload) {
        // Extract bookId and quantity from the request body
        Long bookId  = Long.valueOf(payload.get("bookId").toString());
        int quantity = payload.containsKey("quantity")
                       ? Integer.parseInt(payload.get("quantity").toString())
                       : 1;

        List<CartItem> updatedCart = cartService.addToCart(bookId, quantity);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * DELETE /cart/{id}
     * Remove a specific item from the cart by its cart item ID.
     *
     * Example: DELETE /cart/2
     *
     * @param id - CartItem primary key (not the book ID)
     * @return 200 OK with updated cart items
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<List<CartItem>> removeFromCart(@PathVariable Long id) {
        List<CartItem> updatedCart = cartService.removeFromCart(id);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * DELETE /cart
     * Clear all items from the cart.
     *
     * @return 204 No Content
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
