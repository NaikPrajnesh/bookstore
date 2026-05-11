/**
 * Chapter & Verse Bookstore — script.js
 * Handles all frontend logic with smooth animations
 */

// ==================== CONFIGURATION ====================
// Change this to your Spring Boot server URL when running locally
const API_BASE = 'http://localhost:8080';

// ==================== STATE ====================
let allBooks   = [];       // All books fetched from API
let cartItems  = [];       // Current cart contents
let currentBook = null;    // Book being viewed in details page
let selectedRating = 0;    // Star rating selection
let currentCategory = 'all';

// ==================== MOCK DATA (for demo without backend) ====================
// This mock data lets you test the UI even before the backend is running.
// The real fetch() calls are also included and will be used when backend is live.
const MOCK_BOOKS = [
  {
    id: 1,
    title: "The Midnight Library",
    author: "Matt Haig",
    price: 14.99,
    category: "Fiction",
    description: "Between life and death there is a library, and within that library, the shelves go on forever. Every book provides a chance to try another life you could have lived.",
    image: "https://covers.openlibrary.org/b/id/10909258-L.jpg",
    rating: 4.5,
    reviews: [{ author: "Aria", rating: 5, text: "A beautiful, life-affirming read." }]
  },
  {
    id: 2,
    title: "Atomic Habits",
    author: "James Clear",
    price: 16.99,
    category: "Self-Help",
    description: "An easy and proven way to build good habits and break bad ones. Transform your life with tiny changes in behavior that lead to remarkable results.",
    image: "https://covers.openlibrary.org/b/id/10521270-L.jpg",
    rating: 4.8,
    reviews: []
  },
  {
    id: 3,
    title: "Sapiens",
    author: "Yuval Noah Harari",
    price: 17.99,
    category: "History",
    description: "A brief history of humankind, exploring how Homo sapiens came to rule the world through cognitive revolution, agricultural revolution, and scientific revolution.",
    image: "https://covers.openlibrary.org/b/id/8739161-L.jpg",
    rating: 4.7,
    reviews: []
  },
  {
    id: 4,
    title: "Clean Code",
    author: "Robert C. Martin",
    price: 29.99,
    category: "Technology",
    description: "A handbook of agile software craftsmanship. Learn to write code that is readable, maintainable, and elegant — transforming messy code into clean art.",
    image: "https://covers.openlibrary.org/b/id/8775116-L.jpg",
    rating: 4.6,
    reviews: []
  },
  {
    id: 5,
    title: "A Brief History of Time",
    author: "Stephen Hawking",
    price: 13.99,
    category: "Science",
    description: "From the Big Bang to black holes, Stephen Hawking explores the mysteries of the universe in this accessible and fascinating scientific journey.",
    image: "https://covers.openlibrary.org/b/id/8739177-L.jpg",
    rating: 4.9,
    reviews: []
  },
  {
    id: 6,
    title: "The Alchemist",
    author: "Paulo Coelho",
    price: 11.99,
    category: "Fiction",
    description: "A philosophical novel about a young shepherd named Santiago who travels from Andalusia to the Egyptian desert in search of treasure.",
    image: "https://covers.openlibrary.org/b/id/10987456-L.jpg",
    rating: 4.4,
    reviews: []
  },
  {
    id: 7,
    title: "The Lean Startup",
    author: "Eric Ries",
    price: 18.99,
    category: "Technology",
    description: "How today's entrepreneurs use continuous innovation to create radically successful businesses using validated learning and scientific experimentation.",
    image: "https://covers.openlibrary.org/b/id/8739163-L.jpg",
    rating: 4.3,
    reviews: []
  },
  {
    id: 8,
    title: "Thinking, Fast and Slow",
    author: "Daniel Kahneman",
    price: 15.99,
    category: "Science",
    description: "A groundbreaking tour of the mind that explains the two systems that drive the way we think — fast, intuitive and slow, deliberate thinking.",
    image: "https://covers.openlibrary.org/b/id/7984916-L.jpg",
    rating: 4.6,
    reviews: []
  }
];

// ==================== UTILITY ====================

/**
 * Show a specific page and hide others.
 * @param {string} pageName - 'home' | 'details' | 'cart' | 'checkout' | 'login'
 */
function showPage(pageName) {
  // Hide all pages
  document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
  // Show the selected page
  document.getElementById(`page-${pageName}`).classList.add('active');
  // Scroll to top
  window.scrollTo({ top: 0, behavior: 'smooth' });

  // Load relevant data when switching pages
  if (pageName === 'home') loadBooks();
  if (pageName === 'cart') renderCart();
}

