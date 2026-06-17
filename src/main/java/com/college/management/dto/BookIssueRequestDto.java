package com.college.management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookIssueRequestDto {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Book ID is required")
    private String bookId;
}
