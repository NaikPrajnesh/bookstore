-- =============================================
-- PageTurn Bookstore — schema.sql
-- Creates tables and seeds initial book data.
-- Runs automatically on Spring Boot startup.
-- =============================================

-- Drop tables if they already exist (for clean restarts)
DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS orders;

-- ---- Books table ----
CREATE TABLE book (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    author      VARCHAR(255) NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL,
    category    VARCHAR(100),
    image       VARCHAR(500),
    rating      DECIMAL(2, 1) DEFAULT 0.0
);

-- ---- Cart Items table ----
CREATE TABLE cart_item (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id     BIGINT NOT NULL,
    quantity    INT DEFAULT 1,
    FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE
);

-- ---- Orders table ----
CREATE TABLE orders (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_code  VARCHAR(50) NOT NULL,
    total       DECIMAL(10, 2),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- Seed Data: Initial books
-- =============================================
INSERT INTO book (title, author, description, price, category, image, rating) VALUES
(
    'The Midnight Library',
    'Matt Haig',
    'Between life and death there is a library, and within that library, the shelves go on forever. Every book provides a chance to try another life you could have lived.',
    14.99, 'Fiction',
    'https://covers.openlibrary.org/b/id/10909258-L.jpg',
    4.5
),
(
    'Atomic Habits',
    'James Clear',
    'An easy and proven way to build good habits and break bad ones. Transform your life with tiny changes in behavior that lead to remarkable results.',
    16.99, 'Self-Help',
    'https://covers.openlibrary.org/b/id/10521270-L.jpg',
    4.8
),
(
    'Sapiens',
    'Yuval Noah Harari',
    'A brief history of humankind, exploring how Homo sapiens came to rule the world through cognitive revolution, agricultural revolution, and scientific revolution.',
    17.99, 'History',
    'https://covers.openlibrary.org/b/id/8739161-L.jpg',
    4.7
),
(
    'Clean Code',
    'Robert C. Martin',
    'A handbook of agile software craftsmanship. Learn to write code that is readable, maintainable, and elegant — transforming messy code into clean art.',
    29.99, 'Technology',
    'https://covers.openlibrary.org/b/id/8775116-L.jpg',
    4.6
),
(
    'A Brief History of Time',
    'Stephen Hawking',
    'From the Big Bang to black holes, Stephen Hawking explores the mysteries of the universe in this accessible and fascinating scientific journey.',
    13.99, 'Science',
    'https://covers.openlibrary.org/b/id/8739177-L.jpg',
    4.9
),
(
    'The Alchemist',
    'Paulo Coelho',
    'A philosophical novel about a young shepherd named Santiago who travels from Andalusia to the Egyptian desert in search of treasure.',
    11.99, 'Fiction',
    'https://covers.openlibrary.org/b/id/10987456-L.jpg',
    4.4
),
(
    'The Lean Startup',
    'Eric Ries',
    'How today''s entrepreneurs use continuous innovation to create radically successful businesses using validated learning and scientific experimentation.',
    18.99, 'Technology',
    'https://covers.openlibrary.org/b/id/8739163-L.jpg',
    4.3
),
(
    'Thinking, Fast and Slow',
    'Daniel Kahneman',
    'A groundbreaking tour of the mind that explains the two systems that drive the way we think — fast, intuitive and slow, deliberate thinking.',
    15.99, 'Science',
    'https://covers.openlibrary.org/b/id/7984916-L.jpg',
    4.6
);
