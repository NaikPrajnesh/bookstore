package com.bookstore.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * CartItem — represents a book added to the shopping cart.
 *
 * Each CartItem links to a Book by bookId and tracks the quantity.
 */
@Table("cart_item")
public class CartItem {

    @Id
    private Long id;

    private Long bookId;    // Foreign key to the book table
    private Integer quantity;

    // Transient fields — not stored in DB, populated by the service layer
    // (These carry book details to send back to the frontend)
    private String title;
    private String author;
    private Double price;
    private String image;

    // ---- Constructors ----

    public CartItem() {}

    public CartItem(Long bookId, Integer quantity) {
        this.bookId   = bookId;
        this.quantity = quantity;
    }

    // ---- Getters & Setters ----

    public Long getId()                      { return id; }
    public void setId(Long id)               { this.id = id; }

    public Long getBookId()                  { return bookId; }
    public void setBookId(Long bookId)       { this.bookId = bookId; }

    public Integer getQuantity()             { return quantity; }
    public void setQuantity(Integer qty)     { this.quantity = qty; }

    public String getTitle()                 { return title; }
    public void setTitle(String title)       { this.title = title; }

    public String getAuthor()                { return author; }
    public void setAuthor(String author)     { this.author = author; }

    public Double getPrice()                 { return price; }
    public void setPrice(Double price)       { this.price = price; }

    public String getImage()                 { return image; }
    public void setImage(String image)       { this.image = image; }

    @Override
    public String toString() {
        return "CartItem{id=" + id + ", bookId=" + bookId + ", quantity=" + quantity + "}";
    }
}
