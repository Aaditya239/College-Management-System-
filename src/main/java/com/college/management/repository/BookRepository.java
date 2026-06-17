package com.college.management.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.college.management.model.Book;

public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findByIsbn(String isbn);
}
