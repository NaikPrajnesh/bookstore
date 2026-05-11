package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * BookService — business logic for book operations.
 *
 * The Service layer sits between the Controller (HTTP) and
 * the Repository (database). It handles:
 *   - Data validation
 *   - Business rules
 *   - Calling the repository
 *
 * @Service marks this as a Spring-managed service bean.
 */
@Service
public class BookService {

    // Spring automatically injects the BookRepository
    @Autowired
    private BookRepository bookRepository;

    /**
     * Get all books from the database.
     *
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        // CrudRepository.findAll() returns an Iterable; we convert it to a List
        bookRepository.findAll().forEach(books::add);
        return books;
    }

    /**
     * Get a single book by its ID.
     *
     * @param id - book primary key
     * @return Optional<Book> — empty if not found
     */
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Search books by title or author keyword.
     *
     * @param keyword - search term
     * @return List of matching books
     */
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllBooks();
        }
        // Merge results from title and author searches
        List<Book> byTitle  = bookRepository.findByTitleContainingIgnoreCase(keyword);
        List<Book> byAuthor = bookRepository.findByAuthorContainingIgnoreCase(keyword);

        // Add author results that aren't already in the title results
        for (Book b : byAuthor) {
            if (byTitle.stream().noneMatch(t -> t.getId().equals(b.getId()))) {
                byTitle.add(b);
            }
        }
        return byTitle;
    }

    /**
     * Filter books by category.
     *
     * @param category - e.g. "Fiction", "Science"
     * @return List of books in that category
     */
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategory(category);
    }

    /**
     * Save a new book to the database.
     * (Useful for admin functionality in the future)
     *
     * @param book - book to save
     * @return saved Book with generated ID
     */
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Delete a book by ID.
     *
     * @param id - book primary key
     */
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
