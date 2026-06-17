package com.college.management.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.college.management.model.Course;

public interface CourseRepository extends MongoRepository<Course, String> {

    Optional<Course> findByCourseName(String courseName);
}
