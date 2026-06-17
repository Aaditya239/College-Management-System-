package com.college.management.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.college.management.model.Staff;

public interface StaffRepository extends MongoRepository<Staff, String> {

    Optional<Staff> findByEmail(String email);
}
