package com.college.management.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.college.management.dto.BookRequestDto;
import com.college.management.dto.BookResponseDto;
import com.college.management.exception.DuplicateResourceException;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.model.Book;
import com.college.management.repository.BookRepository;

@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponseDto createBook(BookRequestDto requestDto) {
        ensureIsbnIsUnique(requestDto.getIsbn(), null);
        Book book = toEntity(requestDto);
        book.setBookId(generateId(requestDto.getBookId(), "BOK"));
        Book savedBook = bookRepository.save(book);
        log.info("Created book {}", savedBook.getBookId());
        return toDto(savedBook);
    }

    public BookResponseDto getBookById(String bookId) {
        return toDto(findBook(bookId));
    }

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll().stream().map(this::toDto).toList();
    }

    public BookResponseDto updateBook(String bookId, BookRequestDto requestDto) {
        Book existingBook = findBook(bookId);
        ensureIsbnIsUnique(requestDto.getIsbn(), bookId);
        existingBook.setTitle(requestDto.getTitle());
        existingBook.setAuthor(requestDto.getAuthor());
        existingBook.setIsbn(requestDto.getIsbn());
        existingBook.setQuantity(requestDto.getQuantity());
        Book savedBook = bookRepository.save(existingBook);
        log.info("Updated book {}", bookId);
        return toDto(savedBook);
    }

    public void deleteBook(String bookId) {
        Book book = findBook(bookId);
        bookRepository.delete(book);
        log.info("Deleted book {}", bookId);
    }

    public Book findBook(String bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    private void ensureIsbnIsUnique(String isbn, String currentBookId) {
        bookRepository.findByIsbn(isbn).ifPresent(existing -> {
            if (currentBookId == null || !existing.getBookId().equals(currentBookId)) {
                throw new DuplicateResourceException("Book ISBN already exists: " + isbn);
            }
        });
    }

    private Book toEntity(BookRequestDto requestDto) {
        return Book.builder()
                .title(requestDto.getTitle())
                .author(requestDto.getAuthor())
                .isbn(requestDto.getIsbn())
                .quantity(requestDto.getQuantity())
                .build();
    }

    private BookResponseDto toDto(Book book) {
        return BookResponseDto.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .quantity(book.getQuantity())
                .build();
    }

    private String generateId(String providedId, String prefix) {
        if (providedId != null && !providedId.isBlank()) {
            return providedId;
        }
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
