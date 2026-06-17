package com.college.management.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.college.management.dto.StudentRequestDto;
import com.college.management.dto.StudentResponseDto;
import com.college.management.exception.DuplicateResourceException;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.model.Student;
import com.college.management.repository.StudentRepository;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public StudentResponseDto createStudent(StudentRequestDto requestDto) {
        ensureEmailIsUnique(requestDto.getEmail(), null);
        Student student = toEntity(requestDto);
        student.setStudentId(generateId(requestDto.getStudentId(), "STU"));
        Student savedStudent = studentRepository.save(student);
        log.info("Created student {}", savedStudent.getStudentId());
        return toDto(savedStudent);
    }

    public StudentResponseDto getStudentById(String studentId) {
        return toDto(findStudent(studentId));
    }

    public List<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll().stream().map(this::toDto).toList();
    }

    public StudentResponseDto updateStudent(String studentId, StudentRequestDto requestDto) {
        Student existingStudent = findStudent(studentId);
        ensureEmailIsUnique(requestDto.getEmail(), studentId);
        existingStudent.setName(requestDto.getName());
        existingStudent.setEmail(requestDto.getEmail());
        existingStudent.setPhone(requestDto.getPhone());
        existingStudent.setAddress(requestDto.getAddress());
        existingStudent.setDepartmentId(requestDto.getDepartmentId());
        existingStudent.setCourseIds(requestDto.getCourseIds() == null ? List.of() : requestDto.getCourseIds());
        Student savedStudent = studentRepository.save(existingStudent);
        log.info("Updated student {}", studentId);
        return toDto(savedStudent);
    }

    public void deleteStudent(String studentId) {
        Student student = findStudent(studentId);
        studentRepository.delete(student);
        log.info("Deleted student {}", studentId);
    }

    private Student findStudent(String studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
    }

    private void ensureEmailIsUnique(String email, String currentStudentId) {
        studentRepository.findByEmail(email).ifPresent(existing -> {
            if (currentStudentId == null || !existing.getStudentId().equals(currentStudentId)) {
                throw new DuplicateResourceException("Student email already exists: " + email);
            }
        });
    }

    private Student toEntity(StudentRequestDto requestDto) {
        return Student.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .phone(requestDto.getPhone())
                .address(requestDto.getAddress())
                .departmentId(requestDto.getDepartmentId())
                .courseIds(requestDto.getCourseIds() == null ? List.of() : requestDto.getCourseIds())
                .build();
    }

    private StudentResponseDto toDto(Student student) {
        return StudentResponseDto.builder()
                .studentId(student.getStudentId())
                .name(student.getName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .address(student.getAddress())
                .departmentId(student.getDepartmentId())
                .courseIds(student.getCourseIds())
                .build();
    }

    private String generateId(String providedId, String prefix) {
        if (providedId != null && !providedId.isBlank()) {
            return providedId;
        }
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
