package com.bookstore.repository;

import com.bookstore.model.CartItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * CartItemRepository — data access layer for the CartItem entity.
 *
 * CrudRepository provides:
 *   - findAll()      : get all items in cart
 *   - save(item)     : insert or update a cart item
 *   - deleteById(id) : remove an item from cart
 *
 * Custom methods below handle cart-specific queries.
 */
@Repository
public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    /**
     * Find a cart item by the book it references.
     * This helps detect if a book is already in the cart
     * so we can increment quantity instead of duplicating.
     *
     * SQL: SELECT * FROM cart_item WHERE book_id = ?
     *
     * @param bookId - the book's ID
     * @return Optional cart item (empty if not in cart)
     */
    Optional<CartItem> findByBookId(Long bookId);

    /**
     * Remove a cart item by the book it references.
     * SQL: DELETE FROM cart_item WHERE book_id = ?
     *
     * @param bookId - the book's ID
     */
    void deleteByBookId(Long bookId);
}
