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
public class DepartmentRequestDto {

    private String departmentId;

    @NotBlank(message = "Department name is required")
    private String departmentName;

    @NotBlank(message = "HOD ID is required")
    private String hodId;
}
