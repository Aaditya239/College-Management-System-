package com.college.management.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.college.management.dto.ApiResponse;
import com.college.management.dto.BookRequestDto;
import com.college.management.dto.BookResponseDto;
import com.college.management.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDto>> createBook(@Valid @RequestBody BookRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildResponse(HttpStatus.CREATED, "Book created successfully", bookService.createBook(requestDto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> getAllBooks() {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Books retrieved successfully", bookService.getAllBooks()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> getBookById(@PathVariable("id") String bookId) {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Book retrieved successfully", bookService.getBookById(bookId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> updateBook(@PathVariable("id") String bookId,
                                                                   @Valid @RequestBody BookRequestDto requestDto) {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Book updated successfully", bookService.updateBook(bookId, requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable("id") String bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Book deleted successfully", null));
    }

    private <T> ApiResponse<T> buildResponse(HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.setTimestamp(Instant.now());
        response.setStatus(status.value());
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
