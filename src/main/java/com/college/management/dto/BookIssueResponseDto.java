package com.college.management.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookIssueResponseDto {

    private String issueId;
    private String studentId;
    private String bookId;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private String status;
}
