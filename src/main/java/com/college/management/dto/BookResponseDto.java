package com.college.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDto {

    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
}