/**
 * Show a brief toast notification.
 * @param {string} msg - Message to display
 */
function showToast(msg) {
  const toast = document.getElementById('toast');
  toast.textContent = msg;
  toast.classList.remove('hide');
  toast.classList.add('show');
  setTimeout(() => {
    toast.classList.remove('show');
    toast.classList.add('hide');
  }, 2800);
}

/**
 * Generate star HTML string from a rating number.
 * @param {number} rating - 0 to 5
 */
function renderStars(rating) {
  const full = Math.floor(rating);
  const half = rating - full >= 0.5 ? 1 : 0;
  const empty = 5 - full - half;
  return '\u2605'.repeat(full) + (half ? '\u00BD' : '') + '\u2606'.repeat(empty);
}

// ==================== DARK/LIGHT MODE ====================

function toggleTheme() {
  document.body.classList.toggle('dark');
  const btn = document.getElementById('themeToggle');
  btn.innerHTML = document.body.classList.contains('dark') ? '\u2600\uFE0F' : '\uD83C\uDF19';
  localStorage.setItem('theme', document.body.classList.contains('dark') ? 'dark' : 'light');
}

(function applyTheme() {
  if (localStorage.getItem('theme') === 'dark') {
    document.body.classList.add('dark');
    document.getElementById('themeToggle').innerHTML = '\u2600\uFE0F';
  }
})();

// ==================== MOBILE MENU ====================

function toggleMobileMenu() {
  document.getElementById('mobileMenu').classList.toggle('open');
}

// ==================== RIPPLE EFFECT ====================

function createRipple(e) {
  const btn = e.currentTarget;
  const circle = document.createElement('span');
  const diameter = Math.max(btn.clientWidth, btn.clientHeight);
  const radius = diameter / 2;

  const rect = btn.getBoundingClientRect();
  circle.style.width = circle.style.height = `${diameter}px`;
  circle.style.left = `${e.clientX - rect.left - radius}px`;
  circle.style.top = `${e.clientY - rect.top - radius}px`;
  circle.classList.add('ripple');

  const existing = btn.getElementsByClassName('ripple')[0];
  if (existing) existing.remove();

  btn.appendChild(circle);
}

function initRippleButtons() {
  document.querySelectorAll('.ripple-btn').forEach(btn => {
    btn.addEventListener('click', createRipple);
  });
}

// ==================== SCROLL OBSERVER ====================

function setupScrollObserver() {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');
      }
    });
  }, { threshold: 0.1, rootMargin: '0px 0px -40px 0px' });

  document.querySelectorAll('.animate-on-scroll').forEach(el => {
    observer.observe(el);
  });
}

// ==================== NAVBAR SCROLL ====================

function initNavbarScroll() {
  const navbar = document.getElementById('navbar');
  let lastScroll = 0;

  window.addEventListener('scroll', () => {
    const currentScroll = window.scrollY;
    if (currentScroll > 20) {
      navbar.classList.add('scrolled');
    } else {
      navbar.classList.remove('scrolled');
    }
    lastScroll = currentScroll;
  });
}

// ==================== SKELETON LOADING ====================

function showSkeleton(count = 6) {
  const grid = document.getElementById('booksGrid');
  grid.innerHTML = Array.from({ length: count }, () => `
    <div class="skeleton-card animate-on-scroll">
      <div class="skeleton-img skeleton"></div>
      <div class="skeleton-text skeleton" style="width:70%"></div>
      <div class="skeleton-text skeleton short"></div>
    </div>
  `).join('');
}

// ==================== BOOKS — FETCH & DISPLAY ====================

/**
 * Fetch all books from the backend.
 * Falls back to MOCK_BOOKS if the backend is not running.
 */
async function loadBooks() {
  showSkeleton(6);

  try {
    const response = await fetch(`${API_BASE}/books`);
    if (!response.ok) throw new Error('Network error');
    allBooks = await response.json();
  } catch (err) {
    console.warn('Backend not available, using mock data.');
    await new Promise(r => setTimeout(r, 600));
    allBooks = MOCK_BOOKS;
  }

  renderBooks(allBooks);
  setupScrollObserver();
}

/**
 * Render book cards into the grid.
 * @param {Array} books - Array of book objects
 */
