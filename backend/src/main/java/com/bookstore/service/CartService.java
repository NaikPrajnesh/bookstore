package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CartService — business logic for shopping cart operations.
 *
 * Handles:
 *   - Adding books to the cart (with quantity logic)
 *   - Fetching cart with book details attached
 *   - Removing items from the cart
 *   - Clearing the entire cart (after checkout)
 */
@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Get all items in the cart, with book details populated.
     *
     * We fetch CartItems from the DB, then enrich each one with
     * book title, author, and price so the frontend has everything it needs.
     *
     * @return List of enriched CartItems
     */
    public List<CartItem> getCartItems() {
        List<CartItem> items = new ArrayList<>();

        cartItemRepository.findAll().forEach(item -> {
            // Fetch book details and attach them to the cart item
            Optional<Book> bookOpt = bookRepository.findById(item.getBookId());
            bookOpt.ifPresent(book -> {
                item.setTitle(book.getTitle());
                item.setAuthor(book.getAuthor());
                item.setPrice(book.getPrice());
                item.setImage(book.getImage());
            });
            items.add(item);
        });

        return items;
    }

    /**
     * Add a book to the cart.
     *
     * If the book is already in the cart, increment its quantity.
     * Otherwise, create a new CartItem.
     *
     * @param bookId   - ID of the book to add
     * @param quantity - how many copies to add
     * @return Updated list of cart items
     */
    public List<CartItem> addToCart(Long bookId, int quantity) {
        // Check if this book is already in the cart
        Optional<CartItem> existing = cartItemRepository.findByBookId(bookId);

        if (existing.isPresent()) {
            // Book already in cart — increment quantity
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            // New item — create and save it
            CartItem newItem = new CartItem(bookId, quantity);
            cartItemRepository.save(newItem);
        }

        return getCartItems();   // Return the updated cart
    }

    /**
     * Remove an item from the cart by its cart item ID.
     *
     * @param cartItemId - the CartItem primary key
     * @return Updated list of cart items
     */
    public List<CartItem> removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        return getCartItems();
    }

    /**
     * Clear all items from the cart.
     * Called after a successful checkout.
     */
    public void clearCart() {
        cartItemRepository.deleteAll();
    }

    /**
     * Calculate the total price of all items in the cart.
     *
     * @return Grand total as a double
     */
    public double calculateTotal() {
        return getCartItems().stream()
            .mapToDouble(item -> (item.getPrice() != null ? item.getPrice() : 0.0)
                                 * (item.getQuantity() != null ? item.getQuantity() : 1))
            .sum();
    }
}
