package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.entity.Book;
import bootcamp.hibernate_practical.exception.BookAlreadyBorrowedException;
import bootcamp.hibernate_practical.exception.BookNotBorrowedException;
import bootcamp.hibernate_practical.exception.BookNotFoundException;
import bootcamp.hibernate_practical.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponse createBook(CreateBookRequest request) {
        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getGenre(),
                request.getPublicationYear(),
                true,
                false
        );
        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToResponse).toList();
    }

    public BookResponse getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new BookNotFoundException("Book with id: " + id + " not found.");
        }
        return mapToResponse(book.get());
    }

    public BookResponse updateBook(Long id, UpdateBookRequest request) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book with id: " + id + " not found.");
        }

        Book book = optionalBook.get();

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setPublicationYear(request.getPublicationYear());
        book.setAvailable(request.getAvailable());

        bookRepository.save(book);

        return mapToResponse(book);
    }

    public BookResponse borrowBook(Long id) {

        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book with id: " + id + " not found.");
        }
        Book book = optionalBook.get();

        if (book.isBorrowedStatus()) {
            throw new BookAlreadyBorrowedException("Book with id: " + id + " already borrowed.");
        }
        book.setBorrowedStatus(true);

        bookRepository.save(book);
        return mapToResponse(book);
    }


    public BookResponse returnBook(Long id) {

        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book with id: " + id + " not found.");
        }
        Book book = optionalBook.get();

        if (!book.isBorrowedStatus()) {
            throw new BookNotBorrowedException("Book with id: " + id + " not borrowed.");
        }
        book.setBorrowedStatus(false);

        bookRepository.save(book);
        return mapToResponse(book);
    }

    public void deleteBook(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book with id: " + id + " not found.");
        }
        bookRepository.delete(optionalBook.get());
    }

    public List<BookResponse> findByAuthor(String author) {
        return bookRepository.findByAuthor(author).stream()
                .map(this::mapToResponse).toList();
    }

    public List<BookResponse> findAvailableBooks() {
        return bookRepository.findByAvailableTrue().stream()
                .map(this::mapToResponse).toList();
    }

    public List<BookResponse> findByPartialTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToResponse).toList();
    }

    private BookResponse mapToResponse(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(),
                book.getGenre(), book.getPublicationYear(), book.isAvailable(), book.isBorrowedStatus());
    }
}