function renderBooks(books) {
  const grid = document.getElementById('booksGrid');
  document.getElementById('bookCount').textContent = `${books.length} book${books.length !== 1 ? 's' : ''}`;

  if (books.length === 0) {
    grid.innerHTML = `
      <div class="no-results">
        <div class="no-results-icon">\uD83D\uDCDA</div>
        <p>No books found matching your search.</p>
      </div>
    `;
    return;
  }

  grid.innerHTML = books.map((book, i) => `
    <div class="book-card animate-on-scroll stagger-${Math.min(i % 6 + 1, 6)}" onclick="viewBook(${book.id})">
      ${book.image
        ? `<img src="${book.image}" alt="${book.title}" loading="lazy" onerror="this.outerHTML='<div class=\'book-cover-placeholder\'>\uD83D\uDCDA</div>'">`
        : `<div class="book-cover-placeholder">\uD83D\uDCDA</div>`
      }
      <div class="book-card-body">
        <div class="book-card-title">${book.title}</div>
        <div class="book-card-author">${book.author}</div>
        <div class="book-card-footer">
          <span class="book-price">$${parseFloat(book.price).toFixed(2)}</span>
          <span class="stars" title="${book.rating || 0} stars">
            ${renderStars(book.rating || 0)}
          </span>
        </div>
      </div>
    </div>
  `).join('');

  setupScrollObserver();
}

// ==================== SEARCH ====================

function searchBooks() {
  const query = document.getElementById('searchInput').value.toLowerCase().trim();
  let filtered = allBooks;

  // Apply category filter
  if (currentCategory !== 'all') {
    filtered = filtered.filter(b => b.category === currentCategory);
  }

  // Apply search filter
  if (query) {
    filtered = filtered.filter(b =>
      b.title.toLowerCase().includes(query) ||
      b.author.toLowerCase().includes(query)
    );
  }

  renderBooks(filtered);
}

// ==================== CATEGORY FILTER ====================

function filterCategory(category, btn) {
  currentCategory = category;

  // Update active button styling
  document.querySelectorAll('.cat-btn').forEach(b => b.classList.remove('active'));
  btn.classList.add('active');

  searchBooks(); // Re-run search with new category
}

// ==================== BOOK DETAILS ====================

/**
 * Load and display the details page for a book.
 * @param {number} bookId
 */
async function viewBook(bookId) {
  let book;
  try {
    const response = await fetch(`${API_BASE}/books/${bookId}`);
    if (!response.ok) throw new Error();
    book = await response.json();
  } catch {
    book = allBooks.find(b => b.id === bookId);
  }

  if (!book) { showToast('Book not found.'); return; }
  currentBook = book;

  document.getElementById('bookDetailContent').innerHTML = `
    <div style="text-align:center">
      ${book.image
        ? `<img src="${book.image}" alt="${book.title}" style="max-width:260px; border-radius:12px;" onerror="this.outerHTML='<div class=\'book-cover-placeholder\' style=\'height:320px\'>\uD83D\uDCDA</div>'">`
        : `<div class="book-cover-placeholder" style="height:320px">\uD83D\uDCDA</div>`
      }
    </div>
    <div class="book-detail-info">
      <span class="category-tag">${book.category || 'Book'}</span>
      <h1>${book.title}</h1>
      <p class="author">by <strong>${book.author}</strong></p>
      <div class="stars" style="font-size:1.05rem">${renderStars(book.rating || 0)} &nbsp; <span style="color:var(--text-secondary); font-size:0.88rem">(${book.rating || 0})</span></div>
      <p class="description">${book.description || 'No description available.'}</p>
      <p class="detail-price">$${parseFloat(book.price).toFixed(2)}</p>
      <button class="btn-primary ripple-btn" onclick="addToCart(${book.id})">Add to Cart</button>
    </div>
  `;

  renderReviews(book.reviews || []);
  initRippleButtons();
  showPage('details');
}

// ==================== CART ====================

/**
 * Add a book to the cart via POST /cart.
 * @param {number} bookId
 */
async function addToCart(bookId) {
  const book = allBooks.find(b => b.id === bookId) || currentBook;
  if (!book) return;

  try {
    const response = await fetch(`${API_BASE}/cart`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ bookId: book.id, quantity: 1 })
    });
    if (!response.ok) throw new Error();
    cartItems = await response.json(); // Expect updated cart list
  } catch {
    // Fallback: manage cart locally
    const existing = cartItems.find(c => c.id === book.id);
    if (existing) {
      existing.quantity = (existing.quantity || 1) + 1;
    } else {
      cartItems.push({ ...book, quantity: 1 });
    }
  }

  updateCartBadge();
  showToast(`"${book.title}" added to cart!`);
}

