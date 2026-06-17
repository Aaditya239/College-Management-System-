package com.college.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.college.management.model.BookIssue;

public interface BookIssueRepository extends MongoRepository<BookIssue, String> {

    List<BookIssue> findByStudentIdOrderByIssueDateDesc(String studentId);

    List<BookIssue> findByStatusOrderByIssueDateDesc(String status);

    Optional<BookIssue> findByIssueIdAndStatus(String issueId, String status);
}
