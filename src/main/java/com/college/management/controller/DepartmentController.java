package com.college.management.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.college.management.dto.ApiResponse;
import com.college.management.dto.DepartmentRequestDto;
import com.college.management.dto.DepartmentResponseDto;
import com.college.management.service.DepartmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/departments")
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> createDepartment(@Valid @RequestBody DepartmentRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildResponse(HttpStatus.CREATED, "Department created successfully", departmentService.createDepartment(requestDto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getAllDepartments() {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Departments retrieved successfully", departmentService.getAllDepartments()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> getDepartmentById(@PathVariable("id") String departmentId) {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Department retrieved successfully", departmentService.getDepartmentById(departmentId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> updateDepartment(@PathVariable("id") String departmentId,
                                                                               @Valid @RequestBody DepartmentRequestDto requestDto) {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Department updated successfully", departmentService.updateDepartment(departmentId, requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable("id") String departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Department deleted successfully", null));
    }

    private <T> ApiResponse<T> buildResponse(HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.setTimestamp(Instant.now());
        response.setStatus(status.value());
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