/**
 * Fetch the cart from the backend.
 */
async function fetchCart() {
  try {
    const response = await fetch(`${API_BASE}/cart`);
    if (!response.ok) throw new Error();
    cartItems = await response.json();
  } catch {
    // Use local state as fallback
  }
}

/**
 * Remove a book from the cart via DELETE /cart/{id}.
 * @param {number} bookId
 */
async function removeFromCart(bookId) {
  try {
    const response = await fetch(`${API_BASE}/cart/${bookId}`, { method: 'DELETE' });
    if (!response.ok) throw new Error();
    cartItems = await response.json();
  } catch {
    // Fallback: remove locally
    cartItems = cartItems.filter(c => c.id !== bookId);
  }

  updateCartBadge();
  renderCart();
  showToast('Item removed from cart.');
}

/**
 * Update the cart badge number in the navbar.
 */
function updateCartBadge() {
  const total = cartItems.reduce((sum, c) => sum + (c.quantity || 1), 0);
  const badge = document.getElementById('cartBadge');
  badge.textContent = total;
  badge.classList.remove('bump');
  void badge.offsetWidth;
  if (total > 0) badge.classList.add('bump');
}

/**
 * Render the cart page contents.
 */
function renderCart() {
  fetchCart().then(() => {
    const cartContent = document.getElementById('cartContent');

    if (cartItems.length === 0) {
      cartContent.innerHTML = `
        <div class="empty-state">
          <div class="empty-icon">\uD83D\uDED2</div>
          <h3>Your cart is empty</h3>
          <p>Add some books to get started!</p>
          <br/>
          <button class="btn-primary ripple-btn" onclick="showPage('home')">Browse Books</button>
        </div>
      `;
      initRippleButtons();
      return;
    }

    const total = cartItems.reduce((sum, c) => sum + (c.price * (c.quantity || 1)), 0);
    const shipping = 3.99;
    const grandTotal = total + shipping;

    cartContent.innerHTML = `
      <table class="cart-table animate-on-scroll">
        <thead>
          <tr>
            <th>Book</th>
            <th>Author</th>
            <th>Price</th>
            <th>Qty</th>
            <th>Subtotal</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          ${cartItems.map((item, i) => `
            <tr class="animate-on-scroll stagger-${Math.min(i % 6 + 1, 6)}">
              <td><strong>${item.title}</strong></td>
              <td>${item.author}</td>
              <td>$${parseFloat(item.price).toFixed(2)}</td>
              <td>${item.quantity || 1}</td>
              <td>$${(item.price * (item.quantity || 1)).toFixed(2)}</td>
              <td>
                <button class="btn-danger" onclick="removeFromCart(${item.id})">Remove</button>
              </td>
            </tr>
          `).join('')}
        </tbody>
      </table>

      <div class="cart-summary">
        <h3>Order Summary</h3>
        <div class="summary-row"><span>Subtotal</span><span>$${total.toFixed(2)}</span></div>
        <div class="summary-row"><span>Shipping</span><span>$${shipping.toFixed(2)}</span></div>
        <div class="summary-row summary-total"><span>Total</span><span>$${grandTotal.toFixed(2)}</span></div>
        <br/>
        <button class="btn-primary btn-full ripple-btn" onclick="checkout()">Proceed to Checkout</button>
      </div>
    `;

    setupScrollObserver();
    initRippleButtons();
  });
}

// ==================== CHECKOUT ====================

/**
 * Process checkout via POST /checkout.
 */
async function checkout() {
  if (cartItems.length === 0) {
    showToast('Your cart is empty.');
    return;
  }

  try {
    const response = await fetch(`${API_BASE}/checkout`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ items: cartItems })
    });
    if (!response.ok) throw new Error();
    const data = await response.json();
    document.getElementById('orderId').textContent = data.orderId || generateOrderId();
  } catch {
    // Fallback: generate order ID locally
    document.getElementById('orderId').textContent = generateOrderId();
  }

  // Clear cart after checkout
  cartItems = [];
  updateCartBadge();
  showPage('checkout');
}

/** Generate a random order ID for demo. */
function generateOrderId() {
  return 'CV-' + Date.now().toString(36).toUpperCase();
}

