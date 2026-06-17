package com.college.management.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.college.management.dto.BookIssueRequestDto;
import com.college.management.dto.BookIssueResponseDto;
import com.college.management.dto.BookReturnRequestDto;
import com.college.management.exception.InvalidOperationException;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.model.Book;
import com.college.management.model.BookIssue;
import com.college.management.model.Student;
import com.college.management.repository.BookIssueRepository;
import com.college.management.repository.StudentRepository;

@Service
public class BookIssueService {

    private static final Logger log = LoggerFactory.getLogger(BookIssueService.class);
    private static final String ISSUED = "ISSUED";
    private static final String RETURNED = "RETURNED";

    private final BookIssueRepository bookIssueRepository;
    private final StudentRepository studentRepository;
    private final BookService bookService;

    public BookIssueService(BookIssueRepository bookIssueRepository, StudentRepository studentRepository, BookService bookService) {
        this.bookIssueRepository = bookIssueRepository;
        this.studentRepository = studentRepository;
        this.bookService = bookService;
    }

    public BookIssueResponseDto issueBook(BookIssueRequestDto requestDto) {
        Student student = findStudent(requestDto.getStudentId());
        Book book = bookService.findBook(requestDto.getBookId());
        if (book.getQuantity() == null || book.getQuantity() <= 0) {
            throw new InvalidOperationException("Book is out of stock: " + book.getTitle());
        }
        book.setQuantity(book.getQuantity() - 1);
        bookService.save(book);

        BookIssue issue = BookIssue.builder()
                .issueId(generateId("ISS"))
                .studentId(student.getStudentId())
                .bookId(book.getBookId())
                .issueDate(LocalDate.now())
                .status(ISSUED)
                .build();
        BookIssue savedIssue = bookIssueRepository.save(issue);
        log.info("Issued book {} to student {}", book.getBookId(), student.getStudentId());
        return toDto(savedIssue);
    }

    public BookIssueResponseDto returnBook(BookReturnRequestDto requestDto) {
        BookIssue issue = bookIssueRepository.findByIssueIdAndStatus(requestDto.getIssueId(), ISSUED)
                .orElseThrow(() -> new ResourceNotFoundException("Active issue not found with id: " + requestDto.getIssueId()));
        Book book = bookService.findBook(issue.getBookId());
        book.setQuantity((book.getQuantity() == null ? 0 : book.getQuantity()) + 1);
        bookService.save(book);

        issue.setReturnDate(LocalDate.now());
        issue.setStatus(RETURNED);
        BookIssue savedIssue = bookIssueRepository.save(issue);
        log.info("Returned book issue {}", requestDto.getIssueId());
        return toDto(savedIssue);
    }

    public List<BookIssueResponseDto> viewIssuedBooks() {
        return bookIssueRepository.findByStatusOrderByIssueDateDesc(ISSUED).stream().map(this::toDto).toList();
    }

    public List<BookIssueResponseDto> getStudentBookHistory(String studentId) {
        findStudent(studentId);
        return bookIssueRepository.findByStudentIdOrderByIssueDateDesc(studentId).stream().map(this::toDto).toList();
    }

    private Student findStudent(String studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
    }

    private BookIssueResponseDto toDto(BookIssue issue) {
        return BookIssueResponseDto.builder()
                .issueId(issue.getIssueId())
                .studentId(issue.getStudentId())
                .bookId(issue.getBookId())
                .issueDate(issue.getIssueDate())
                .returnDate(issue.getReturnDate())
                .status(issue.getStatus())
                .build();
    }

    private String generateId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
