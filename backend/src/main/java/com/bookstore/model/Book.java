package com.bookstore.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Book — represents a book in the store.
 *
 * @Table maps this class to the "book" table in the database.
 * Spring Data JDBC will automatically map columns by name (snake_case).
 */
@Table("book")
public class Book {

    /** Auto-generated primary key */
    @Id
    private Long id;

    private String title;
    private String author;
    private String description;
    private Double price;
    private String category;
    private String image;    // URL to the book cover image
    private Double rating;   // Average rating (0.0 - 5.0)

    // ---- Constructors ----

    public Book() {}

    public Book(String title, String author, String description,
                Double price, String category, String image, Double rating) {
        this.title       = title;
        this.author      = author;
        this.description = description;
        this.price       = price;
        this.category    = category;
        this.image       = image;
        this.rating      = rating;
    }

    // ---- Getters & Setters ----

    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }

    public String getTitle()               { return title; }
    public void setTitle(String title)     { this.title = title; }

    public String getAuthor()              { return author; }
    public void setAuthor(String author)   { this.author = author; }

    public String getDescription()         { return description; }
    public void setDescription(String d)   { this.description = d; }

    public Double getPrice()               { return price; }
    public void setPrice(Double price)     { this.price = price; }

    public String getCategory()            { return category; }
    public void setCategory(String c)      { this.category = c; }

    public String getImage()               { return image; }
    public void setImage(String image)     { this.image = image; }

    public Double getRating()              { return rating; }
    public void setRating(Double rating)   { this.rating = rating; }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', author='" + author + "', price=" + price + "}";
    }
}