// ==================== REVIEWS ====================

let selectedRatingValue = 0;

function setRating(val) {
  selectedRatingValue = val;
  const stars = document.querySelectorAll('#starRating span');
  stars.forEach((s, i) => {
    s.classList.toggle('active', i < val);
  });
}

function renderReviews(reviews) {
  const list = document.getElementById('reviewsList');
  if (!reviews || reviews.length === 0) {
    list.innerHTML = '<p style="color:var(--text-secondary); font-size:0.92rem">No reviews yet. Be the first!</p>';
    return;
  }
  list.innerHTML = reviews.map(r => `
    <div class="review-item">
      <div class="review-meta">
        <span class="review-author">${r.author}</span>
        <span class="stars">${renderStars(r.rating)}</span>
      </div>
      <p class="review-text">${r.text}</p>
    </div>
  `).join('');
}

function submitReview() {
  const text = document.getElementById('reviewText').value.trim();
  if (!text) { showToast('Please write a review.'); return; }
  if (selectedRatingValue === 0) { showToast('Please select a rating.'); return; }

  if (!currentBook.reviews) currentBook.reviews = [];
  currentBook.reviews.push({ author: 'You', rating: selectedRatingValue, text });

  renderReviews(currentBook.reviews);
  document.getElementById('reviewText').value = '';
  setRating(0);
  showToast('Review submitted!');
}

// ==================== AUTH FORMS ====================

function switchAuthTab(tab) {
  const loginForm  = document.getElementById('loginForm');
  const signupForm = document.getElementById('signupForm');
  const loginTab   = document.getElementById('loginTab');
  const signupTab  = document.getElementById('signupTab');

  if (tab === 'login') {
    loginForm.style.display  = 'block';
    signupForm.style.display = 'none';
    loginTab.classList.add('active');
    signupTab.classList.remove('active');
    loginForm.classList.add('animate-on-scroll', 'visible');
  } else {
    loginForm.style.display  = 'none';
    signupForm.style.display = 'block';
    signupTab.classList.add('active');
    loginTab.classList.remove('active');
    signupForm.classList.add('animate-on-scroll', 'visible');
  }
}

/**
 * Validate and handle login form submission.
 * @param {Event} e
 */
function handleLogin(e) {
  e.preventDefault();
  let valid = true;

  const email    = document.getElementById('loginEmail').value.trim();
  const password = document.getElementById('loginPassword').value;

  // Clear previous errors
  document.getElementById('loginEmailErr').textContent = '';
  document.getElementById('loginPassErr').textContent = '';

  // Email validation
  if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
    document.getElementById('loginEmailErr').textContent = 'Enter a valid email address.';
    valid = false;
  }

  // Password validation
  if (!password || password.length < 6) {
    document.getElementById('loginPassErr').textContent = 'Password must be at least 6 characters.';
    valid = false;
  }

  if (!valid) return;

  // Simulate login success (replace with real API call)
  showToast('Logged in successfully!');
  showPage('home');
}

/**
 * Validate and handle signup form submission.
 * @param {Event} e
 */
function handleSignup(e) {
  e.preventDefault();
  let valid = true;

  const name     = document.getElementById('signupName').value.trim();
  const email    = document.getElementById('signupEmail').value.trim();
  const password = document.getElementById('signupPassword').value;
  const confirm  = document.getElementById('signupConfirm').value;

  // Clear errors
  ['signupNameErr','signupEmailErr','signupPassErr','signupConfirmErr']
    .forEach(id => document.getElementById(id).textContent = '');

  if (!name || name.length < 2) {
    document.getElementById('signupNameErr').textContent = 'Name must be at least 2 characters.';
    valid = false;
  }
  if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
    document.getElementById('signupEmailErr').textContent = 'Enter a valid email address.';
    valid = false;
  }
  if (!password || password.length < 6) {
    document.getElementById('signupPassErr').textContent = 'Password must be at least 6 characters.';
    valid = false;
  }
  if (password !== confirm) {
    document.getElementById('signupConfirmErr').textContent = 'Passwords do not match.';
    valid = false;
  }

  if (!valid) return;

  // Simulate signup success
  showToast('Account created! You can now log in.');
  switchAuthTab('login');
}

// ==================== INIT ====================

document.addEventListener('DOMContentLoaded', () => {
  loadBooks();
  initNavbarScroll();
  initRippleButtons();
});
