package com.college.management.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.college.management.dto.ApiResponse;
import com.college.management.dto.BookIssueRequestDto;
import com.college.management.dto.BookIssueResponseDto;
import com.college.management.dto.BookReturnRequestDto;
import com.college.management.service.BookIssueService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/book-issues")
@Validated
public class BookIssueController {

    private final BookIssueService bookIssueService;

    public BookIssueController(BookIssueService bookIssueService) {
        this.bookIssueService = bookIssueService;
    }

    @PostMapping("/issue")
    public ResponseEntity<ApiResponse<BookIssueResponseDto>> issueBook(@Valid @RequestBody BookIssueRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildResponse(HttpStatus.CREATED, "Book issued successfully", bookIssueService.issueBook(requestDto)));
    }

    @PostMapping("/return")
    public ResponseEntity<ApiResponse<BookIssueResponseDto>> returnBook(@Valid @RequestBody BookReturnRequestDto requestDto) {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Book returned successfully", bookIssueService.returnBook(requestDto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookIssueResponseDto>>> viewIssuedBooks() {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Issued books retrieved successfully", bookIssueService.viewIssuedBooks()));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<BookIssueResponseDto>>> getStudentBookHistory(@PathVariable String studentId) {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Student book history retrieved successfully", bookIssueService.getStudentBookHistory(studentId)));
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
