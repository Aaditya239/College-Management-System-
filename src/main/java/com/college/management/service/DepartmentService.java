package com.college.management.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.college.management.dto.DepartmentRequestDto;
import com.college.management.dto.DepartmentResponseDto;
import com.college.management.exception.DuplicateResourceException;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.model.Department;
import com.college.management.repository.DepartmentRepository;

@Service
public class DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public DepartmentResponseDto createDepartment(DepartmentRequestDto requestDto) {
        ensureDepartmentNameIsUnique(requestDto.getDepartmentName(), null);
        Department department = Department.builder()
                .departmentName(requestDto.getDepartmentName())
                .hodId(requestDto.getHodId())
                .build();
        department.setDepartmentId(generateId(requestDto.getDepartmentId(), "DEP"));
        Department savedDepartment = departmentRepository.save(department);
        log.info("Created department {}", savedDepartment.getDepartmentId());
        return toDto(savedDepartment);
    }

    public DepartmentResponseDto getDepartmentById(String departmentId) {
        return toDto(findDepartment(departmentId));
    }

    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll().stream().map(this::toDto).toList();
    }

    public DepartmentResponseDto updateDepartment(String departmentId, DepartmentRequestDto requestDto) {
        Department existingDepartment = findDepartment(departmentId);
        ensureDepartmentNameIsUnique(requestDto.getDepartmentName(), departmentId);
        existingDepartment.setDepartmentName(requestDto.getDepartmentName());
        existingDepartment.setHodId(requestDto.getHodId());
        Department savedDepartment = departmentRepository.save(existingDepartment);
        log.info("Updated department {}", departmentId);
        return toDto(savedDepartment);
    }

    public void deleteDepartment(String departmentId) {
        Department department = findDepartment(departmentId);
        departmentRepository.delete(department);
        log.info("Deleted department {}", departmentId);
    }

    private Department findDepartment(String departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
    }

    private void ensureDepartmentNameIsUnique(String departmentName, String currentDepartmentId) {
        departmentRepository.findByDepartmentName(departmentName).ifPresent(existing -> {
            if (currentDepartmentId == null || !existing.getDepartmentId().equals(currentDepartmentId)) {
                throw new DuplicateResourceException("Department name already exists: " + departmentName);
            }
        });
    }

    private DepartmentResponseDto toDto(Department department) {
        return DepartmentResponseDto.builder()
                .departmentId(department.getDepartmentId())
                .departmentName(department.getDepartmentName())
                .hodId(department.getHodId())
                .build();
    }

    private String generateId(String providedId, String prefix) {
        if (providedId != null && !providedId.isBlank()) {
            return providedId;
        }
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
