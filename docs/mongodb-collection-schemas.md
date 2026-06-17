# MongoDB Collection Schemas

## `students`

```json
{
  "_id": "STU-12345678",
  "name": "Aman Sharma",
  "email": "aman@example.com",
  "phone": "9876543210",
  "address": "Pune",
  "departmentId": "DEP-12345678",
  "courseIds": ["CRS-11111111", "CRS-22222222"]
}
```

## `staff`

```json
{
  "_id": "STF-12345678",
  "name": "Dr. Priya Nair",
  "email": "priya@example.com",
  "designation": "Professor",
  "salary": 85000.0,
  "departmentId": "DEP-12345678"
}
```

## `departments`

```json
{
  "_id": "DEP-12345678",
  "departmentName": "Computer Science",
  "hodId": "STF-12345678"
}
```

## `courses`

```json
{
  "_id": "CRS-12345678",
  "courseName": "Data Structures",
  "credits": 4,
  "departmentId": "DEP-12345678",
  "studentIds": ["STU-12345678"]
}
```

## `books`

```json
{
  "_id": "BOK-12345678",
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "quantity": 10
}
```

## `book_issues`

```json
{
  "_id": "ISS-12345678",
  "studentId": "STU-12345678",
  "bookId": "BOK-12345678",
  "issueDate": "2026-06-11",
  "returnDate": null,
  "status": "ISSUED"
}
```

## Relationship Notes

- Department to HOD is stored through `hodId` in `departments`.
- One department can be referenced by many students, staff members, and courses through `departmentId`.
- Student and course many-to-many links are represented through `courseIds` in `students` and `studentIds` in `courses`.
- Book borrowing history is stored in `book_issues`.
