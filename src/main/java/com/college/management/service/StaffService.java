package com.college.management.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.college.management.dto.StaffRequestDto;
import com.college.management.dto.StaffResponseDto;
import com.college.management.exception.DuplicateResourceException;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.model.Staff;
import com.college.management.repository.StaffRepository;

@Service
public class StaffService {

    private static final Logger log = LoggerFactory.getLogger(StaffService.class);

    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public StaffResponseDto createStaff(StaffRequestDto requestDto) {
        ensureEmailIsUnique(requestDto.getEmail(), null);
        Staff staff = toEntity(requestDto);
        staff.setStaffId(generateId(requestDto.getStaffId(), "STF"));
        Staff savedStaff = staffRepository.save(staff);
        log.info("Created staff {}", savedStaff.getStaffId());
        return toDto(savedStaff);
    }

    public StaffResponseDto getStaffById(String staffId) {
        return toDto(findStaff(staffId));
    }

    public List<StaffResponseDto> getAllStaff() {
        return staffRepository.findAll().stream().map(this::toDto).toList();
    }

    public StaffResponseDto updateStaff(String staffId, StaffRequestDto requestDto) {
        Staff existingStaff = findStaff(staffId);
        ensureEmailIsUnique(requestDto.getEmail(), staffId);
        existingStaff.setName(requestDto.getName());
        existingStaff.setEmail(requestDto.getEmail());
        existingStaff.setDesignation(requestDto.getDesignation());
        existingStaff.setSalary(requestDto.getSalary());
        existingStaff.setDepartmentId(requestDto.getDepartmentId());
        Staff savedStaff = staffRepository.save(existingStaff);
        log.info("Updated staff {}", staffId);
        return toDto(savedStaff);
    }

    public void deleteStaff(String staffId) {
        Staff staff = findStaff(staffId);
        staffRepository.delete(staff);
        log.info("Deleted staff {}", staffId);
    }

    private Staff findStaff(String staffId) {
        return staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));
    }

    private void ensureEmailIsUnique(String email, String currentStaffId) {
        staffRepository.findByEmail(email).ifPresent(existing -> {
            if (currentStaffId == null || !existing.getStaffId().equals(currentStaffId)) {
                throw new DuplicateResourceException("Staff email already exists: " + email);
            }
        });
    }

    private Staff toEntity(StaffRequestDto requestDto) {
        return Staff.builder()
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .designation(requestDto.getDesignation())
                .salary(requestDto.getSalary())
                .departmentId(requestDto.getDepartmentId())
                .build();
    }

    private StaffResponseDto toDto(Staff staff) {
        return StaffResponseDto.builder()
                .staffId(staff.getStaffId())
                .name(staff.getName())
                .email(staff.getEmail())
                .designation(staff.getDesignation())
                .salary(staff.getSalary())
                .departmentId(staff.getDepartmentId())
                .build();
    }

    private String generateId(String providedId, String prefix) {
        if (providedId != null && !providedId.isBlank()) {
            return providedId;
        }
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
