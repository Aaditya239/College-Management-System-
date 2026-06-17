package com.college.management.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDto {

    private String courseId;
    private String courseName;
    private Integer credits;
    private String departmentId;
    private List<String> studentIds;
}
