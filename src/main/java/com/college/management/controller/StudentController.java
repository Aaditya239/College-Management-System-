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
import com.college.management.dto.StudentRequestDto;
import com.college.management.dto.StudentResponseDto;
import com.college.management.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
@Validated
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDto>> createStudent(@Valid @RequestBody StudentRequestDto requestDto) {
        StudentResponseDto responseDto = studentService.createStudent(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(buildResponse(HttpStatus.CREATED, "Student created successfully", responseDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAllStudents() {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Students retrieved successfully", studentService.getAllStudents()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentById(@PathVariable("id") String studentId) {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Student retrieved successfully", studentService.getStudentById(studentId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> updateStudent(@PathVariable("id") String studentId,
                                                                         @Valid @RequestBody StudentRequestDto requestDto) {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Student updated successfully", studentService.updateStudent(studentId, requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable("id") String studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Student deleted successfully", null));
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
