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
import com.college.management.dto.CourseRequestDto;
import com.college.management.dto.CourseResponseDto;
import com.college.management.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
@Validated
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponseDto>> createCourse(@Valid @RequestBody CourseRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildResponse(HttpStatus.CREATED, "Course created successfully", courseService.createCourse(requestDto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponseDto>>> getAllCourses() {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Courses retrieved successfully", courseService.getAllCourses()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDto>> getCourseById(@PathVariable("id") String courseId) {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Course retrieved successfully", courseService.getCourseById(courseId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDto>> updateCourse(@PathVariable("id") String courseId,
                                                                       @Valid @RequestBody CourseRequestDto requestDto) {
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Course updated successfully", courseService.updateCourse(courseId, requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable("id") String courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok(buildResponse(HttpStatus.OK, "Course deleted successfully", null));
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
