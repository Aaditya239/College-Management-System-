package com.college.management.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.college.management.dto.CourseRequestDto;
import com.college.management.dto.CourseResponseDto;
import com.college.management.exception.DuplicateResourceException;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.model.Course;
import com.college.management.repository.CourseRepository;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseResponseDto createCourse(CourseRequestDto requestDto) {
        ensureCourseNameIsUnique(requestDto.getCourseName(), null);
        Course course = toEntity(requestDto);
        course.setCourseId(generateId(requestDto.getCourseId(), "CRS"));
        Course savedCourse = courseRepository.save(course);
        log.info("Created course {}", savedCourse.getCourseId());
        return toDto(savedCourse);
    }

    public CourseResponseDto getCourseById(String courseId) {
        return toDto(findCourse(courseId));
    }

    public List<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll().stream().map(this::toDto).toList();
    }

    public CourseResponseDto updateCourse(String courseId, CourseRequestDto requestDto) {
        Course existingCourse = findCourse(courseId);
        ensureCourseNameIsUnique(requestDto.getCourseName(), courseId);
        existingCourse.setCourseName(requestDto.getCourseName());
        existingCourse.setCredits(requestDto.getCredits());
        existingCourse.setDepartmentId(requestDto.getDepartmentId());
        existingCourse.setStudentIds(requestDto.getStudentIds() == null ? List.of() : requestDto.getStudentIds());
        Course savedCourse = courseRepository.save(existingCourse);
        log.info("Updated course {}", courseId);
        return toDto(savedCourse);
    }

    public void deleteCourse(String courseId) {
        Course course = findCourse(courseId);
        courseRepository.delete(course);
        log.info("Deleted course {}", courseId);
    }

    private Course findCourse(String courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
    }

    private void ensureCourseNameIsUnique(String courseName, String currentCourseId) {
        courseRepository.findByCourseName(courseName).ifPresent(existing -> {
            if (currentCourseId == null || !existing.getCourseId().equals(currentCourseId)) {
                throw new DuplicateResourceException("Course name already exists: " + courseName);
            }
        });
    }

    private Course toEntity(CourseRequestDto requestDto) {
        return Course.builder()
                .courseName(requestDto.getCourseName())
                .credits(requestDto.getCredits())
                .departmentId(requestDto.getDepartmentId())
                .studentIds(requestDto.getStudentIds() == null ? List.of() : requestDto.getStudentIds())
                .build();
    }

    private CourseResponseDto toDto(Course course) {
        return CourseResponseDto.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .credits(course.getCredits())
                .departmentId(course.getDepartmentId())
                .studentIds(course.getStudentIds())
                .build();
    }

    private String generateId(String providedId, String prefix) {
        if (providedId != null && !providedId.isBlank()) {
            return providedId;
        }
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
