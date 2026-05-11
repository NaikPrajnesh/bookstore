package com.bookstore.repository;

import com.bookstore.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * BookRepository — data access layer for the Book entity.
 *
 * Extends CrudRepository which gives us built-in methods:
 *   - findAll()      : get all books
 *   - findById(id)   : get one book by ID
 *   - save(book)     : insert or update a book
 *   - deleteById(id) : remove a book
 *
 * Spring Data JDBC automatically implements these at runtime.
 * We can also add custom query methods here.
 */
@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    /**
     * Find books whose title contains the given string (case-insensitive).
     * Spring Data JDBC generates the SQL automatically from the method name.
     *
     * SQL equivalent: SELECT * FROM book WHERE LOWER(title) LIKE LOWER('%keyword%')
     *
     * @param keyword - search term
     * @return list of matching books
     */
    List<Book> findByTitleContainingIgnoreCase(String keyword);

    /**
     * Find books by category.
     * SQL: SELECT * FROM book WHERE category = ?
     *
     * @param category - e.g. "Fiction", "Science"
     * @return list of books in that category
     */
    List<Book> findByCategory(String category);

    /**
     * Find books by author name (partial match).
     * SQL: SELECT * FROM book WHERE LOWER(author) LIKE LOWER('%name%')
     *
     * @param name - author search term
     * @return list of matching books
     */
    List<Book> findByAuthorContainingIgnoreCase(String name);
}
