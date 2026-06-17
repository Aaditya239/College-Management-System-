package com.college.management.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "students")
public class Student {

    @Id
    private String studentId;

    private String name;
    private String email;
    private String phone;
    private String address;
    private String departmentId;

    @Builder.Default
    @Field("courseIds")
    private List<String> courseIds = new ArrayList<>();
}
