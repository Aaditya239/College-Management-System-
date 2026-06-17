package com.college.management.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.college.management.model.Department;

public interface DepartmentRepository extends MongoRepository<Department, String> {

    Optional<Department> findByDepartmentName(String departmentName);
}
