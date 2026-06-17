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
import com.college.management.dto.StaffRequestDto;
import com.college.management.dto.StaffResponseDto;
import com.college.management.service.StaffService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/staff")
@Validated
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StaffResponseDto>> createStaff(@Valid @RequestBody StaffRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildResponse(HttpStatus.CREATED, "Staff created successfully", staffService.createStaff(requestDto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StaffResponseDto>>> getAllStaff() {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Staff retrieved successfully", staffService.getAllStaff()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponseDto>> getStaffById(@PathVariable("id") String staffId) {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Staff retrieved successfully", staffService.getStaffById(staffId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponseDto>> updateStaff(@PathVariable("id") String staffId,
                                                                     @Valid @RequestBody StaffRequestDto requestDto) {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Staff updated successfully", staffService.updateStaff(staffId, requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStaff(@PathVariable("id") String staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Staff deleted successfully", null));
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
