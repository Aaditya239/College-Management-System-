package com.college.management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffRequestDto {

    private String staffId;

    @NotBlank(message = "Staff name is required")
    private String name;

    @NotBlank(message = "Staff email is required")
    @Email(message = "Staff email must be valid")
    private String email;

    @NotBlank(message = "Designation is required")
    private String designation;

    @NotNull(message = "Salary is required")
    @PositiveOrZero(message = "Salary must be zero or positive")
    private Double salary;

    @NotBlank(message = "Department ID is required")
    private String departmentId;
}
