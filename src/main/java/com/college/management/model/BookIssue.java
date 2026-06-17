package com.college.management.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "book_issues")
public class BookIssue {

    @Id
    private String issueId;

    private String studentId;
    private String bookId;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private String status;
}
