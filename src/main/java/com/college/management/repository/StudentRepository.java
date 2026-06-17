package com.college.management.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.college.management.model.Student;

public interface StudentRepository extends MongoRepository<Student, String> {

    Optional<Student> findByEmail(String email);
}
