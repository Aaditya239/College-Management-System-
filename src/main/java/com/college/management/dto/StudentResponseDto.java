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
public class StudentResponseDto {

    private String studentId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String departmentId;
    private List<String> courseIds;
}
