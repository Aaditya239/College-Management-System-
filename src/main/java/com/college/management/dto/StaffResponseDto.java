package com.college.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponseDto {

    private String staffId;
    private String name;
    private String email;
    private String designation;
    private Double salary;
    private String departmentId;
}
