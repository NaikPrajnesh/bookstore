package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * BookController — handles HTTP requests related to books.
 *
 * @RestController  = @Controller + @ResponseBody
 *   → Every method returns JSON automatically.
 *
 * @RequestMapping("/books") means all routes start with /books.
 *
 * Available endpoints:
 *   GET  /books           → list all books
 *   GET  /books/{id}      → get one book
 *   GET  /books/search?q= → search by title or author
 *   POST /books           → add a new book (admin use)
 */
@RestController
@RequestMapping("/books")
public class BookController {

    // Spring injects the service automatically
    @Autowired
    private BookService bookService;

    /**
     * GET /books
     * Returns all books, or filters by category if provided.
     *
     * Example: GET /books
     * Example: GET /books?category=Fiction
     *
     * @param category - optional query param to filter by category
     * @return 200 OK with list of books
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String category) {

        List<Book> books;

        if (category != null && !category.isBlank()) {
            // Filter by category
            books = bookService.getBooksByCategory(category);
        } else {
            // Return all books
            books = bookService.getAllBooks();
        }

        return ResponseEntity.ok(books);
    }

    /**
     * GET /books/{id}
     * Returns a single book by its ID.
     *
     * Example: GET /books/3
     *
     * @param id - book primary key (from the URL path)
     * @return 200 OK with the book, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);

        // If book exists, return 200 with it; otherwise return 404
        return book.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /books/search?q=keyword
     * Search books by title or author.
     *
     * Example: GET /books/search?q=atomic
     *
     * @param q - search keyword
     * @return 200 OK with matching books
     */
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String q) {
        List<Book> results = bookService.searchBooks(q);
        return ResponseEntity.ok(results);
    }

    /**
     * POST /books
     * Create a new book (for admin functionality).
     *
     * Request body (JSON):
     * {
     *   "title": "Book Title",
     *   "author": "Author Name",
     *   "price": 19.99,
     *   "category": "Fiction",
     *   "description": "...",
     *   "image": "https://..."
     * }
     *
     * @param book - request body automatically parsed from JSON
     * @return 201 Created with the saved book
     */
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book saved = bookService.saveBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * DELETE /books/{id}
     * Delete a book by ID (admin use).
     *
     * @param id - book primary key
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
