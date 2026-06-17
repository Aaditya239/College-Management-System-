# Sample JSON Requests and Responses

## Student

### Create Student Request

```json
{
  "name": "Aman Sharma",
  "email": "aman@example.com",
  "phone": "9876543210",
  "address": "Pune",
  "departmentId": "DEP-12345678",
  "courseIds": ["CRS-11111111"]
}
```

### Create Student Response

```json
{
  "timestamp": "2026-06-11T10:00:00Z",
  "status": 201,
  "message": "Student created successfully",
  "data": {
    "studentId": "STU-12345678",
    "name": "Aman Sharma",
    "email": "aman@example.com",
    "phone": "9876543210",
    "address": "Pune",
    "departmentId": "DEP-12345678",
    "courseIds": ["CRS-11111111"]
  }
}
```

## Staff

### Create Staff Request

```json
{
  "name": "Dr. Priya Nair",
  "email": "priya@example.com",
  "designation": "Professor",
  "salary": 85000,
  "departmentId": "DEP-12345678"
}
```

### Create Staff Response

```json
{
  "timestamp": "2026-06-11T10:00:00Z",
  "status": 201,
  "message": "Staff created successfully",
  "data": {
    "staffId": "STF-12345678",
    "name": "Dr. Priya Nair",
    "email": "priya@example.com",
    "designation": "Professor",
    "salary": 85000,
    "departmentId": "DEP-12345678"
  }
}
```

## Department

### Create Department Request

```json
{
  "departmentName": "Computer Science",
  "hodId": "STF-12345678"
}
```

### Create Department Response

```json
{
  "timestamp": "2026-06-11T10:00:00Z",
  "status": 201,
  "message": "Department created successfully",
  "data": {
    "departmentId": "DEP-12345678",
    "departmentName": "Computer Science",
    "hodId": "STF-12345678"
  }
}
```

## Course

### Create Course Request

```json
{
  "courseName": "Data Structures",
  "credits": 4,
  "departmentId": "DEP-12345678",
  "studentIds": ["STU-12345678"]
}
```

### Create Course Response

```json
{
  "timestamp": "2026-06-11T10:00:00Z",
  "status": 201,
  "message": "Course created successfully",
  "data": {
    "courseId": "CRS-12345678",
    "courseName": "Data Structures",
    "credits": 4,
    "departmentId": "DEP-12345678",
    "studentIds": ["STU-12345678"]
  }
}
```

## Book

### Create Book Request

```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "quantity": 10
}
```

### Create Book Response

```json
{
  "timestamp": "2026-06-11T10:00:00Z",
  "status": 201,
  "message": "Book created successfully",
  "data": {
    "bookId": "BOK-12345678",
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "9780132350884",
    "quantity": 10
  }
}
```

## Book Issue

### Issue Book Request

```json
{
  "studentId": "STU-12345678",
  "bookId": "BOK-12345678"
}
```

### Issue Book Response

```json
{
  "timestamp": "2026-06-11T10:00:00Z",
  "status": 201,
  "message": "Book issued successfully",
  "data": {
    "issueId": "ISS-12345678",
    "studentId": "STU-12345678",
    "bookId": "BOK-12345678",
    "issueDate": "2026-06-11",
    "returnDate": null,
    "status": "ISSUED"
  }
}
```

### Return Book Request

```json
{
  "issueId": "ISS-12345678"
}
```

### Return Book Response

```json
{
  "timestamp": "2026-06-11T10:00:00Z",
  "status": 200,
  "message": "Book returned successfully",
  "data": {
    "issueId": "ISS-12345678",
    "studentId": "STU-12345678",
    "bookId": "BOK-12345678",
    "issueDate": "2026-06-11",
    "returnDate": "2026-06-15",
    "status": "RETURNED"
  }
}
```
