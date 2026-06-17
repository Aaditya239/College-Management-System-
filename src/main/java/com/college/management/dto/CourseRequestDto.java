package com.college.management.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDto {

    private String courseId;

    @NotBlank(message = "Course name is required")
    private String courseName;

    @NotNull(message = "Credits are required")
    @Positive(message = "Credits must be positive")
    private Integer credits;

    @NotBlank(message = "Department ID is required")
    private String departmentId;

    private List<String> studentIds;
}
